package ru.redsolution.bst.data.tables;

import android.database.Cursor;

/**
 * Интерфейс таблицы, которая возвращает список всех объектов.
 * 
 * @author alexander.ivanov
 * 
 */
public interface ListableTable {

	/**
	 * Возвращает список всем объетов.
	 * 
	 * @return
	 */
	Cursor list();

}
