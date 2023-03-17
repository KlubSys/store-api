package com.klub.store.api.controller.dto;

import com.klub.store.api.model.entity.KlubDataBlockStore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @class StoreDto default representation of a store
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoreDto {
    private Long id;
    private String identifier;
    private Boolean online = false;
    private Integer size = 1000; //TODO CHANGE TO 1 MILLION AS NECESSARY
    private Integer free = 100;

    public StoreDto(KlubDataBlockStore store) {
        this.id = store.getId();
        this.online = store.getOnline();
        this.size = store.getSize();
        this.free = store.getFree();
        this.identifier = store.getIdentifier();
    }
}
