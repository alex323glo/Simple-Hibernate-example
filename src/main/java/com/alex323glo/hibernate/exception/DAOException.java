package com.alex323glo.hibernate.exception;

/**
 * DAO operations Exception class.
 * Could be thrown in Runtime.
 * Is caused by some unexpected problems with DAO operations.
 *
 * @author alex323glo
 * @version 1.0
 *
 * @see RuntimeException
 */
public class DAOException extends RuntimeException {

    public DAOException(String message) {
        super(message);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
