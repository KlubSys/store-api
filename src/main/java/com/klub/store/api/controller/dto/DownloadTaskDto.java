package com.klub.store.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.klub.store.api.model.entity.KlubDownloadUploadTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadTaskDto {

    private String id;
    @JsonProperty("hasData")
    private boolean hasData;
    private String data;
    private String blocGroupRef;

    public static DownloadTaskDto from(KlubDownloadUploadTask task){
        if (task == null) return null;
        return new DownloadTaskDto(task.getId(),
                task.getData() != null, task.getData(), task.getBlocGroup().getIdentifier());
    }
}
