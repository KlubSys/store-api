package com.klub.store.api.controller.restapi;

import com.klub.store.api.controller.dto.StoreDto;
import com.klub.store.api.controller.dto.request.store.GetRandomOnlineStoreRequest;
import com.klub.store.api.controller.dto.request.store.SaveStoreRequest;
import com.klub.store.api.controller.dto.response.store.*;
import com.klub.store.api.model.entity.KlubDataBlock;
import com.klub.store.api.model.entity.KlubDataBlockStore;
import com.klub.store.api.service.KlubStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/api/v1/stores")
public class StoreController {

    private final KlubStoreService klubStoreService;

    @Autowired
    public StoreController(KlubStoreService klubStoreService) {
        this.klubStoreService = klubStoreService;
    }

    /**
     * Create a new store
     * @param body
     * @return
     */
    @PostMapping
    public ResponseEntity<SaveStoreResponse> createStore(@RequestBody SaveStoreRequest body){
        //TODO Validate the body
        KlubDataBlockStore klubDataBlockStore = new KlubDataBlockStore();
        klubDataBlockStore.setOwner(body.getOwner());

        klubDataBlockStore = klubStoreService.createStore(klubDataBlockStore);

        StoreDto data = new StoreDto(klubDataBlockStore);
        SaveStoreResponse response = new SaveStoreResponse( data );
        return  new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update a store size
     *
     * @param id
     * @param size
     * @return
     * @throws Exception
     */
    @PatchMapping(path = "/{store_id}/_size/{value}")
    public ResponseEntity<String> updateStoreSize(@PathVariable(value = "store_id") String id,
                                                @PathVariable("value") Integer size) {
        try {
            KlubDataBlockStore klubDataBlockStore = klubStoreService.updateStoreSize(id, size);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Update the online state of the store. on or off
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    @PatchMapping(path = "/{store_id}/_online/{value}")
    public ResponseEntity<Void> updateOnlineStatus(@PathVariable(value = "store_id") String id,
                                                @PathVariable("value") String status) throws Exception {
        if ( !(status.equals("on") || status.equals("off")) ){
            return new ResponseEntity<> ((Void) null, HttpStatus.NOT_FOUND);
        }

        KlubDataBlockStore klubDataBlockStore = klubStoreService.updateStoreOnlineStatus(
                id, status.equals("on"));
        return new ResponseEntity<>((Void) null, HttpStatus.OK);

    }

    /**
     * Get Store by Id
     * @param id
     * @return
     */
    @GetMapping(path = "/{store_id}")
    public ResponseEntity<GetSingleStoreResponse> findById(@PathVariable(value = "store_id") String id) throws Exception {
        KlubDataBlockStore store = klubStoreService.getById(id);

        StoreDto data = new StoreDto(store);

        GetSingleStoreResponse response = new GetSingleStoreResponse(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get data block identifiers
     * @param id the store identifier
     * @return aa list of string
     */
    @GetMapping(path = "/{store_id}/data_blocks")
    public ResponseEntity<List<Map<String, Object>>> getDataBlockByStore(@PathVariable(value = "store_id") String id){
        List<KlubDataBlock> dataBlocks = klubStoreService.getDataBlockIdentifiers(id);

        List<Map<String, Object>> data = dataBlocks.stream().map(
                (bloc) -> {
                    HashMap<String, Object> d = new HashMap<>();
                    d.put("bloc", bloc.getIdentifier());
                    d.put("group", bloc.getGroup().getIdentifier());
                    return d;

                }).collect(Collectors.toList());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Get all stores
     * @return
     */
    @GetMapping
    public ResponseEntity<GetAllStoreResponse> getAll(){
        //TODO implements pagination
        List<KlubDataBlockStore> stores = klubStoreService.findAll();

        List<StoreDto> data = stores.stream()
                .map(StoreDto::new)
                .collect(Collectors.toList());

        GetAllStoreResponse response = new GetAllStoreResponse(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get a random online store
     * Called when distributing a data
     */
    @GetMapping(path = "/_online/_random")
    public ResponseEntity<GetRandomOnlineStoreResponse> getRandomOnlineStore(
            @RequestParam("dataSize") int dataSize, @RequestParam("blocGroupRef") String blocGroupRef
            //@RequestBody GetRandomOnlineStoreRequest body
    ){

        /*KlubDataBlockStore store = this.klubStoreService.getRandomOnlineStores(
                body.getBlockGroupRef(), body.getDataSize());*/
        KlubDataBlockStore store = this.klubStoreService.getRandomOnlineStores(
                blocGroupRef, dataSize);
        String ref = "";

        if (store != null){
            ref = store.getIdentifier();
        }

        GetRandomOnlineStoreResponse response = new GetRandomOnlineStoreResponse(ref);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get stores attached to a given block group. The link is made
     * when a block data is added
     *
     * @param blockGroupRef
     * @return
     */
    @GetMapping(path = "/_blockGroup/{ref}")
    public ResponseEntity<GetStoreByBlockGroupRefResponse> getStoreByBlockGroupReference(
            @PathVariable("ref") String blockGroupRef){
        List<KlubDataBlockStore> stores = this.klubStoreService.getStoresByBlockGroup(blockGroupRef);

        List<String> data = stores.stream()
                .map(KlubDataBlockStore::getIdentifier)
                .collect(Collectors.toList());

        GetStoreByBlockGroupRefResponse response = new GetStoreByBlockGroupRefResponse(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
