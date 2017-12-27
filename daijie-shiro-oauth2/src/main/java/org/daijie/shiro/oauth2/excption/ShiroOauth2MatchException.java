package org.daijie.shiro.oauth2.excption;

@SuppressWarnings("serial")
public class ShiroOauth2MatchException extends RuntimeException {

	public ShiroOauth2MatchException(){
		super();
	}
	
	public ShiroOauth2MatchException(String msg){
		super(msg);
	}
	
	public ShiroOauth2MatchException(String msg, Exception e){
		super(msg, e);
	}
}
