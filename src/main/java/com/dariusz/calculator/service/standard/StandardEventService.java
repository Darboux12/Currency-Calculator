package com.dariusz.calculator.service.standard;

import com.dariusz.calculator.dal.entity.Event;
import com.dariusz.calculator.dal.repository.EventRepository;
import com.dariusz.calculator.service.EventService;
import org.springframework.stereotype.Service;

@Service
public class StandardEventService implements EventService {

    private final EventRepository eventRepository;

    public StandardEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Iterable<Event> findAllEvent() {

        return this.eventRepository.findAll();
    }

    @Override
    public void addEventLog(Event event) {
        this.eventRepository.save(event);
    }

}
