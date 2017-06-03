package org.daijie.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;

public interface RedisSessionFactory {

	public Session getSession(Serializable sessionId);
}
