package com.klub.store.api.repository;


import com.klub.store.api.model.entity.KlubBlockGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KlubBlockGroupRepository extends JpaRepository<KlubBlockGroup, Long> {

    @Query(value =
        "SELECT b FROM KlubBlockGroup b " +
            "WHERE b.previous.identifier = :prev"
    )
    public KlubBlockGroup getNextBlockGroup(@Param("prev") String prev);

    @Query(value =
            "SELECT b FROM KlubBlockGroup b " +
                    "WHERE b.identifier = :id"
    )
    Optional<KlubBlockGroup> findByIdentifier(@Param("id") String blockGroupIdentifier);
}
