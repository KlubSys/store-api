package com.klub.store.api.model.entity;

import com.klub.store.api.model.UuidAsIdEntity;

import javax.persistence.*;

@Entity
@Table(name = "download_task")
public class KlubDownloadUploadTask extends UuidAsIdEntity {

    @JoinColumn(nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private KlubBlockGroup blocGroup;
    private String data;

    public KlubDownloadUploadTask() {
        super();
    }

    public KlubDownloadUploadTask(String id, KlubBlockGroup blocGroup, String data) {
        super(id);
        this.blocGroup = blocGroup;
        this.data = data;
    }

    public KlubBlockGroup getBlocGroup() {
        return blocGroup;
    }

    public void setBlocGroup(KlubBlockGroup blocGroup) {
        this.blocGroup = blocGroup;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
