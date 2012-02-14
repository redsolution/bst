package ru.redsolution.bst.data.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Интерфейс для таблиц базы данных.
 * 
 * @author alexander.ivanov
 * 
 */
public interface DatabaseTable {
	/**
	 * Вызывается при создании базы данных.
	 * 
	 * @param db
	 */
	void create(SQLiteDatabase db);

	/**
	 * Вызывается при миграции базы данных.
	 * 
	 * @param db
	 * @param toVersion
	 */
	void migrate(SQLiteDatabase db, int toVersion);

	/**
	 * Вызывается перед синхронизацией базы данных.
	 */
	void clear();
}
