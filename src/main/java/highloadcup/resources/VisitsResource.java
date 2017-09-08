package highloadcup.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import highloadcup.models.Visit;
import highloadcup.models.response.Empty;
import highloadcup.repository.VisitsRepository;
import highloadcup.resources.generic.GenericResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Alikin E.A. on 24.08.17.
 */
@RestController
public class VisitsResource implements GenericResource<Visit> {

    @Autowired
    VisitsRepository repository;

    @RequestMapping("visits/{id}")
    public ResponseEntity get(@PathVariable(value="id") String id ,HttpServletRequest request) {
        Visit visit = repository.getById(Integer.valueOf(id));
        if (visit != null) {
            return new ResponseEntity<>(visit, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "visits/new" , method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Visit visit , HttpServletRequest request) {
        if (!request.getRequestURI().equals("/visits/new")) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (visit.getId() == null
                || visit.getUser() == null
                || visit.getLocation() == null
                || visit.getMark() == null
                || visit.getVisited_at() == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            if (repository.getById(visit.getId()) !=null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                repository.put(visit);
                return new ResponseEntity<>(new Empty(), HttpStatus.OK);
            }
        }
    }

    @RequestMapping(value = "visits/{id}" , method = RequestMethod.POST)
    public ResponseEntity change(@PathVariable(value="id") Integer id , @RequestBody String visitStr , HttpServletRequest request) throws IOException {
        if (visitStr.contains("null")) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Visit visitOld = repository.getById(id);
        if (visitOld == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Visit visit = new ObjectMapper().readValue(visitStr,Visit.class);
        if (visit.getLocation() != null) {
            visitOld.setLocation(visit.getLocation());
        }
        if (visit.getVisited_at() != null) {
            visitOld.setVisited_at(visit.getVisited_at());
        }
        if (visit.getMark() != null) {
            visitOld.setMark(visit.getMark());
        }
        if (visit.getUser() != null) {
            visitOld.setUser(visit.getUser());
        }
        repository.put(visitOld);
        return new ResponseEntity<>(new Empty(), HttpStatus.OK);
    }

}
