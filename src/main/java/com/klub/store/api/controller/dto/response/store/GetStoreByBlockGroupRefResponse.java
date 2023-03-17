package com.klub.store.api.controller.dto.response.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetStoreByBlockGroupRefResponse {
    /**
     * A list of store identifier
     */
    private List<String> data;
}
