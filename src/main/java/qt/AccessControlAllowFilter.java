package qt;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessControlAllowFilter implements Filter {

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// LOGGER.debug("applying accessControlAllow filter");
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		HttpServletResponse httpResponse = (HttpServletResponse) response;

		httpResponse.addHeader("Access-Control-Allow-Origin", "*");
		httpResponse.addHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");

		// httpResponse.addHeader("Access-Control-Allow-Credentials", "true");

		httpResponse.setHeader("Access-Control-Expose-Headers",
				"Origin,X-Requested-With,Content-Type,Accept,JMS-TOKEN");
		httpResponse.addHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept,JMS-TOKEN");

		chain.doFilter(httpRequest, httpResponse);

	}

	@Override
	public void destroy() {
	}
}