package com.klub.store.api.controller.dto.response.store;

import com.klub.store.api.controller.dto.StoreDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @class SaveStoreResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetSingleStoreResponse {
    /**
     * main body data
     */
    private StoreDto data;
}
