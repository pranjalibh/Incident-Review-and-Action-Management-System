package com.tus.incidentmanagement.dao;

import com.tus.incidentmanagement.entity.IncidentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IncidentRepository extends CrudRepository<IncidentEntity,Long> {
    @Query("SELECT i FROM IncidentEntity i WHERE i.id = :id")
    Optional<IncidentEntity> findIncidentById(@Param("id") Long id);
}
