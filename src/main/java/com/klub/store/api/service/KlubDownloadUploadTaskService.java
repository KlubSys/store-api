package com.klub.store.api.service;

import com.klub.store.api.model.entity.KlubBlockGroup;
import com.klub.store.api.model.entity.KlubDownloadUploadTask;
import com.klub.store.api.repository.KlubBlockGroupRepository;
import com.klub.store.api.repository.KlubDownloadUploadTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class KlubDownloadUploadTaskService {

    private final KlubDownloadUploadTaskRepository klubDownloadUploadTaskRepository;
    private final KlubBlockGroupRepository klubBlockGroupRepository;


    @Autowired
    public KlubDownloadUploadTaskService(KlubDownloadUploadTaskRepository klubDownloadUploadTaskRepository,
                                         KlubBlockGroupRepository klubBlockGroupRepository) {
        this.klubDownloadUploadTaskRepository = klubDownloadUploadTaskRepository;
        this.klubBlockGroupRepository = klubBlockGroupRepository;
    }

    public KlubDownloadUploadTask createTask(KlubBlockGroup group){
        KlubDownloadUploadTask task = klubDownloadUploadTaskRepository.findOneByBlocGroup(group);
        if (task == null) {
            task = new KlubDownloadUploadTask();
            task.setData(null);
            task.setBlocGroup(group);
        } else return task;

        return klubDownloadUploadTaskRepository.save(task);
    }

    public void updateData(KlubDownloadUploadTask task, String data){
        if (task.getData() != null) return;
        task.setData(data);
        klubDownloadUploadTaskRepository.save(task);
    }

    public List<KlubDownloadUploadTask> getDownloadsForBlocGroupRef(List<String> blocGroupRef){
        return blocGroupRef.stream().map(ref -> {
            Optional<KlubBlockGroup> blocGroupCtn = klubBlockGroupRepository
                    .findByIdentifier(ref);
            if (blocGroupCtn.isEmpty()) return null;
            return klubDownloadUploadTaskRepository
                    .findOneByBlocGroupAndData(blocGroupCtn.get(), null);
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
