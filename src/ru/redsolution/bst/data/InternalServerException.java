package ru.redsolution.bst.data;

/**
 * Ошибка сервера с кодом 500.
 * 
 * @author alexander.ivanov
 * 
 */
public class InternalServerException extends Exception {

	private static final long serialVersionUID = 1L;

	public InternalServerException(String message) {
		super(message);
	}

}
