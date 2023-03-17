package com.klub.store.api.model.entity;

import com.klub.store.api.model.LongAsIdEntity;
import com.klub.store.api.model.UuidAsIdEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A KlubBlockGroup a container for blocks references. It contains
 * all blocks that have exactly the same data, although they are
 * distributed on many stores.
 *
 * Each block group has information about the previous and the next group
 * in order to find all block data
 */
@Entity
@Table(name = "block_group")
public class KlubBlockGroup extends LongAsIdEntity {


    /**
     * Identify a group of blocks
     */
    @Column(nullable = false, unique = true)
    private String identifier;

    /**
     * The number of instance of the data block hold by the block group
     * that have been already deployed
     *
     * By default, we've deployed 0 instance to stores
     */
    private Integer deployed = 0;

    /**
     * Maximum of instance of the held block data to be deployed
     */
    private Integer maxBlock;

    /**
     * The next block group. There we will find the next data block
     *
     * It can be null
     */
    @OneToOne(fetch = FetchType.LAZY)
    private KlubBlockGroup next;

    /**
     * The previous block group. There we will find the previous data block
     *
     * It can be null
     */
    @OneToOne(fetch = FetchType.LAZY)
    private KlubBlockGroup previous;

    /**
     * Since data are in base64 format; it contains the data to save into
     * a block data in a store.
     *
     * After max deploy reached, we erase this content
     */
    @Column(columnDefinition = "text")
    private String data;

    /**
     * Data size in bytes
     */
    @Column(nullable = false)
    private Integer dataSize;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<KlubDataBlock> klubDataBlocks = new ArrayList<>();;


    public KlubBlockGroup() {
        super();
    }

    public KlubBlockGroup(Long id, String identifier, Integer deployed, Integer maxBlock,
                          KlubBlockGroup next, KlubBlockGroup previous, String data,
                          Integer dataSize, List<KlubDataBlock> klubDataBlocks) {
        super(id);
        this.identifier = identifier;
        this.deployed = deployed;
        this.maxBlock = maxBlock;
        this.next = next;
        this.previous = previous;
        this.data = data;
        this.dataSize = dataSize;
        this.klubDataBlocks = klubDataBlocks;
    }

    public KlubBlockGroup(String identifier, Integer deployed, Integer maxBlock,
                          KlubBlockGroup next, KlubBlockGroup previous, String data,
                          Integer dataSize, List<KlubDataBlock> klubDataBlocks) {
        this.identifier = identifier;
        this.deployed = deployed;
        this.maxBlock = maxBlock;
        this.next = next;
        this.previous = previous;
        this.data = data;
        this.dataSize = dataSize;
        this.klubDataBlocks = klubDataBlocks;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getDeployed() {
        return deployed;
    }

    public void setDeployed(Integer deployed) {
        this.deployed = deployed;
    }

    public Integer getMaxBlock() {
        return maxBlock;
    }

    public void setMaxBlock(Integer maxBlock) {
        this.maxBlock = maxBlock;
    }

    public KlubBlockGroup getNext() {
        return next;
    }

    public void setNext(KlubBlockGroup next) {
        this.next = next;
    }

    public KlubBlockGroup getPrevious() {
        return previous;
    }

    public void setPrevious(KlubBlockGroup previous) {
        this.previous = previous;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    public List<KlubDataBlock> getKlubDataBlocks() {
        return klubDataBlocks;
    }

    public void setKlubDataBlocks(List<KlubDataBlock> klubDataBlocks) {
        this.klubDataBlocks = klubDataBlocks;
    }
}
