package com.klub.store.api.controller.dto.response.datablock;

import com.klub.store.api.controller.dto.DataBlockDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @class SaveDataBlockResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SaveDataBlockResponse {
    /**
     * main body data
     */
    private DataBlockDto data;
}
