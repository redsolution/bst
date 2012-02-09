package ru.redsolution.bst.data.tables;

import java.util.ArrayList;

import ru.redsolution.bst.data.BST;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Помошник для работы с базой данных.
 * 
 * @author alexander.ivanov
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "testF.db";
	private static final int DATABASE_VERSION = 1;

	private static final DatabaseHelper instance;

	static {
		instance = new DatabaseHelper(BST.getInstance());
	}

	public static DatabaseHelper getInstance() {
		return instance;
	}

	private final ArrayList<DatabaseTable> tables;

	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		tables = new ArrayList<DatabaseTable>();
	}

	public void addTable(DatabaseTable table) {
		tables.add(table);
	}

	@Override
	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		for (DatabaseTable table : tables)
			table.create(paramSQLiteDatabase);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion > newVersion) {
			throw new IllegalStateException();
		} else {
			while (oldVersion < newVersion) {
				oldVersion += 1;
				for (DatabaseTable table : tables)
					table.migrate(db, oldVersion);
			}
		}
	}

	/**
	 * Удалить все данные.
	 */
	public void clear() {
		for (DatabaseTable table : tables)
			table.clear();
	}
}
