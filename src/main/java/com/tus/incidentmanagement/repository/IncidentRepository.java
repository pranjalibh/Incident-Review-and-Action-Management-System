package com.tus.incidentmanagement.repository;

import com.tus.incidentmanagement.entity.IncidentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IncidentRepository extends CrudRepository<IncidentEntity,Long> {
    @Query("SELECT i FROM IncidentEntity i WHERE i.id = :id")
    Optional<IncidentEntity> findIncidentById(@Param("id") Long id);
}
