package com.dariusz.calculator.dal.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    private UUID id;
    private Date date;
    private String localization;
    private String description;

    public Event(String localization, String description) {
        this.date = new Date();
        this.localization = localization;
        this.description = description;
    }
}
