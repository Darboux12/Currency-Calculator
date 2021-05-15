package com.dariusz.calculator.service;

import com.dariusz.calculator.dal.entity.Event;

public interface EventService {

    Iterable<Event> findAllEvent();

    void addEventLog(Event event);

}
