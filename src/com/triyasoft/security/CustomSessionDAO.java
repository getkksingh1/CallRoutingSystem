package com.triyasoft.security;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.SessionDAO;

public class CustomSessionDAO implements SessionDAO {

	@Override
	public Serializable create(Session arg0) {

		return null;
	}

	@Override
	public void delete(Session arg0) {
		
		
	}

	@Override
	public Collection<Session> getActiveSessions() {

		return null;
	}

	@Override
	public Session readSession(Serializable arg0)
			throws UnknownSessionException {
		return null;
	}

	@Override
	public void update(Session arg0) throws UnknownSessionException {

		
	}

}
