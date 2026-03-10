package com.tus.incidentmanagement.repository;

import com.tus.incidentmanagement.entity.ActionItemEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActionItemRepository extends CrudRepository<ActionItemEntity, Long> {

    @Query("SELECT a FROM ActionItemEntity a WHERE a.incident.id = :incidentId")
    List<ActionItemEntity> findActionsByIncidentId(@Param("incidentId") Long incidentId);

    @Query("SELECT a FROM ActionItemEntity a WHERE a.id = :id")
    ActionItemEntity findActionById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ActionItemEntity a SET a.completed = true WHERE a.id = :id")
    void completeAction(@Param("id") Long id);

}
