package highloadcup.resources.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
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
        response.setHeader("Connection", "keep-alive");
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig arg0) throws ServletException {}
}
