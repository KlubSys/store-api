package com.klub.store.api.controller.restapi;

import com.klub.store.api.controller.dto.BlockGroupDto;
import com.klub.store.api.controller.dto.request.blockGroup.SaveBlockGroupRequest;
import com.klub.store.api.controller.dto.request.datablock.SaveDataBlockRequest;
import com.klub.store.api.controller.dto.response.blockGroup.GetAllBlockGroupResponse;
import com.klub.store.api.controller.dto.response.blockGroup.GetNextBlockGroupRefResponse;
import com.klub.store.api.controller.dto.response.blockGroup.GetSingleBlockGroupResponse;
import com.klub.store.api.controller.dto.response.blockGroup.SaveBlockGroupResponse;
import com.klub.store.api.exception.NotFoundException;
import com.klub.store.api.model.entity.KlubBlockGroup;
import com.klub.store.api.model.entity.KlubDataBlock;
import com.klub.store.api.service.KlubBlockGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/block_groups")
public class BlockGroupController {

    private final KlubBlockGroupService klubBlockGroupService;

    @Autowired
    public BlockGroupController(KlubBlockGroupService klubBlockGroupService) {
        this.klubBlockGroupService = klubBlockGroupService;
    }

    /**
     * Create a block group
     *
     * @param body
     * @return
     */
    @PostMapping
    public ResponseEntity<SaveBlockGroupResponse> createBlockGroup(
            @RequestBody SaveBlockGroupRequest body){
        //TODO validate body
        KlubBlockGroup group = null;
        try {
            group = this.klubBlockGroupService.saveGroup(body);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        BlockGroupDto data = new BlockGroupDto(group);
        SaveBlockGroupResponse response = new SaveBlockGroupResponse(data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<GetAllBlockGroupResponse> getAll(){
        List<KlubBlockGroup> groups = this.klubBlockGroupService.getAll();

        List<BlockGroupDto> data = groups.stream()
                .map(BlockGroupDto::new).collect(Collectors.toList());

        GetAllBlockGroupResponse response = new GetAllBlockGroupResponse(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get a bloCk group by id
     * @param groupId
     * @return
     */
    @GetMapping(path = "/{group_id}")
    public ResponseEntity<GetSingleBlockGroupResponse> getById(
            @PathVariable(value = "group_id") String groupId) {
        KlubBlockGroup group = null;
        try {
            group = this.klubBlockGroupService.getById(groupId);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        BlockGroupDto data = new BlockGroupDto(group);

        GetSingleBlockGroupResponse response = new GetSingleBlockGroupResponse(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create a block data for a block group.
     * @deprecated this link is not valid anymore. It will throw service unavailable
     * in the future. Instead, use the data block resource
     *
     * @param groupId
     * @param body
     * @return
     */
    @PostMapping(path = "/{group_id}/data_block")
    public ResponseEntity<KlubDataBlock> createBlockData(@PathVariable("group_id") String groupId,
                                                         @RequestBody SaveDataBlockRequest body) {
        return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Get the next block
     * @param currentBlockGroupRef
     * @return
     */
    @GetMapping(path = "/{currentRef}/_nextBlock")
    public ResponseEntity<GetNextBlockGroupRefResponse> findNextBlockGroup(
            @PathVariable("currentRef") String currentBlockGroupRef){
        KlubBlockGroup data = this.klubBlockGroupService.getNextBlock(currentBlockGroupRef);

        GetNextBlockGroupRefResponse response = new GetNextBlockGroupRefResponse(
                data != null ? data.getIdentifier() : null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
