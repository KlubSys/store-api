package com.klub.store.api.controller.dto.response.blockGroup;

import com.klub.store.api.controller.dto.BlockGroupDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @class SaveBlockGroupResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetSingleBlockGroupResponse {
    /**
     * The main body
     */
    private BlockGroupDto data;
}
