package com.klub.store.api.controller.dto;

import com.klub.store.api.model.entity.KlubDataBlock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @class DataBlockDto default representation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DataBlockDto {
    private Long id;
    private String identifier;
    private Integer size;

    private String blockGroupRef;
    private String dataStoreRef;


    public DataBlockDto(KlubDataBlock block) {
        this.id = block.getId();
        this.size = block.getSize();
        this.identifier = block.getIdentifier();

        this.blockGroupRef = block.getGroup().getIdentifier();
        this.dataStoreRef = block.getStore().getIdentifier();
    }
}
