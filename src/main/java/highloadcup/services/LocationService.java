package highloadcup.services;

import highloadcup.models.Location;
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
                    .stream().filter(item -> {
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
                if (fromAge != null) {
                   // System.out.println("param = " +new Date(getTimeFromYear(fromAge)) + " birth = " + new Date(usersRepository.getById(item.getUser()).getBirth_date()) + "isNeed= " + (new Date(getTimeFromYear(fromAge)).after(new Date(usersRepository.getById(item.getUser()).getBirth_date()))) );
                    if (!(getTimeFromYear(fromAge) > (usersRepository.getById(item.getUser()).getBirth_date())*1000)) {
                        return false;
                    }
                }
                if (toAge != null) {

                    //System.out.println("param = " +new Date(getTimeFromYear(toAge)) + " birth = " + new Date(usersRepository.getById(item.getUser()).getBirth_date() * 1000));
                    if (!(getTimeFromYear(toAge) < (usersRepository.getById(item.getUser()).getBirth_date()*1000))) {
                        return false;
                    }
                }
                if (gender != null) {
                    if (!gender.equals(usersRepository.getById(item.getUser()).getGender())) {
                        return false;
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
    private Integer getYear(Long bithday) {

        LocalDate birthdate =
                Instant.ofEpochMilli(bithday).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now =
                Instant.ofEpochMilli(new Date().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        Period period =Period.between(birthdate, now);
        return period.getYears();
    }

    private Long getTimeFromYear(Integer year) {
        Calendar cal= GregorianCalendar.getInstance();
        cal.setTimeInMillis(initService.getCurrentTimeStamp());
        /*cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);*/
        cal.add(Calendar.YEAR, -year);
        return cal.getTimeInMillis();
    }

    private Integer calc(long userBirth) {
        userBirth = userBirth * 1000;
        Integer age = 0;
        Long curr = initService.getCurrentTimeStamp();
        while (curr > userBirth) {
            curr = curr - 31536000000l;
            age++;
        }
        return age;
    }
}
