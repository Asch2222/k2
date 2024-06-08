package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class SecurityHeadersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("SecurityHeadersFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("SecurityHeadersFilter doFilter method called");

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Aggiungi header X-Frame-Options
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // Aggiungi header Content-Security-Policy
        httpResponse.setHeader("Content-Security-Policy", "frame-ancestors 'none'");

        // Continua con il resto della catena dei filtri
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("SecurityHeadersFilter destroyed");
    }
}
