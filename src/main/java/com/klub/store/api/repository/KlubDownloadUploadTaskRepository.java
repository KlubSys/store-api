package com.klub.store.api.repository;

import com.klub.store.api.model.entity.KlubBlockGroup;
import com.klub.store.api.model.entity.KlubDownloadUploadTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KlubDownloadUploadTaskRepository extends JpaRepository<KlubDownloadUploadTask, String> {

    KlubDownloadUploadTask findOneByBlocGroup(KlubBlockGroup group);

    KlubDownloadUploadTask findOneByBlocGroupAndData(KlubBlockGroup klubBlockGroup, String data);
}
