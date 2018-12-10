package com.triyasoft.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.triyasoft.daos.UsersDao;
import com.triyasoft.model.UserModel;
import com.triyasoft.utils.Constans;
import com.triyasoft.utils.ProjectUtils;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		Cookie loginCookie = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(Constans.REMEMBER_ME_COOKIE_NAME)) {
					loginCookie = cookie;

					break;
				}
			}
		}

		UserModel user = (UserModel) request.getSession().getAttribute("user");
		UsersDao.deleteSession(user);

		if (loginCookie != null) {
			loginCookie.setMaxAge(0);
			response.addCookie(loginCookie);
		}
		request.getSession().removeAttribute("user");
		String baseURL = ProjectUtils.getBaseURL(request);

		response.sendRedirect(baseURL + "/login.jsp");
	}

}