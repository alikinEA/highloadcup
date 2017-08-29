package highloadcup.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Alikin E.A. on 26.08.17.
 */
@AllArgsConstructor
@Getter
@Setter
public class VisitResp {
    private Integer mark;
    private Integer visited_at;
    private String place;

}
