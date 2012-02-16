package ru.redsolution.bst.data;

/**
 * Сервер сообщает об ошибки с кодом 500.
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
