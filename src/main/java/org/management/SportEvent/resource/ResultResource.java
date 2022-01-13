package org.management.SportEvent.resource;

import org.management.SportEvent.dao.EventDao;
import org.management.SportEvent.dao.ResultDao;
import org.management.SportEvent.model.Event;
import org.management.SportEvent.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.*;
import java.util.List;
import java.util.Optional;

@Path("/result")
public class ResultResource {
    private final ResultDao resultDao;
    private final EventDao eventDao;

    @Autowired
    public ResultResource(ResultDao resultDao, EventDao eventDao) {
        this.resultDao = resultDao;
        this.eventDao = eventDao;
    }

    @GET
    @Path("/list")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> list() {
        try {
            List<Result> resultList = resultDao.findAllByOrderByTotalTimeAsc();
            if (resultList.isEmpty()) {
                return new ResponseEntity<>("There is no result in the database", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(resultList, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/by-event-id")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> getByEventId(@QueryParam("event_id") Integer eventId) {
        try {
            Optional<Event> event = eventDao.findById(eventId);
            if (event.isPresent()) {
                List<Result> resultList = resultDao.findResultsByEventIDOrderByTotalTimeAsc(eventId);

                if (resultList.isEmpty()) {
                    return new ResponseEntity<>("There is no result in the database", HttpStatus.OK);
                } else {
                    for (int i = 0; i < resultList.size(); i++) {
                        resultList.get(i).setPosition(i + 1);
                        resultDao.save(resultList.get(i));
                    }
                    return new ResponseEntity<>(resultList, HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("There is no event against this ID in the database", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/delete/id")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> deleteResultById(@QueryParam("result_id") Integer resultId) {
        try {
            Optional<Result> result = resultDao.findById(resultId);
            if (result.isPresent()) {
                resultDao.delete(result.get());
                return new ResponseEntity<>("Result is successfully deleted from the database", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There is no result against this ID in the database", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    @Path("/update")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<Object> updateResult(Result result) {
        try {
            Optional<Result> optionalResult = resultDao.findById(result.getId());
            if (optionalResult.isPresent()) {
                resultDao.save(result);
                return new ResponseEntity<>("Result is successfully updated in the database", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There is no result against this ID in the database", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
