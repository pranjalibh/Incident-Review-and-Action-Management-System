package com.tus.incidentmanagement.repository;

import com.tus.incidentmanagement.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

}