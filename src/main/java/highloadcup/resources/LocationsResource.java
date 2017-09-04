package highloadcup.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import highloadcup.models.Location;
import highloadcup.models.User;
import highloadcup.models.Visit;
import highloadcup.models.response.Empty;
import highloadcup.repository.LocationsRepository;
import highloadcup.resources.generic.GenericResource;
import highloadcup.services.LocationService;
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
public class LocationsResource implements GenericResource<Location>{

    @Autowired
    LocationsRepository repository;

    @Autowired
    LocationService locationService;

    @RequestMapping("locations/{id}")
    public ResponseEntity get(@PathVariable(value="id") String id ,HttpServletRequest request) {
        if (!id.chars().allMatch(Character::isDigit)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Location location = repository.getById(Integer.valueOf(id));
        if (location != null && request.getRequestURI().replace("/locations/","").chars().allMatch(Character::isDigit)) {
            return new ResponseEntity<>(location, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @RequestMapping(value = "locations/new" , method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Location location , HttpServletRequest request) {
        if (!request.getRequestURI().equals("/locations/new")) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (location.getId() == null
                || location.getCity() == null
                || location.getCountry() == null
                || repository.getById(location.getId()) !=null
                || location.getDistance() == null
                || location.getPlace() == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        repository.put(location);
        return new ResponseEntity<>(new Empty(), HttpStatus.OK);
    }

    @RequestMapping(value = "locations/{id}" , method = RequestMethod.POST)
    public ResponseEntity change(@PathVariable(value="id") Integer id,@RequestBody String locationStr,HttpServletRequest request) throws IOException {
        if (locationStr.contains("null")) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Location locationOld = repository.getById(id);
        if (locationOld == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Location location = new ObjectMapper().readValue(locationStr,Location.class);
        if (location.getCity() != null) {
            locationOld.setCity(location.getCity());
        }
        if (location.getDistance() != null) {
            locationOld.setDistance(location.getDistance());
        }
        if (location.getPlace() != null) {
            locationOld.setPlace(location.getPlace());
        }
        if (location.getCountry() != null) {
            locationOld.setCountry(location.getCountry());
        }
        repository.put(locationOld);
        return new ResponseEntity<>(new Empty(), HttpStatus.OK);
    }

    @RequestMapping("locations/{id}/avg")
    public ResponseEntity average(@PathVariable(value="id") Integer id
            , @RequestParam(value = "fromDate",required = false) Integer fromDate
            , @RequestParam(value = "toDate",required = false) Integer toDate
            , @RequestParam(value = "fromAge",required = false) Integer fromAge
            , @RequestParam(value = "toAge",required = false) Integer toAge
            , @RequestParam(value = "gender",required = false) String gender
            ,HttpServletRequest request) {
        if (gender != null && gender.length() > 1) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (!request.getRequestURI().contains("/locations/")
                || !request.getRequestURI().contains("/avg")) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Location location = repository.getById(id);
        if (location != null) {
            return new ResponseEntity<>(locationService.average(location,fromDate,fromAge,toDate,toAge,gender), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
