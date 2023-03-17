package com.klub.store.api.controller.restapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klub.store.api.config.route.DataBlockRoutes;
import com.klub.store.api.controller.dto.request.datablock.SaveDataBlockRequest;
import com.klub.store.api.controller.dto.response.datablock.GetDataBlockRefResponse;
import com.klub.store.api.controller.dto.response.datablock.SaveDataBlockResponse;
import com.klub.store.api.model.entity.KlubBlockGroup;
import com.klub.store.api.model.entity.KlubDataBlock;
import com.klub.store.api.model.entity.KlubDataBlockStore;
import com.klub.store.api.repository.KlubBlockGroupRepository;
import com.klub.store.api.repository.KlubDataBlockRepository;
import com.klub.store.api.repository.KlubDataStoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application.properties"
)
@AutoConfigureMockMvc
class DataBlockControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KlubBlockGroupRepository klubBlockGroupRepository;

    @Autowired
    private KlubDataStoreRepository klubDataStoreRepository;

    @Autowired
    private KlubDataBlockRepository klubDataBlockRepository;


    private KlubBlockGroup blockGroup;
    private KlubDataBlockStore dataStore;

    @BeforeEach
    void beforeEach(){
        this.blockGroup = new KlubBlockGroup(
                "block_group" + UUID.randomUUID().toString(),
                0, 20,
                null, null, "data",
                4, new ArrayList<>()
        );

        this.klubBlockGroupRepository.saveAndFlush(this.blockGroup);

        this.dataStore = new KlubDataBlockStore(
                "identifier_store" + UUID.randomUUID().toString(), "owner", true, 4,
                100, new ArrayList<>()
        );
        this.klubDataStoreRepository.saveAndFlush(this.dataStore);

    }

    @AfterEach()
    void tearDown(){
        this.dataStore = null;
        this.blockGroup = null;
    }

    @Test
    void createDataBlock() throws Exception {
        //given
        SaveDataBlockRequest saveDataBlockRequest = new SaveDataBlockRequest(
                this.blockGroup.getData(), this.blockGroup.getDataSize(),
                dataStore.getIdentifier(), this.blockGroup.getIdentifier()
        );

        //when
        MvcResult result = mockMvc.perform(post(DataBlockRoutes.getCreateUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(saveDataBlockRequest)))
                .andExpect(status().isOk())
                .andReturn();

        SaveDataBlockResponse response = this.objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<SaveDataBlockResponse>() {
                }
        );

        assertNotNull(response.getData().getIdentifier());
        assertEquals(this.dataStore.getIdentifier(), response.getData().getDataStoreRef());
        assertEquals(this.blockGroup.getIdentifier(), response.getData().getBlockGroupRef());

    }

    @Test
    void getDataBlockRef() throws Exception {
        //given
        KlubDataBlock block =  new KlubDataBlock(
                "identifier", 4, "data", true,
                this.blockGroup, this.dataStore, "reference"
        );
        this.klubDataBlockRepository.saveAndFlush(block);

        //when
        MvcResult result = mockMvc.perform(get(DataBlockRoutes.
                        getGetDataBlockGetRefByBlockGroupUrl(
                                this.blockGroup.getIdentifier(), this.dataStore.getIdentifier()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        GetDataBlockRefResponse response = this.objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<GetDataBlockRefResponse>() {
                }
        );

        assertEquals(block.getIdentifier(), response.getReference());
    }

    @Test
    void updateDataBlockDownloadState() throws Exception {
        //given
        KlubDataBlock block =  new KlubDataBlock(
                UUID.randomUUID().toString(), 4, "data", true,
                this.blockGroup, this.dataStore, "reference"
        );
        this.klubDataBlockRepository.saveAndFlush(block);

        //when
        MvcResult result = mockMvc.perform(patch(DataBlockRoutes.
                        getUpdateDataBlockDownloadStateUrl(
                                block.getIdentifier(), true))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Optional<KlubDataBlock> newBlockContainer = this.klubDataBlockRepository.findByIdentifier(block.getIdentifier());
        assertTrue(newBlockContainer.isPresent());
        assertNull(newBlockContainer.get().getData());
    }
}