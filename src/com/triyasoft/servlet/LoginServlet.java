package com.triyasoft.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.triyasoft.daos.UsersDao;
import com.triyasoft.model.UserModel;
import com.triyasoft.utils.Constans;
import com.triyasoft.utils.ProjectUtils;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Map<String, String> sessionIdMap = new HashMap<String, String>();

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

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("user");
		String password = (request.getParameter("pwd"));
		boolean remember = "true".equals(request.getParameter("remember"));
		if (ProjectUtils.currentAppContext == null)
			ProjectUtils.currentAppContext = request.getContextPath();

		UserModel user = UsersDao.find(username, password);

		if (user != null && user.getUserid() != null) {

			user.setIs_logged_in(true);

			// request.login(user.getUserid(), user.getPassword()); // Password
			// should already be the hashed variant.
			request.getSession().setAttribute("user", user);

			if (remember) {
				String uuid = UUID.randomUUID().toString();
				UsersDao.save(uuid, user);
				addCookie(response, Constans.REMEMBER_ME_COOKIE_NAME, uuid,
						Constans.REMEMBER_ME_COOKIE_TIME_OUT);
			} else {
				UsersDao.deleteSession(user);
				removeCookie(response, Constans.REMEMBER_ME_COOKIE_NAME);
			}
			response.sendRedirect(ProjectUtils.getBaseURL(request)
					+ "/dashboard.jsp");

		}

		else {

			response.sendRedirect(ProjectUtils.getBaseURL(request)
					+ "/login.jsp");
			// RequestDispatcher rd =
			// getServletContext().getRequestDispatcher(baseURL+"/login.jsp");

		}

	}

	private String getJessionID(Cookie[] cookies) {

		for (Cookie cookie : cookies) {
			if ("JSESSIONID".equals(cookie.getName()))
				return cookie.getValue();
		}
		return null;
	}

}
