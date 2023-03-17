package com.klub.store.api.controller.restapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klub.store.api.config.route.BlockGroupRoutes;
import com.klub.store.api.controller.dto.request.blockGroup.SaveBlockGroupRequest;
import com.klub.store.api.controller.dto.response.blockGroup.GetNextBlockGroupRefResponse;
import com.klub.store.api.controller.dto.response.blockGroup.SaveBlockGroupResponse;
import com.klub.store.api.model.entity.KlubBlockGroup;
import com.klub.store.api.repository.KlubBlockGroupRepository;
import com.klub.store.api.repository.KlubDataStoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application.properties"
)
@AutoConfigureMockMvc
class BlockGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KlubDataStoreRepository klubDataStoreRepository;

    @Autowired
    private KlubBlockGroupRepository klubBlockGroupRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createBlockGroup() throws Exception {
        //given
        SaveBlockGroupRequest saveBlockGroupRequest = new SaveBlockGroupRequest();
        saveBlockGroupRequest.setData("data");
        saveBlockGroupRequest.setDataSize(4);
        saveBlockGroupRequest.setPrevious(null);
        saveBlockGroupRequest.setNext(null);

        //when
        MvcResult result = mockMvc.perform(post(BlockGroupRoutes.getCreateUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveBlockGroupRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        SaveBlockGroupResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<SaveBlockGroupResponse>() {
                }
        );

        //then
        assertEquals(saveBlockGroupRequest.getDataSize(), response.getData().getDataSize());

        assertNull(response.getData().getNext()); //for a first block group
        assertNull(response.getData().getPrevious()); //for a first block group

        //[START][create a block group with a prev block group]
        //given
        /*SaveBlockGroupRequest saveBlockGroupWithPrevRequest = new SaveBlockGroupRequest();
        saveBlockGroupRequest.setData("data-prev");
        saveBlockGroupRequest.setDataSize(9);
        saveBlockGroupRequest.setPrevious(response.getIdentifier());
        saveBlockGroupRequest.setNext(null);

        //when
        MvcResult resultWithPrev = mockMvc.perform(
                post(BlockGroupRoutes.getCreateUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveBlockGroupWithPrevRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        SaveBlockGroupResponse responseWithPrev = objectMapper.readValue(
                resultWithPrev.getResponse().getContentAsString(),
                new TypeReference<SaveBlockGroupResponse>() {
                }
        );

        //then
        assertEquals(saveBlockGroupWithPrevRequest.getDataSize(), responseWithPrev.getDataSize());

        assertNull(responseWithPrev.getNext()); //for a non first block group
        assertEquals(responseWithPrev.getIdentifier(), response.getIdentifier()); //for a non first block group
        //[END][create a block group with a prev block group]
        */

    }

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void createBlockData() {
    }

    @Test
    @Description("Test can get the nex block reference with a current valid block identifier given")
    void findNextBlockGroup() throws Exception {
        //given
        KlubBlockGroup group = new KlubBlockGroup(
                "identifiant_", 0, 20,
                null, null, "data", 4,
                new ArrayList<>()
        );

        group = this.klubBlockGroupRepository.saveAndFlush(group);
        assertNotNull(group.getId());

        KlubBlockGroup nextGroup = new KlubBlockGroup(
                "identifiant_next", 0, 20,
                null, group, "data", 4,
                new ArrayList<>()
        );

        nextGroup = this.klubBlockGroupRepository.saveAndFlush(nextGroup);
        assertNotNull(nextGroup.getId());

        //when
        MvcResult result = mockMvc.perform(get(BlockGroupRoutes.getGetGroupFindNextUrl(group.getIdentifier()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        GetNextBlockGroupRefResponse response = this.objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<GetNextBlockGroupRefResponse>(){

                }
        );

        assertEquals(nextGroup.getIdentifier(), response.getRef());

    }
}