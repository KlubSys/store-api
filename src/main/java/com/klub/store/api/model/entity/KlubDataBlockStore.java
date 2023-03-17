package com.klub.store.api.model.entity;

import com.klub.store.api.model.LongAsIdEntity;
import com.klub.store.api.model.UuidAsIdEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A store hold by a store owner. I t contains data blocks that are not
 * linked.
 */
@Entity
@Table(name = "store")
public class KlubDataBlockStore extends LongAsIdEntity {
    /**
     * Minimal size to have in a store
     */
    public static final int MIN_SIZE = 1000;

    /**
     * A random UUID
     */
    @Column(nullable = false)
    private String identifier;

    /**
     * The owner key
     */
    @Column(columnDefinition = "text")
    private String owner;

    /**
     * The store is online at the moment it was requested
     *
     * By default, the store is online
     */
    private Boolean online = true;

    /**
     * Size in byte
     *
     * By default, we allocate 1 MB
     */
    private Integer size = 1000; //TODO CHANGE TO 1 MILLION AS NECESSARY

    /**
     * Available free space in the store. Expressed in percentage
     * No decimal allowed, all is in integer format
     *
     * By default, we have 100% free space
     */
    private Integer free = 100;

    @OneToMany(mappedBy = "store")
    private List<KlubDataBlock> klubDataBlocks;


    public KlubDataBlockStore() {
        super();
        this.klubDataBlocks = new ArrayList<>();
    }

    public KlubDataBlockStore(Long id, String identifier, String owner, Boolean online, Integer size,
                              Integer free, List<KlubDataBlock> klubDataBlocks) {
        super(id);
        this.identifier = identifier;
        this.owner = owner;
        this.online = online;
        this.size = size;
        this.free = free;
        this.klubDataBlocks = klubDataBlocks;
    }

    public KlubDataBlockStore(String identifier, String owner, Boolean online, Integer size,
                              Integer free, List<KlubDataBlock> klubDataBlocks) {
        this.identifier = identifier;
        this.owner = owner;
        this.online = online;
        this.size = size;
        this.free = free;
        this.klubDataBlocks = klubDataBlocks;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getFree() {
        return free;
    }

    public void setFree(Integer free) {
        this.free = free;
    }

    public List<KlubDataBlock> getKlubDataBlocks() {
        return klubDataBlocks;
    }

    public void setKlubDataBlocks(List<KlubDataBlock> klubDataBlocks) {
        this.klubDataBlocks = klubDataBlocks;
    }
}
