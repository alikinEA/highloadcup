package highloadcup.repository;

import highloadcup.models.Location;
import highloadcup.models.Visit;
import highloadcup.models.response.VisitResp;
import highloadcup.models.response.VisitsResp;
import highloadcup.repository.generic.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Alikin E.A. on 24.08.17.
 */
@Repository
public class VisitsRepository extends GenericRepository<Visit>{

    @Autowired
    LocationsRepository locationsRepository;

    public VisitsResp getVisitsByUser(Integer id, Integer fromDate, Integer toDate, String country, Integer toDistance) {
        VisitsResp visitsResp = new VisitsResp();
        List<VisitResp> visitRespList = new ArrayList<>();
        map.forEach((k,v) -> {
            boolean isValid = true;
            if (fromDate != null ) {
                if (!(v.getVisited_at() > fromDate))
                    isValid =  false;
            }
            if (toDate != null ) {
                if (!(v.getVisited_at() < toDate))
                    isValid =  false;
            }
            Location location = locationsRepository.getById(v.getLocation());
            if (country != null ) {
                if (!(country.equals(location.getCountry())))
                    isValid =  false;
            }
            if (toDistance != null ) {
                if (!(toDistance > location.getDistance())) {
                    isValid =  false;
                }
            }
            if (v.getUser().equals(id) && isValid) {
                visitRespList.add(new VisitResp(v.getMark(),v.getVisited_at(),location.getPlace()));
            }
        });
        visitRespList.sort(Comparator.comparingInt(VisitResp::getVisited_at));
        visitsResp.setVisits(visitRespList);
        return visitsResp;
    }

    public List<Visit> getVisitsByLocation(Integer id) {
        List<Visit> list = new ArrayList<>();
        map.forEach((k,v) -> {
            if (v.getLocation().equals(id)) {
                list.add(v);
            }
        });
        return list;
    }
}
