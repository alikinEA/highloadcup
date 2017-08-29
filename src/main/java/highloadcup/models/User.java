package highloadcup.models;

import highloadcup.models.common.Identificable;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Alikin E.A. on 24.08.17.
 */
@Getter
@Setter
public class User extends Identificable {

    private String first_name;
    private String last_name;
    private String gender;
    private String email;
    private Long birth_date;

}
