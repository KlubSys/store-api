package com.klub.store.api.controller.dto.response.store;

import com.klub.store.api.controller.dto.StoreDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @class SaveStoreResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetAllStoreResponse {
    /**
     * main body data
     */
    private List<StoreDto> data;
}
