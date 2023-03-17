package com.klub.store.api.repository;

import com.klub.store.api.model.entity.KlubDataBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KlubDataBlockRepository extends JpaRepository<KlubDataBlock, Long> {
    Optional<KlubDataBlock> findByIdentifier(String dataBlockId);

    @Query("SELECT d " +
            "FROM KlubDataBlock d " +
            "WHERE d.group.identifier = :id")
    Optional<KlubDataBlock> findByBlockGroupIdentifier(@Param("id") String blockGroupId);

    @Query("SELECT d " +
            "FROM KlubDataBlock d " +
            "WHERE d.group.identifier = :gid " +
            "AND d.store.identifier = :sid")
    Optional<KlubDataBlock> findByBlockGroupIdentifierAndStoreIdentifier(
            @Param("gid") String blockGroupId, @Param("sid") String storeId);

    @Query("SELECT d " +
            "FROM KlubDataBlock d " +
            "WHERE d.store.identifier = :id")
    List<KlubDataBlock> findByBlockStoreIdentifier(@Param("id") String storeId);
}
