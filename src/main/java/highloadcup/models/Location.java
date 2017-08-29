package highloadcup.models;


import highloadcup.models.common.Identificable;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Alikin E.A. on 24.08.17.
 */
@Getter
@Setter
public class Location extends Identificable {

    private Integer distance;
    private String city;
    private String place;
    private String country;

}
