package org.management.SportEvent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chip {
    @Id
    private String id = UUID.randomUUID().toString();
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime totalTime;
    private LocationEnum location;
}
