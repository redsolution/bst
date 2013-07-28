package ru.redsolution.bst.data;

/**
 * Слушатель изменения состояния импорта.
 * 
 * @author alexander.ivanov
 * 
 */
public interface ImportOperationListener extends OperationListener {

	/**
	 * Обновлен прогресс выполнения.
	 */
	void onProgressUpdate(ImportProgress progress);

}
