package com.triyasoft.security;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;

public class ShiroTest {
public static void main(String[] args) {
	
	DefaultSecurityManager securityManager = new DefaultSecurityManager();

	SessionDAO sessionDAO = new CustomSessionDAO();
	
	((DefaultSessionManager)securityManager.getSessionManager()).setSessionDAO(sessionDAO);


}
}
