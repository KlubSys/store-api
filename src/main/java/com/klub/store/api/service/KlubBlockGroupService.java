package com.klub.store.api.service;

import com.klub.store.api.controller.dto.request.blockGroup.SaveBlockGroupRequest;
import com.klub.store.api.model.entity.KlubBlockGroup;
import com.klub.store.api.repository.KlubBlockGroupRepository;
import com.klub.store.api.repository.KlubDataBlockRepository;
import com.klub.store.api.repository.KlubDataStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class KlubBlockGroupService {

    private final KlubBlockGroupRepository klubBlockGroupRepository;
    private final KlubDataBlockRepository klubDataBlockRepository;
    private final KlubDataStoreRepository klubDataStoreRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public KlubBlockGroupService(KlubBlockGroupRepository klubBlockGroupRepository, KlubDataBlockRepository klubDataBlockRepository,
                                 KlubDataStoreRepository klubDataStoreRepository) {
        this.klubBlockGroupRepository = klubBlockGroupRepository;
        this.klubDataBlockRepository = klubDataBlockRepository;
        this.klubDataStoreRepository = klubDataStoreRepository;
    }


    /**
     * Create a block group
     *
     * @param body
     * @return
     */
    public KlubBlockGroup saveGroup(SaveBlockGroupRequest body) throws Exception {
        String identifier = "block" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString();
        KlubBlockGroup group = new KlubBlockGroup();
        group.setIdentifier(identifier);

        //Validate the next and the previous
        if (body.getNext() != null) {
            Optional<KlubBlockGroup> nextBlockContainer = this.klubBlockGroupRepository
                    .findByIdentifier(body.getNext());
            if(nextBlockContainer.isPresent())
                group.setNext(nextBlockContainer.get());
            else
                throw new Exception("Invalid block group reference for the next");
        }else {
            group.setNext(null);
        }

        if (body.getPrevious() != null) {
            Optional<KlubBlockGroup> previousBlockContainer = this.klubBlockGroupRepository
                    .findByIdentifier(body.getPrevious());
            if(previousBlockContainer.isPresent())
                group.setPrevious(previousBlockContainer.get());
            else
                throw new Exception("Invalid block group reference for the previous");
        }else {
            group.setPrevious(null);
        }

        group.setData(body.getData());
        System.out.println(body.getData());
        group.setDataSize(body.getDataSize());
        group.setMaxBlock(1000); //TODO use static variable or a config param

        return this.klubBlockGroupRepository.save(group);
    }

    public List<KlubBlockGroup> getAll(){
        return this.klubBlockGroupRepository.findAll();
    }

    /**
     * Get block group by its identifier
     *
     * @param id
     * @return
     * @throws Exception when no block group found
     */
    public KlubBlockGroup getById(String id) throws Exception {
        Optional<KlubBlockGroup> groupContainer = this.klubBlockGroupRepository.findByIdentifier(id);
        if (groupContainer.isPresent())
            return groupContainer.get();

        throw new Exception("Invalid KlubBlockGroup identifier");
    }

    /**
     * Get the next block group
     * @param currentBlockGroupRef
     * @return
     */
    public KlubBlockGroup getNextBlock(String currentBlockGroupRef){
        return this.klubBlockGroupRepository.getNextBlockGroup(currentBlockGroupRef);
    }
}
