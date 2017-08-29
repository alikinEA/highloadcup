package highloadcup.resources.generic;

import highloadcup.models.common.Identificable;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Alikin E.A. on 26.08.17.
 */
public interface GenericResource<T extends Identificable> {

    ResponseEntity get(String string , HttpServletRequest request);

    ResponseEntity create(T entity, HttpServletRequest request);

    ResponseEntity change(Integer id ,String entity, HttpServletRequest request) throws IOException;

}
