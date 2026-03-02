package com.tus.incidentmanagement.repository;

import com.tus.incidentmanagement.entity.IncidentEntity;
import org.springframework.data.repository.CrudRepository;

public interface IncidentRepository extends CrudRepository<IncidentEntity,Long> {
}
