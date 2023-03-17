package com.klub.store.api.controller.dto.response.blockGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @class GetNextBlockGroupRefResponse default representation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetNextBlockGroupRefResponse {
    /**
     * The next block reference
     */
    private String ref;
}
