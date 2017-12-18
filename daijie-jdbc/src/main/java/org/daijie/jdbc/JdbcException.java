package org.daijie.jdbc;

public class JdbcException extends RuntimeException {

	private static final long serialVersionUID = 2496912709305774038L;
	
	public JdbcException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }
    
    public JdbcException(final String message, final Exception cause) {
        super(message, cause);
    }
    
    public JdbcException(final Exception cause) {
        super(cause);
    }

}
