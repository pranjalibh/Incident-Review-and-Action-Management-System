package com.tus.incidentmanagement.dao;

import com.tus.incidentmanagement.entity.ActionItemEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ActionItemRepository extends CrudRepository<ActionItemEntity, Long> {

    @Query("SELECT a FROM ActionItemEntity a WHERE a.incident.id = :incidentId")
    List<ActionItemEntity> findActionsByIncidentId(@Param("incidentId") Long incidentId);

    @Query("SELECT a FROM ActionItemEntity a WHERE a.id = :id")
    ActionItemEntity findActionById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ActionItemEntity a SET a.completed = true WHERE a.id = :id")
    void completeAction(@Param("id") Long id);

    @Query("""
            SELECT COUNT(a)
            FROM ActionItemEntity a
            WHERE a.incident.id = :incidentId
            AND a.completed = false
            """)
    long countOpenActions(@Param("incidentId") Long incidentId);

    @Query("""
        SELECT a FROM ActionItemEntity a
        JOIN FETCH a.incident
        WHERE a.assignedTo.username = :username
        """)
    List<ActionItemEntity> findByAssignedUsername(@Param("username") String username);
}
