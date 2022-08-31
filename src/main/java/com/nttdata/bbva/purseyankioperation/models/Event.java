package com.nttdata.bbva.purseyankioperation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private String id;
    private Date date;
    private EventOperation operation;
}
