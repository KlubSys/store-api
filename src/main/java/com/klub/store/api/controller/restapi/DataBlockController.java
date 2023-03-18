package com.klub.store.api.controller.restapi;

import com.klub.store.api.controller.dto.DataBlockDto;
import com.klub.store.api.controller.dto.request.datablock.SaveDataBlockRequest;
import com.klub.store.api.controller.dto.response.datablock.GetAllDataBlockResponse;
import com.klub.store.api.controller.dto.response.datablock.GetDataBlockRefResponse;
import com.klub.store.api.controller.dto.response.datablock.GetSingleDataBlockResponse;
import com.klub.store.api.controller.dto.response.datablock.SaveDataBlockResponse;
import com.klub.store.api.model.entity.KlubDataBlock;
import com.klub.store.api.service.KlubDataBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/data_block")
public class DataBlockController {

    private final KlubDataBlockService klubDataBlockService;

    @Autowired
    public DataBlockController(KlubDataBlockService klubDataBlockService) {
        this.klubDataBlockService = klubDataBlockService;
    }

    /**
     * Handle create a block data request
     *
     * @param body
     * @return
     * @throws Exception
     */
    @PostMapping(path = "")
    public ResponseEntity<SaveDataBlockResponse> createDataBlock(
            @RequestBody SaveDataBlockRequest body) throws Exception {

        //TODO validate the body
        KlubDataBlock blockData = this.klubDataBlockService.createDataBlock(body);

        DataBlockDto data = new DataBlockDto(blockData);

        SaveDataBlockResponse response = new SaveDataBlockResponse(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get data block by identifier
     *
     * @param blockIdentifier the data block identifier
     * @return
     * @throws Exception
     */
    @GetMapping(path = "/{block_identifier}")
    public ResponseEntity<GetSingleDataBlockResponse> getByIdentifier(
            @PathVariable("block_identifier") String blockIdentifier) throws Exception {

        KlubDataBlock dataBlock = this.klubDataBlockService.getDataBlock(blockIdentifier);

        DataBlockDto data = new DataBlockDto(dataBlock);

        GetSingleDataBlockResponse response = new GetSingleDataBlockResponse(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get data block
     *
     * @return
     * @throws Exception
     */
    @GetMapping(path = "")
    public ResponseEntity<GetAllDataBlockResponse> getDataBlocks() {

        List<KlubDataBlock> dataBlock = this.klubDataBlockService.getAll();

        List<DataBlockDto> data = dataBlock.stream().map(DataBlockDto::new).collect(Collectors.toList());

        GetAllDataBlockResponse response = new GetAllDataBlockResponse(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update the download state
     *
     * @param blockId
     * @return
     * @throws Exception
     */
    @PatchMapping(path = "/{block_id}/_download/{value}")
    public ResponseEntity<Void> updateDownloadReference(
            @PathVariable("block_id") String blockId,
            @PathVariable("value") Boolean value) throws Exception {

        this.klubDataBlockService.updateDownloadState(blockId, value);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Get a data block ref by a block group identifier and a store identifier.
     * We assume that a latest request controlled that the store is online.
     * @param blockGroupId
     * @return
     */
    @GetMapping(path = "/_block_group/{block_group_id}/_store/{store_id}/_ref")
    public ResponseEntity<GetDataBlockRefResponse> getDataBlockRef(
            @PathVariable("block_group_id") String blockGroupId,
            @PathVariable("store_id") String storeId) throws Exception {
        KlubDataBlock dataBlock = this.klubDataBlockService.getDataBlock(blockGroupId, storeId);

        GetDataBlockRefResponse response = new GetDataBlockRefResponse(dataBlock.getIdentifier());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
