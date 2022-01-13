package org.management.SportEvent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private EventEnum eventName;
    private LocalDateTime eventDate;

    @ManyToMany(targetEntity = Athlete.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Athlete> athletes = new ArrayList<>();

}
