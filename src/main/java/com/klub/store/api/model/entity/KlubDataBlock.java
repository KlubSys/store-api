package com.klub.store.api.model.entity;

import com.klub.store.api.model.LongAsIdEntity;
import com.klub.store.api.model.UuidAsIdEntity;

import javax.persistence.*;

/**
 * A block of data stored by the store. It's a part of file
 * The next part ( data block ) can be in another store
 *
 * Each block of data contains information about it's group,
 * size and the reference in the store storage support
 */
@Entity
@Table(name = "data_block")
public class KlubDataBlock extends LongAsIdEntity {

    /**
     * An identifier for this block data
     */
    @Column(nullable = false)
    private String identifier;

    /**
     * The total size of the content to store
     */
    @Column(nullable = false)
    private Integer size;

    /**
     * The block data content. The content will be erased
     * after downloaded by the store owner
     */
    private String data;

    /**
     * The percent of group block content downloaded into store
     * storage support.
     *
     * Bt default, we've downloaded 0 percent so false
     */
    private Boolean downloaded = false;

    /**
     * Block Group ref
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    private KlubBlockGroup group;

    /**
     * Rhe store which hold the data block
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    private KlubDataBlockStore store;

    /**
     * The reference to use as name of the downloaded data in
     * storage support
     *
     * Only reserved to the owner of the store
     * Will take it a hash if possible in next version
     */
    private String reference;

    public KlubDataBlock() {
        super();
    }

    public KlubDataBlock(Long id, String identifier, Integer size, String data,
                         Boolean downloaded, KlubBlockGroup group,
                         KlubDataBlockStore store, String reference) {
        super(id);
        this.identifier = identifier;
        this.size = size;
        this.data = data;
        this.downloaded = downloaded;
        this.group = group;
        this.store = store;
        this.reference = reference;
    }

    public KlubDataBlock(String identifier, Integer size, String data,
                         Boolean downloaded, KlubBlockGroup group,
                         KlubDataBlockStore store, String reference) {
        this.identifier = identifier;
        this.size = size;
        this.data = data;
        this.downloaded = downloaded;
        this.group = group;
        this.store = store;
        this.reference = reference;
    }


    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Boolean downloaded) {
        this.downloaded = downloaded;
    }

    public KlubBlockGroup getGroup() {
        return group;
    }

    public void setGroup(KlubBlockGroup group) {
        this.group = group;
    }

    public KlubDataBlockStore getStore() {
        return store;
    }

    public void setStore(KlubDataBlockStore store) {
        this.store = store;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
