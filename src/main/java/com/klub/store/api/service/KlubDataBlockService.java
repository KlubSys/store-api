package com.klub.store.api.service;

import com.klub.store.api.controller.dto.request.datablock.SaveDataBlockRequest;
import com.klub.store.api.model.entity.KlubBlockGroup;
import com.klub.store.api.model.entity.KlubDataBlock;
import com.klub.store.api.model.entity.KlubDataBlockStore;
import com.klub.store.api.repository.KlubBlockGroupRepository;
import com.klub.store.api.repository.KlubDataBlockRepository;
import com.klub.store.api.repository.KlubDataStoreRepository;
import com.klub.store.api.service.api.CentralLoggerServerApi;
import com.klub.store.api.service.api.dto.CentralServerLogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class KlubDataBlockService {

    private final KlubDataBlockRepository klubDataBlockRepository;
    private final KlubDataStoreRepository klubDataStoreRepository;
    private final KlubBlockGroupRepository klubBlockGroupRepository;
    private final CentralLoggerServerApi centralLoggerServerApi;

    @Autowired
    public KlubDataBlockService(KlubDataBlockRepository klubDataBlockRepository,
                                KlubDataStoreRepository klubDataStoreRepository,
                                KlubBlockGroupRepository klubBlockGroupRepository, CentralLoggerServerApi centralLoggerServerApi) {
        this.klubDataBlockRepository = klubDataBlockRepository;
        this.klubDataStoreRepository = klubDataStoreRepository;
        this.klubBlockGroupRepository = klubBlockGroupRepository;
        this.centralLoggerServerApi = centralLoggerServerApi;
    }

    public void createBlock(KlubDataBlock block, String storeRef){
        String identifier = UUID.randomUUID().toString();
        block.setIdentifier(identifier);

        this.klubDataStoreRepository.findByIdentifier(storeRef);

    }

    /**
     * Create a block data
     *
     * @param body
     * @return
     * @throws Exception
     */
    public KlubDataBlock createDataBlock(SaveDataBlockRequest body) throws Exception {
        String blockGroupId = body.getBlockGroup();
        String storeId = body.getStore();

        KlubDataBlock block = new KlubDataBlock();

        String identifier = UUID.randomUUID().toString();
        block.setIdentifier(identifier);
        block.setData(body.getData()); //Remember this content will be erased after downloaded
        block.setSize(body.getSize());

        //Validate the block group
        Optional<KlubBlockGroup> blockGroupContainer = this.klubBlockGroupRepository
                .findByIdentifier(blockGroupId);
        if (!blockGroupContainer.isPresent()) {
            throw new Exception("Invalid KlubBlockGroup identifier");
        }

        Optional<KlubDataBlockStore> storeContainer = this.klubDataStoreRepository.findByIdentifier(storeId);
        if (!storeContainer.isPresent()) {
            throw new Exception("Invalid Store identifier");
        }

        block.setGroup(blockGroupContainer.get());

        //Check the storage free space
        KlubDataBlockStore klubDataBlockStore = storeContainer.get();
        if (klubDataBlockStore.getFree() <= 0) { //Free space is measured in percent
            throw new Exception("Store size not sufficient");
        }

        int free = (klubDataBlockStore.getSize() * klubDataBlockStore.getFree() )/ 100;
        if (free < block.getSize()){
            throw  new Exception("Block to huge for store");
        }

        int newFree = (int) Math.ceil(
                (
                        ( (double)(free - block.getSize()) ) / klubDataBlockStore.getSize()
                ) * 100 );
        //TODO pay attention to transaction on the store
        //Update the store free space
        klubDataBlockStore.setFree(newFree);
        block.setStore(klubDataBlockStore);

        //Set the download reference
        String downloadRef = "download_" + UUID.randomUUID().toString().replace('-', '_');
        block.setReference(downloadRef);

        this.klubDataStoreRepository.save(klubDataBlockStore);

        centralLoggerServerApi.dispatchLog(CentralServerLogMessage.builder()
                .text("Created a databloc").build());

        return this.klubDataBlockRepository.save(block);

    }

    /**
     * Get a data block by a block group a store
     * @param blockGroupId
     * @param storeId
     * @return
     */
    public KlubDataBlock getDataBlock(String blockGroupId, String storeId) throws Exception {
        Optional<KlubDataBlock> dataBlockContainer = this.klubDataBlockRepository.
                findByBlockGroupIdentifierAndStoreIdentifier(blockGroupId, storeId);
        if (!dataBlockContainer.isPresent()){
            throw new Exception("Invalid Block group identifier for a data block");
        }

        return dataBlockContainer.get();
    }

    /**
     * Get data block by it's identifier
     * @param blockIdentifier
     * @return
     * @throws Exception
     */
    public KlubDataBlock getDataBlock(String blockIdentifier) throws Exception {
        Optional<KlubDataBlock> dataBlockContainer = this.klubDataBlockRepository
                .findByIdentifier(blockIdentifier);

        if(dataBlockContainer.isPresent()){
            throw new Exception("Invalid data block identifier provided");
        }
        return dataBlockContainer.get();
    }

    /**
     * Get the download reference of a block data
     *
     * @param dataBlockId
     * @return
     * @throws Exception
     */
    public KlubDataBlock getDownloadReference(String dataBlockId) throws Exception {
        Optional<KlubDataBlock> blockContainer = this.klubDataBlockRepository.findByIdentifier(dataBlockId);
        if (!blockContainer.isPresent()){
            throw  new Exception("No Block data Found");
        }

        return blockContainer.get();
    }


    /**
     * Update the download state of a data block
     * @param blockId
     * @param value
     * @throws Exception
     */
    public void updateDownloadState(String blockId, Boolean value) throws Exception {
        Optional<KlubDataBlock> blockContainer = this.klubDataBlockRepository.findByIdentifier(blockId);
        if (!blockContainer.isPresent()){
            throw  new Exception("No Block data Found");
        }

        KlubDataBlock dataBlock = blockContainer.get();
        dataBlock.setDownloaded(value);

        if (value = true){
            //Erase the data content
            dataBlock.setData(null);
        }
    }

    /**
     * Get all data block
     * @return
     */
    public List<KlubDataBlock> getAll() {
        return this.klubDataBlockRepository.findAll();
    }
}
