package com.triyasoft.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.triyasoft.daos.UsersDao;
import com.triyasoft.model.UserModel;
import com.triyasoft.utils.ProjectUtils;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/GenericLoginServlet")
public class GenericLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Map<String, String> sessionIdMap = new HashMap<String, String>();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// get request parameters for userID and password
		String userId = request.getParameter("user");
		String pwd = request.getParameter("pwd");

		UserModel userModel = UsersDao.fetchUser(userId);

		String baseURL = ProjectUtils.getBaseURL(request);

		if (userModel.getPassword() != null
				&& userModel.getPassword().equals(pwd)) {
			Cookie loginCookie = new Cookie("user", userId);
			// setting cookie to expiry in 10 years

			loginCookie.setMaxAge(10 * 365 * 24 * 60 * 60);
			Cookie[] cookies = request.getCookies();
			String jssessionid = getJessionID(cookies);
			if (jssessionid != null) {
				UsersDao.updateJessionCookie(userId, jssessionid);
				sessionIdMap.put(userId, jssessionid);
			}

			userModel.setIs_logged_in(true);
			request.getSession().setAttribute("user", userModel);

			response.addCookie(loginCookie);
			response.sendRedirect(baseURL + "/dashboard.jsp");

		} else {

			response.sendRedirect(baseURL + "/login.jsp");
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
