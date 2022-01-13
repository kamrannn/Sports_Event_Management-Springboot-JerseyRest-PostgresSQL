package org.management.SportEvent.resource;

import org.management.SportEvent.dao.AthleteDao;
import org.management.SportEvent.dao.EventDao;
import org.management.SportEvent.dao.ResultDao;
import org.management.SportEvent.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * The type Event resource.
 */
@Path("/event")
public class EventResource {
    private final EventDao eventDao;
    private final AthleteDao athleteDao;
    private final ResultDao resultDao;

    /**
     * Instantiates a new Event resource.
     *
     * @param eventDao   the event dao
     * @param athleteDao the athlete dao
     * @param resultDao
     */
    @Autowired
    public EventResource(EventDao eventDao, AthleteDao athleteDao, ResultDao resultDao) {
        this.eventDao = eventDao;
        this.athleteDao = athleteDao;
        this.resultDao = resultDao;
    }

    /**
     * Save event.
     *
     * @param event the event
     * @return the event
     */
    @POST
    @Path("/save")
    @Consumes("application/json")
    @Produces("application/json")
    public Event save(Event event) {
        if ("MARATHON".equalsIgnoreCase(event.getEventName() + "")) {
            event.setEventName(EventEnum.MARATHON);
        } else if ("SPRINT".equalsIgnoreCase(event.getEventName() + "")) {
            event.setEventName(EventEnum.SPRINT);
        } else {
            event.setEventName(EventEnum.TRAILS);
        }
        return eventDao.save(event);
    }

    /**
     * List down all the events.
     *
     * @return the response entity
     */
    @GET
    @Path("/list")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> list() {
        try {
            List<Event> eventList = eventDao.findAll();
            if (eventList.isEmpty()) {
                return new ResponseEntity<>("There is no event in the database", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(eventList, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets event by name.
     *
     * @param eventName the event name
     * @return the event by name
     */
    @GET
    @Path("/getByName")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> getEventByName(@QueryParam("name") EventEnum eventName) {
        try {
            Optional<Event> event = eventDao.findByEventName(eventName);
            if (event.isPresent()) {
                return new ResponseEntity<>(event, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There is no event against this name: " + eventName, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete the event.
     *
     * @param eventId the event id
     * @return the response entity
     */
    @DELETE
    @Path("/delete/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> delete(@PathParam("id") Integer eventId) {
        Optional<Event> event = eventDao.findById(eventId);
        if (event.isPresent()) {
            eventDao.deleteById(eventId);
            return new ResponseEntity<>(event.get().getEventName() + " Event is Successfully deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("There is no event against this Event ID", HttpStatus.OK);
        }
    }

    /**
     * Update event.
     *
     * @param event the event
     * @return the event
     */
    @PUT
    @Path("/update")
    @Consumes("application/json")
    @Produces("application/json")
    public Event update(Event event) {
        return eventDao.save(event);
    }

    /**
     * Add athlete in an event.
     *
     * @param eventId   the event id
     * @param athleteId the athlete id
     * @return the response entity
     */
    @POST
    @Path("/add-athlete")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> addAthleteInEvent(@QueryParam("eventId") Integer eventId, @QueryParam("athleteId") Integer athleteId) {
        Optional<Event> event = eventDao.findById(eventId);
        if (event.isPresent()) {
            Optional<Athlete> athlete = athleteDao.findById(athleteId);
            if (athlete.isPresent()) {
                List<Athlete> athleteList = event.get().getAthletes();
                athleteList.add(athlete.get());
                eventDao.save(event.get());
                return new ResponseEntity<>(athlete.get().getFirstName() + " is successfully added in the event " + event.get().getEventName(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There is no athlete against this id", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("There is no event against this id", HttpStatus.OK);
        }
    }

    /**
     * Add athlete in an event.
     *
     * @param eventId the event id
     * @return the response entity
     */
    @POST
    @Path("/start-event")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> startEvent(@QueryParam("eventId") Integer eventId) {
        Optional<Event> event = eventDao.findById(eventId);
        if (event.isPresent()) {
            List<Athlete> athleteList = event.get().getAthletes();
            if (athleteList.isEmpty()) {
                return new ResponseEntity<>("There is no athlete registered for this event", HttpStatus.OK);
            } else {
                for (Athlete athlete : athleteList
                ) {
                    if (null != athlete.getChip()) {
                        athlete.getChip().setStartTime(LocalTime.now());
                        athlete.getChip().setLocation(LocationEnum.start);
                    } else {
                        return new ResponseEntity<>("There are some athltes ho don't have any chips yet", HttpStatus.OK);
                    }
                }
                eventDao.save(event.get());
                return new ResponseEntity<>("The event " + event.get().getEventName() + " has been started and starting time has been set", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("There is no event against this id", HttpStatus.OK);
        }
    }

    @POST
    @Path("/update-athlete-location")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> updateLocationOfAthlete(@QueryParam("athlete_id") Integer athleteId, @QueryParam("current_location") LocationEnum currentLocation) {
        Optional<Athlete> athlete = athleteDao.findById(athleteId);
        if (athlete.isPresent()) {
            if ("finish".equalsIgnoreCase(currentLocation.toString())) {
                athlete.get().getChip().setLocation(LocationEnum.finish);
                athlete.get().getChip().setEndTime(LocalTime.now());
                athlete.get().getChip()
                        .setTotalTime(athlete.get().getChip().getEndTime()
                                .minusHours(athlete.get().getChip().getStartTime().getHour())
                                .minusMinutes(athlete.get().getChip().getStartTime().getMinute()));
                athleteDao.save(athlete.get());
                return new ResponseEntity<>("Athlete: " + athlete.get().getFirstName() + " has finished the race with total time: " + athlete.get().getChip().getTotalTime(), HttpStatus.OK);
            } else {
                athlete.get().getChip().setLocation(currentLocation);
                athleteDao.save(athlete.get());
                return new ResponseEntity<>("Athlete: " + athlete.get().getFirstName() + " current location is: " + currentLocation, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("There is no athlete against this Id: ", HttpStatus.OK);

        }
    }

    @POST
    @Path("/end-event")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> endEvent(@QueryParam("eventId") Integer eventId) {
        Optional<Event> event = eventDao.findById(eventId);
        if (event.isPresent()) {
            List<Athlete> athleteList = event.get().getAthletes();
            if (athleteList.isEmpty()) {
                return new ResponseEntity<>("There is no athlete registered for this event", HttpStatus.OK);
            } else {
                for (Athlete athlete : athleteList
                ) {
                    if (null != athlete.getChip()) {
                        Result result = new Result();
                        result.setEventName(event.get().getEventName());
                        result.setTotalTime(athlete.getChip().getTotalTime());
                        result.setPlayerName(athlete.getFirstName());
                        result.setEventID(event.get().getId());
                        resultDao.save(result);
                    } else {
                        return new ResponseEntity<>("There are some athletes who don't have any chips yet", HttpStatus.OK);
                    }
                }
                return new ResponseEntity<>("The event " + event.get().getEventName() + " has been ended and see the results from result endpoint", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("There is no event against this id", HttpStatus.OK);
        }
    }
}
