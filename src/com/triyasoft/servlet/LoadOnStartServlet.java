package com.triyasoft.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.triyasoft.daos.AppSettingsDao;
import com.triyasoft.utils.CallcenterCallAllocationService;
import com.triyasoft.utils.ProjectUtils;

public class LoadOnStartServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {

		String contextPath = config.getServletContext().getContextPath();
		contextPath = contextPath.replaceAll("/", "");
		System.out.println("App Context is: " + contextPath);
		ProjectUtils.currentAppContext = contextPath;
		AppSettingsDao.loadAllAppSettings();
		// CallcenterCallAllocationService.loadInitdata();

	}
}
