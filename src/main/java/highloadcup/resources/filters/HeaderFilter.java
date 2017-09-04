package highloadcup.resources.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Alikin E.A. on 28.08.17.
 */
@Component
public class HeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        if (request.getHeader("Connection").toLowerCase().equals("keep-alive")
                || request.getHeader("connection").toLowerCase().equals("keep-alive")) {
            response.setHeader("Connection", "Keep-Alive");
        }
        if (request.getHeader("Connection").toLowerCase().equals("close")
                || request.getHeader("connection").toLowerCase().equals("close")) {
            response.setHeader("Connection", "Close");
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig arg0) throws ServletException {}
}
