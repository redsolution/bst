package ru.redsolution.bst.data.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Список единиц изменения.
 * 
 * @author alexander.ivanov
 * 
 */
public class PriceTypeTable extends NamedTable {

	private static final String NAME = "price_type";

	private final static PriceTypeTable instance;

	static {
		instance = new PriceTypeTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static PriceTypeTable getInstance() {
		return instance;
	}

	private PriceTypeTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

	@Override
	public void migrate(SQLiteDatabase db, int toVersion) {
		super.migrate(db, toVersion);
		String sql;
		switch (toVersion) {
		case 5:
			sql = "CREATE TABLE price_type (" + "_id TEXT,"
					+ "name TEXT COLLATE UNICODE);";
			DatabaseHelper.execSQL(db, sql);
			break;
		default:
			break;
		}
	}

}
