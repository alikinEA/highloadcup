package highloadcup.services;

import highloadcup.models.Location;
import highloadcup.models.User;
import highloadcup.models.Visit;
import highloadcup.models.response.AverageResp;
import highloadcup.repository.UsersRepository;
import highloadcup.repository.VisitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;

/**
 * Created by Alikin E.A. on 26.08.17.
 */
@Service
public class LocationService {

    @Autowired
    InitService initService;

    @Autowired
    private VisitsRepository visitsRepository;

    @Autowired
    private UsersRepository usersRepository;

    public  double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public AverageResp average(Location location, Integer fromDate, Integer fromAge, Integer toDate, Integer toAge, String gender) {
        List<Visit> visits = visitsRepository.getVisitsByLocation(location.getId());
        if (visits.size() != 0) {
            OptionalDouble optionalDouble = visits
                    .parallelStream().filter(item -> {
                if (fromDate != null) {
                    if (!(item.getVisited_at() > fromDate)) {
                        return false;
                    }
                }
                if (toDate != null) {
                    if (!(toDate != null && item.getVisited_at() < toDate)) {
                        return false;
                    }
                }
                if (fromAge != null || toAge != null || gender != null) {
                    User user = usersRepository.getById(item.getUser());
                    Long old = user.getBirth_date()*1000;
                    if (fromAge != null) {
                        if (!(getTimeFromYear(fromAge) > old)) {
                            return false;
                        }
                    }
                    if (toAge != null) {
                        if (!(getTimeFromYear(toAge) < old)) {
                            return false;
                        }
                    }
                    if (gender != null) {
                        if (!gender.equals(user.getGender())) {
                            return false;
                        }
                    }
                }

                return true;
            })
                    .mapToInt(Visit::getMark).average();
            if (optionalDouble.isPresent()) {
                return new AverageResp(round(optionalDouble.getAsDouble(), 5));
            } else {
                return new AverageResp(0);
            }
        } else {
            return new AverageResp(0);
        }

    }

    private Long getTimeFromYear(Integer year) {
        Calendar cal= GregorianCalendar.getInstance();
        cal.setTimeInMillis(initService.getCurrentTimeStamp());
        cal.add(Calendar.YEAR, -year);
        return cal.getTimeInMillis();
    }

}
