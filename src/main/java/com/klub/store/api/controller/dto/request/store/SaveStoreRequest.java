package com.klub.store.api.controller.dto.request.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @class SaveStoreRequest  Create store request input
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SaveStoreRequest {
    /**
     * The client ref
     */
    private String owner;
}
