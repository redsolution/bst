package ru.redsolution.bst.data;

/**
 * Слушатель изменения состояния операции.
 * 
 * @author alexander.ivanov
 * 
 */
public interface OperationListener {

	/**
	 * Операция запущена.
	 */
	void onBegin();

	/**
	 * Операция успешно завершена.
	 */
	void onDone();

	/**
	 * Операция была отменена.
	 */
	void onCancelled();

	/**
	 * В процессе выполнения операция произошла ошибка.
	 * 
	 * @param exception
	 */
	void onError(RuntimeException exception);
}
