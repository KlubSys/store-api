package com.klub.store.api.controller.dto.response.blockGroup;

import com.klub.store.api.controller.dto.BlockGroupDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @class SaveBlockGroupResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetAllBlockGroupResponse {
    /**
     * The main body
     */
    private List<BlockGroupDto> data;
}
