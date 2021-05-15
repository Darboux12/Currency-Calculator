package com.dariusz.calculator.controller.standard;

import com.dariusz.calculator.controller.EventController;
import com.dariusz.calculator.dal.entity.Event;
import com.dariusz.calculator.dto.response.EventResponse;
import com.dariusz.calculator.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StandardEventController implements EventController {

    private final EventService eventService;

    public StandardEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public ResponseEntity<Iterable<EventResponse>> findAllEvent() {
        return ResponseEntity.ok(this.convertEventToEventResponseIterable(this.eventService.findAllEvent()));
    }

    private EventResponse convertEventToEventResponse(Event event){

        return new EventResponse(
                event.getId(),
                event.getDate(),
                event.getLocalization(),
                event.getDescription()
        );
    }

    private Iterable<EventResponse> convertEventToEventResponseIterable(Iterable<Event> events){

        List<EventResponse> responseList = new ArrayList<>();

        events.forEach(event -> responseList.add(this.convertEventToEventResponse(event)));

        return responseList;

    }

}
