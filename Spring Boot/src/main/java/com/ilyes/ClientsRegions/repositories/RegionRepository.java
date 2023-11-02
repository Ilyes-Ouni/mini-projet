package com.ilyes.ClientsRegions.repositories;

import com.ilyes.ClientsRegions.entities.Client;
import com.ilyes.ClientsRegions.entities.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("SELECT c FROM Client c WHERE c.region.idRegion = :regionId")
    List<Client> findByRegion(@Param("regionId") Long regionId);

    @Query("SELECT c FROM Client c WHERE c.region.idRegion = :regionId")
    Page<Client> findClientsByRegionId(@Param("regionId") long regionId, Pageable pageable);

}
