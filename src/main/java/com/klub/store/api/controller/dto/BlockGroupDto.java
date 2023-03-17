package com.klub.store.api.controller.dto;

import com.klub.store.api.model.entity.KlubBlockGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

/**
 * @class BlockGroupDto default representation of a store
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlockGroupDto {
    private Long id;
    private String identifier;

    private Integer deployed = 0;
    private Integer maxBlock;
    private String next;
    private String previous;
    private String data;
    private Integer dataSize;

    /**
     * Constructor
     * @param group
     */
    public BlockGroupDto(KlubBlockGroup group) {
        this.id = group.getId();
        this.identifier = group.getIdentifier();

        if (group.getNext() != null)
            this.next = group.getNext().getIdentifier();
        if (group.getPrevious() != null)
            this.previous = group.getPrevious().getIdentifier();

        this.dataSize = group.getDataSize();
    }
}
