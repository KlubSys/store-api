package com.klub.store.api.repository;

import com.klub.store.api.model.entity.KlubDataBlockStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KlubDataStoreRepository extends JpaRepository<KlubDataBlockStore, Long> {
    @Query(value = "SELECT count(k.id) FROM KlubDataBlockStore k")
    long count();

    @Query(value =
            "SELECT s FROM KlubDataBlockStore s " +
            "INNER JOIN s.klubDataBlocks d " +
            "WHERE d.group.identifier = :ref")
    List<KlubDataBlockStore> getByBlockGroupRef(@Param("ref") String blockGroupRef);

    @Query(value = "SELECT s " +
            "FROM KlubDataBlockStore s " +
            "WHERE s.identifier = :ref")
    Optional<KlubDataBlockStore> findByIdentifier(@Param("ref") String storeRef);
}
