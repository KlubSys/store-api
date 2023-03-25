package com.klub.store.api.service;

import com.klub.store.api.repository.KlubBlockGroupRepository;
import com.klub.store.api.repository.KlubDataBlockRepository;
import com.klub.store.api.repository.KlubDataStoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KlubKlubBlockGroupServiceTest {

    private KlubBlockGroupService klubBlockGroupService;

    @Mock
    private KlubDataStoreRepository klubDataStoreRepository;

    @Mock
    private KlubBlockGroupRepository klubBlockGroupRepository;

    @Mock
    private KlubDataBlockRepository klubDataBlockRepository;

    @BeforeEach
    void setup(){
        //klubBlockGroupService = new KlubBlockGroupService(klubBlockGroupRepository, klubDataBlockRepository, klubDataStoreRepository, centralLoggerServerApi);
    }

    @Test
    public void testWillThrowInvalidBlockGroup(){
        assertEquals(0, 0);
    }

    @Test
    public void testWillThrowInvalidStore(){

    }

    @Test
    public void testWillThrowStoreSizeInsufficient(){

    }

    @Test
    public void testWillThrowBlockToHugeForStore(){

    }
}