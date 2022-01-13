package org.management.SportEvent.resource;

import org.management.SportEvent.dao.AthleteDao;
import org.management.SportEvent.dao.ChipDao;
import org.management.SportEvent.model.Athlete;
import org.management.SportEvent.model.Chip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;
import java.util.List;
import java.util.Optional;

@Path("/athlete")
public class AthleteResource {

    private final AthleteDao athleteDao;
    private final ChipDao chipDao;

    @Autowired
    public AthleteResource(AthleteDao athleteDao, ChipDao chipDao) {
        this.athleteDao = athleteDao;
        this.chipDao = chipDao;
    }

    @POST
    @Path("/save")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> save(Athlete athlete) {
        Optional<Athlete> alreadyAthlete = athleteDao.findByEmail(athlete.getEmail());
        if (alreadyAthlete.isPresent()) {
            return new ResponseEntity<>("There is already a user present with this email", HttpStatus.OK);
        } else {
            athlete.setChip(new Chip());
            athleteDao.save(athlete);
            return new ResponseEntity<>(athlete, HttpStatus.OK);
        }
    }

    @GET
    @Path("/list")
    @Consumes("application/json")
    @Produces("application/json")
    public List<Athlete> list() {
        return athleteDao.findAll();
    }

    @DELETE
    @Path("/delete/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> delete(@PathParam("id") Integer athleteId) {
        Optional<Athlete> athlete = athleteDao.findById(athleteId);
        if (athlete.isPresent()) {
            athleteDao.deleteById(athleteId);
            return new ResponseEntity<>("Athlete is Successfully deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("There is no athlete against this ID", HttpStatus.OK);
        }
    }

    @PUT
    @Path("/update")
    @Consumes("application/json")
    @Produces("application/json")
    public Athlete update(Athlete athlete) {
        return athleteDao.save(athlete);
    }

    @POST
    @Path("/attach-chip")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> attachChip(@QueryParam("athlete_id") Integer athleteId, @QueryParam("chip_id") Integer chipId) {
        Optional<Chip> chip = chipDao.findById(chipId);
        if (chip.isPresent()) {
            Optional<Athlete> athlete = athleteDao.findById(athleteId);
            if (athlete.isPresent()) {
                athlete.get().setChip(chip.get());
                return new ResponseEntity<>(athlete.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There is no athlete against this id", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("There is no chip against this id", HttpStatus.OK);
        }
    }
}
