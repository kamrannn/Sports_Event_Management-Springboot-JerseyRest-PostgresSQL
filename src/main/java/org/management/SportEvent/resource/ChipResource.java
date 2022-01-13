package org.management.SportEvent.resource;

import org.management.SportEvent.dao.ChipDao;
import org.management.SportEvent.model.Chip;
import org.management.SportEvent.model.LocationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;
import java.util.List;

@Path("/chip")
public class ChipResource {
    private final ChipDao chipDao;

    @Autowired
    public ChipResource(ChipDao chipDao) {
        this.chipDao = chipDao;
    }

    @POST
    @Path("/save")
    @Consumes("application/json")
    @Produces("application/json")
    public Chip save(Chip chip) {
        chip.setLocation(LocationEnum.start);
        return chipDao.save(chip);
    }

    @GET
    @Path("/list")
    @Consumes("application/json")
    @Produces("application/json")
    public List<Chip> list() {
        return chipDao.findAll();
    }

    @DELETE
    @Path("/delete/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> delete(@PathParam("id") Integer chipId) {
        chipDao.deleteById(chipId);
        return new ResponseEntity<>("Chip is Successfully deleted", HttpStatus.OK);
    }

    @PUT
    @Path("/update")
    @Consumes("application/json")
    @Produces("application/json")
    public Chip update(Chip chip) {
        return chipDao.save(chip);
    }
}
