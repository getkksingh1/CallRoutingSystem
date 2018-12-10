package com.triyasoft.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.triyasoft.daos.UsersDao;
import com.triyasoft.model.UserModel;
import com.triyasoft.utils.Constans;
import com.triyasoft.utils.ProjectUtils;

@WebFilter("/RequestLoggingFilter")
public class SecurityFilter implements Filter {

	private ServletContext context;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		UserModel user = (UserModel) request.getSession().getAttribute("user");

		String path = ((HttpServletRequest) request).getRequestURI();
		if (path.contains("login"))
			chain.doFilter(req, res);
		else {
			if (user == null) {
				String uuid = getCookieValue(request,
						Constans.REMEMBER_ME_COOKIE_NAME);

				if (uuid != null) {
					user = UsersDao.find(uuid);

					if (user != null) {
						// request.login(user.getUserid() , user.getPassword());
						request.getSession().setAttribute("user", user); // Login.
						addCookie(response, Constans.REMEMBER_ME_COOKIE_NAME,
								uuid, Constans.REMEMBER_ME_COOKIE_TIME_OUT); // Extends
																				// age.
					} else {
						removeCookie(response, Constans.REMEMBER_ME_COOKIE_NAME);
					}
				}
			}

			if (user == null) {
				response.sendRedirect(ProjectUtils.getBaseURL(request)
						+ "/login.jsp");
			} else {
				chain.doFilter(req, res);
			}
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {

		this.context = fConfig.getServletContext();
		this.context.log("RequestLoggingFilter initialized");
	}

	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public static void addCookie(HttpServletResponse response, String name,
			String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	public static void removeCookie(HttpServletResponse response, String name) {
		addCookie(response, name, null, 0);
	}

}
