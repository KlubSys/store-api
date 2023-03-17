package com.klub.store.api.service;


import com.klub.store.api.helper.Pair;
import com.klub.store.api.model.entity.KlubBlockGroup;
import com.klub.store.api.model.entity.KlubDataBlock;
import com.klub.store.api.model.entity.KlubDataBlockStore;
import com.klub.store.api.repository.KlubBlockGroupRepository;
import com.klub.store.api.repository.KlubDataBlockRepository;
import com.klub.store.api.repository.KlubDataStoreRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class KlubStoreService {

    private final KlubDataStoreRepository klubDataStoreRepository;
    private final KlubBlockGroupRepository klubBlockGroupRepository;
    private final KlubDataBlockRepository klubDataBlockRepository;

    @Autowired
    public KlubStoreService(KlubDataStoreRepository klubDataStoreRepository,
                            KlubBlockGroupRepository klubBlockGroupRepository,
                            KlubDataBlockRepository klubDataBlockRepository) {
        this.klubDataStoreRepository = klubDataStoreRepository;
        this.klubBlockGroupRepository = klubBlockGroupRepository;
        this.klubDataBlockRepository = klubDataBlockRepository;
    }

    /**
     * Create a store
     *
     * TODO write the function to take a dto as param
     * @param store
     * @return
     */
    public KlubDataBlockStore createStore(KlubDataBlockStore store){
        String identifier = UUID.randomUUID().toString();
        store.setIdentifier(identifier);

        return this.klubDataStoreRepository.save(store);
    }

    public List<KlubDataBlockStore> findAll(){
        return this.klubDataStoreRepository.findAll();
    }

    /**
     * Update the store size
     *
     * @param id
     * @param size
     * @return
     * @throws Exception
     */
    @Transactional
    public KlubDataBlockStore updateStoreSize(String id, Integer size) throws Exception {
        Optional<KlubDataBlockStore> storeContainer = this.klubDataStoreRepository.findByIdentifier(id);
        if (storeContainer.isPresent()){
            if ( size < KlubDataBlockStore.MIN_SIZE || (
                    storeContainer.get().getSize() == KlubDataBlockStore.MIN_SIZE
                            && size == KlubDataBlockStore.MIN_SIZE)){
                throw new Exception("Invalid size amount");
            }

            KlubDataBlockStore klubDataBlockStore = storeContainer.get();
            //TODO recalculate the free value
            klubDataBlockStore.setSize(size);
        }else {
            throw new Exception("Invalid Store identifier");
        }
        return storeContainer.get();
    }

    @Transactional
    public KlubDataBlockStore updateStoreOnlineStatus(String identifier, boolean isOnline) throws Exception {
        Optional<KlubDataBlockStore> storeContainer = this.klubDataStoreRepository.findByIdentifier(identifier);
        if (!storeContainer.isPresent()){
           throw new Exception("Invalid Store");
        }

        KlubDataBlockStore klubDataBlockStore = storeContainer.get();
        klubDataBlockStore.setOnline(isOnline);

        return klubDataBlockStore;
    }

    /**
     * Get a klub store by an id
     * @param id
     * @return
     */
    public KlubDataBlockStore getById(String id) throws Exception {
        Optional<KlubDataBlockStore> storeContainer = this.klubDataStoreRepository.findByIdentifier(id);
        if(!storeContainer.isPresent()){
            throw new Exception("Invalid store identifier");
        }

        return storeContainer.get();
    }

    /**
     * Get data blocks
     * @param storeId
     * @return
     */
    public List<KlubDataBlock> getDataBlockIdentifiers(String storeId){
        return this.klubDataBlockRepository.findByBlockStoreIdentifier(storeId);
    }

    /**
     * Get a random store that is online and has a link
     * to a block group that has the provided ref as identifier
     *
     * It uses a random search. Taking an interval, take a random from
     * this interval if not then construct the left interval and the
     * right interval
     *
     * @param blockGroupRef
     * @param dataSize the data size we wanted to store
     */
    public KlubDataBlockStore getRandomOnlineStores(final String blockGroupRef, final long dataSize){
        KlubDataBlockStore resultDataBlockStore = null;

        //Find the block group and add it to the store
        Optional<KlubBlockGroup> blockGroupContainer =
                this.klubBlockGroupRepository.findByIdentifier(blockGroupRef);
        if (blockGroupContainer.isEmpty()){
            System.out.println("[ERROR] The group reference is invalid");
            return null;
        }
        //Here using a linear search , TODO improve it later with binary or tertiary search or

        //An improved algorithm with random search
        //OperationResult<StoreApiFindRandomStoreResponse> result = null;

        //For development purpose, we will use the whole size of the table
        //but 

        Random rand = new Random();
        //TODO Declare Pair to be a Long
        //A Queue of ranges, left bound and the right bound
        Queue<Pair<Integer, Integer>> ranges = new LinkedList<>();
        ranges.add(
                new Pair<Integer, Integer>(1, ((int) this.klubDataStoreRepository.count() + 1))
        );

        boolean hadFoundStore = false;
        do {
            Pair<Integer, Integer> r = ranges.poll();
            if (r == null){
                break;
            }

            int index = -1;
            if (r.getKey() - r.getValue() == 0){
                index = r.getKey();
            }else
                index = rand.nextInt(Math.abs( r.getKey() - r.getValue() ))
                        + Math.min( r.getKey(), r.getValue() );

            //Fill next range to check
            if (index + 1 <= Math.max(r.getValue(), r.getKey()) )
                ranges.add(new Pair<>(index + 1, Math.max(r.getValue(), r.getKey())));
            if (index - 1 >=  Math.min(r.getValue(), r.getKey()))
                ranges.add(new Pair<>(Math.min(r.getValue(), r.getKey()), index));

            Optional<KlubDataBlockStore> ksContainer =
                    this.klubDataStoreRepository.findById((long) index);


            //Check the container content
            if (!ksContainer.isPresent()){
                continue;
            }

            KlubDataBlockStore ks = ksContainer.get();
            //Compute free space to check if we have an available space
            int freeSpace = ((ks.getSize() * ks.getFree()) / 100) - 1;
            if (freeSpace >= dataSize && ks.getOnline()) {
                resultDataBlockStore = ks;

                //Update the free space
                long free = (((freeSpace - dataSize) * 100) / ks.getSize());
                System.out.println("\n\n\nBlock Ref to add " + blockGroupRef + "\n\n\n\n");

                ranges.clear();
                hadFoundStore = true;

                //Save updates
                this.klubDataStoreRepository.saveAndFlush(ks);
                break;
            }

        } while (!hadFoundStore || ranges.isEmpty());

        if (!hadFoundStore) {
            System.out.println("No available store found");
            return null;
        }

        return resultDataBlockStore;
    }

    /**
     * Providing a block group reference (identifier as table column name)
     * we return all store attached to this reference
     * @param blockGroupRef
     * @return
     */
    public List<KlubDataBlockStore> getStoresByBlockGroup(@NonNull final String blockGroupRef){
        return this.klubDataStoreRepository.getByBlockGroupRef(blockGroupRef);
    }


}
