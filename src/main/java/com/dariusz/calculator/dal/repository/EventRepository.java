package com.dariusz.calculator.dal.repository;

import com.dariusz.calculator.dal.entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends CrudRepository<Event, UUID> {
}