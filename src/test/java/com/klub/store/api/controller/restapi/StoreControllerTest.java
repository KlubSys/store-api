package com.klub.store.api.controller.restapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klub.store.api.config.route.StoreRoutes;
import com.klub.store.api.controller.dto.request.store.GetRandomOnlineStoreRequest;
import com.klub.store.api.controller.dto.request.store.SaveStoreRequest;
import com.klub.store.api.controller.dto.response.store.GetRandomOnlineStoreResponse;
import com.klub.store.api.controller.dto.response.store.GetStoreByBlockGroupRefResponse;
import com.klub.store.api.controller.dto.response.store.SaveStoreResponse;
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
import org.springframework.context.annotation.Description;
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
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KlubDataStoreRepository klubDataStoreRepository;

    @Autowired
    private KlubBlockGroupRepository klubBlockGroupRepository;

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

    @AfterEach
    void tearDown(){
        this.dataStore = null;
        this.blockGroup = null;
    }


    @Test
    void createStore() throws Exception {
        //Given
        String owner = UUID.randomUUID().toString();
        SaveStoreRequest request = new SaveStoreRequest(owner);

        //when
        MvcResult result = mockMvc.perform(post(
                        StoreRoutes.getCreateUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        //then
        SaveStoreResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<SaveStoreResponse>() {
                }
        );

        assertNotEquals(null, response.getData().getIdentifier());
    }

    @Test
    void updateStoreSize() throws Exception {
        //given
        String owner = UUID.randomUUID().toString();
        SaveStoreRequest request = new SaveStoreRequest(owner);

        MvcResult result = mockMvc.perform(post(
                        StoreRoutes.getCreateUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        //then
        SaveStoreResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<SaveStoreResponse>() {

                }
        );


        //[START][TEST When supplied a valid amount]
        //when
        int newSize = 2000;
        mockMvc.perform(patch(
                StoreRoutes.getUpdateSizeUrl(response.getData().getIdentifier(), newSize))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //Then
        Optional<KlubDataBlockStore> store = this.klubDataStoreRepository
                .findByIdentifier(response.getData().getIdentifier());
        assertEquals(true, store.isPresent());
        assertEquals(newSize, store.get().getSize());
        //[END][TEST When supplied a valid amount]

        //[START][TEST When supplied an amount that is less than the minimum then return exception]
        //when
        //TODO use static variable for the minimum size
        newSize = 200;
        mockMvc.perform(patch(
                        StoreRoutes.getUpdateSizeUrl(response.getData().getIdentifier(), newSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //then
        //[END][TEST When supplied an amount that is less than the minimum then return exception]

        //[START][TEST When supplied a non existing store]
        //when
        //TODO use static variable for the minimum size
        newSize = 200;
        mockMvc.perform(patch(
                        StoreRoutes.getUpdateSizeUrl(response.getData().getIdentifier() + "bad", newSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); //then
        //[END][TEST When supplied a non existing store]

    }

    @Test
    void updateOnlineStatus() {
    }

    @Test
    void findById() {
    }

    @Test
    void getAll() {
    }

    @Test
    void createDataBlock() {
    }

    @Test
    void getDataBlock() throws Exception {
    }

    @Test
    void getRandomOnlineStore() throws Exception{
        //given
        String owner = UUID.randomUUID().toString();
        SaveStoreRequest request = new SaveStoreRequest(owner);
        MvcResult result = mockMvc.perform(post(
                        StoreRoutes.getCreateUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();


        SaveStoreResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<SaveStoreResponse>() {

                }
        );

        KlubBlockGroup blockGroup = new KlubBlockGroup(
                "block_group" + UUID.randomUUID().toString(),
                0, 20,
                null, null, "data",
                4, new ArrayList<>()
        );

        this.klubBlockGroupRepository.saveAndFlush(blockGroup);

        //when
        GetRandomOnlineStoreRequest randomOnlineStoreRequest =
                new GetRandomOnlineStoreRequest(blockGroup.getDataSize(), blockGroup.getIdentifier());
        MvcResult resultGetRandom = mockMvc.perform(get(StoreRoutes
                .getGetRandomOnlineUrl(blockGroup.getDataSize(), blockGroup.getIdentifier()))
                        .contentType(MediaType.APPLICATION_JSON))
                        //.content(objectMapper.writeValueAsString(randomOnlineStoreRequest)))
                .andExpect(status().isOk()).andReturn();

        GetRandomOnlineStoreResponse getRandomOnlineStoreResponse = objectMapper.readValue(
                resultGetRandom.getResponse().getContentAsString(),
                new TypeReference<GetRandomOnlineStoreResponse>() {

                }
        );

        //then
        assertNotNull(response.getData().getIdentifier());
        assertNotNull(getRandomOnlineStoreResponse.getStoreRef());
        //assertEquals(response.getIdentifier(), getRandomOnlineStoreResponse.getStoreRef());
    }

    @Test
    @Description("We have one store online and one store not online")
    void getRandomOnlineStoreTwo() throws Exception{
        //given
        KlubDataBlockStore dataStoreOnline = new KlubDataBlockStore(
                "identifier_store" + UUID.randomUUID().toString(), "owner", true, KlubDataBlockStore.MIN_SIZE,
                100, new ArrayList<>()
        );
        this.klubDataStoreRepository.saveAndFlush(dataStoreOnline);
        KlubDataBlockStore dataStoreNotOnline = new KlubDataBlockStore(
                "identifier_store" + UUID.randomUUID().toString(), "owner", false, KlubDataBlockStore.MIN_SIZE,
                100, new ArrayList<>()
        );
        this.klubDataStoreRepository.saveAndFlush(dataStoreNotOnline);

        KlubBlockGroup blockGroup = new KlubBlockGroup(
                "block_group" + UUID.randomUUID().toString(),
                0, 20,
                null, null, "data",
                4, new ArrayList<>()
        );

        this.klubBlockGroupRepository.saveAndFlush(blockGroup);

        //when
        GetRandomOnlineStoreRequest randomOnlineStoreRequest =
                new GetRandomOnlineStoreRequest(blockGroup.getDataSize(), blockGroup.getIdentifier());
        MvcResult resultGetRandom = mockMvc.perform(get(StoreRoutes
                .getGetRandomOnlineUrl(blockGroup.getDataSize(), blockGroup.getIdentifier()))
                        .contentType(MediaType.APPLICATION_JSON))
                        //.content(objectMapper.writeValueAsString(randomOnlineStoreRequest)))
                .andExpect(status().isOk()).andReturn();

        GetRandomOnlineStoreResponse getRandomOnlineStoreResponse = objectMapper.readValue(
                resultGetRandom.getResponse().getContentAsString(),
                new TypeReference<GetRandomOnlineStoreResponse>() {

                }
        );

        //then
        assertNotNull(getRandomOnlineStoreResponse.getStoreRef());
        //assertEquals(dataStoreOnline.getIdentifier(), getRandomOnlineStoreResponse.getStoreRef());
        //assertNotEquals(dataStoreNotOnline.getIdentifier(), getRandomOnlineStoreResponse.getStoreRef());
    }

    @Test
    void getStoreByBlockGroupReference() throws Exception {
        //Given
        KlubDataBlock block =  new KlubDataBlock(
                UUID.randomUUID().toString(), 4, "data", true,
                this.blockGroup, this.dataStore, "reference"
        );
        this.klubDataBlockRepository.saveAndFlush(block);

        //When
        MvcResult result = mockMvc.perform(get(
                    StoreRoutes.getStoreGetByBlockGroupRefUrl(this.blockGroup.getIdentifier())
                ).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        GetStoreByBlockGroupRefResponse response =   this.objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<GetStoreByBlockGroupRefResponse>() {
                }
        );

        assertNotEquals(0, response.getData().size());
        assertEquals(this.dataStore.getIdentifier(), response.getData().get(0));

        //Then
    }
}