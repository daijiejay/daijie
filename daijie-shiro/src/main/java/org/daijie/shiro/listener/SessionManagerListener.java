package org.daijie.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.daijie.shiro.session.RedisSession;

public abstract class SessionManagerListener implements SessionListener {

    private RedisSession redisSession;

    @Override
    public void onStart(Session session) {
        System.out.println("on start");
    }

    @Override
    public void onStop(Session session) {
        System.out.println("on stop");
    }

    @Override
    public void onExpiration(Session session) {
    	redisSession.delete(session);
    }

}
