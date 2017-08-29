package highloadcup.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import highloadcup.models.User;
import highloadcup.models.Visit;
import highloadcup.models.response.Empty;
import highloadcup.repository.UsersRepository;
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
public class UsersResource implements GenericResource<User> {

    @Autowired
    UsersRepository repository;

    @Autowired
    VisitsRepository visitsRepository;

    @RequestMapping("users/{id}")
    public ResponseEntity get(@PathVariable(value = "id") String id, HttpServletRequest request) {
        if (!id.chars().allMatch(Character::isDigit)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        User user = repository.getById(Integer.valueOf(id));
        if (user != null && request.getRequestURI().replace("/users/","").chars().allMatch(Character::isDigit)) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("users/{id}/visits")
    public ResponseEntity visitUser(@PathVariable(value="id") Integer id
            ,HttpServletRequest request
            , @RequestParam(value = "fromDate",required = false) Integer fromDate
            , @RequestParam(value = "toDate",required = false) Integer toDate
            , @RequestParam(value = "country",required = false) String country
            , @RequestParam(value = "toDistance",required = false) Integer toDistance) {
        User user = repository.getById(id);
        if(!request.getRequestURI().contains("/users/") || !request.getRequestURI().contains("/visits")) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (user != null) {
            return new ResponseEntity<>(/*visitsRepository.getVisitsByUser(user.getId()
                    ,fromDate
                    ,toDate
                    ,country
                    ,toDistance
            ),*/ HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @RequestMapping(value = "users/new" , method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody User user, HttpServletRequest request) {
        if (!request.getRequestURI().equals("/users/new")) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (user.getId() == null
                || user.getBirth_date() == null
                || user.getEmail() == null
                || repository.getById(user.getId()) !=null
                || user.getFirst_name() == null
                || user.getGender() == null
                || user.getLast_name() == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        repository.put(user);
        return new ResponseEntity<>(new Empty(), HttpStatus.OK);
    }

    @RequestMapping(value = "users/{id}" , method = RequestMethod.POST)
    public ResponseEntity change(@PathVariable(value = "id") Integer id, @RequestBody String userStr, HttpServletRequest request) throws IOException {
        if (userStr.contains("null")) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User userOld = repository.getById(id);
        if (userOld == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        User user = new ObjectMapper().readValue(userStr,User.class);
        if (user.getLast_name() != null) {
            userOld.setLast_name(user.getLast_name());
        }
        if (user.getGender() != null) {
            userOld.setGender(user.getGender());
        }
        if (user.getFirst_name() != null) {
            userOld.setFirst_name(user.getFirst_name());
        }
        if (user.getBirth_date() != null) {
            userOld.setBirth_date(user.getBirth_date());
        }
        if (user.getEmail() != null) {
            userOld.setEmail(user.getEmail());
        }
        repository.put(userOld);
        return new ResponseEntity<>(new Empty(), HttpStatus.OK);
    }


}
