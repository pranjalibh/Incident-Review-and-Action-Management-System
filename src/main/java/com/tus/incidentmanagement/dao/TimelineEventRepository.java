package com.tus.incidentmanagement.dao;

import com.tus.incidentmanagement.entity.TimelineEventEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TimelineEventRepository extends CrudRepository<TimelineEventEntity, Long> {

    @Query("SELECT t FROM TimelineEventEntity t WHERE t.incident.id = :incidentId ORDER BY t.eventTime ASC")
    List<TimelineEventEntity> findTimelineByIncidentId(@Param("incidentId") Long incidentId);

}
