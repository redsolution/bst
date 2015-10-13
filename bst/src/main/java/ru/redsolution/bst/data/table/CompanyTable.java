/**
 * Copyright (c) 2013, Redsolution LTD. All rights reserved.
 *
 * This file is part of Barcode Scanner Terminal project;
 * you can redistribute it and/or modify it under the terms of
 *
 * Barcode Scanner Terminal is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package ru.redsolution.bst.data.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Список организаций.
 * 
 * @author alexander.ivanov
 * 
 */
public class CompanyTable extends NamedTable {

	private static final String NAME = "company";

	private final static CompanyTable instance;

	static {
		instance = new CompanyTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static CompanyTable getInstance() {
		return instance;
	}

	private CompanyTable() {
	}

	@Override
	protected String getTableName() {
		return NAME;
	}

	@Override
	public void migrate(SQLiteDatabase db, int toVersion) {
		super.migrate(db, toVersion);
		String sql;
		switch (toVersion) {
		case 3:
			sql = "ALTER TABLE company RENAME TO company_;";
			DatabaseHelper.execSQL(db, sql);
			sql = "CREATE TABLE company (" + "_id TEXT,"
					+ "name TEXT COLLATE UNICODE);";
			DatabaseHelper.execSQL(db, sql);
			sql = "INSERT INTO company (_id, name) "
					+ "SELECT _id, name FROM company_;";
			DatabaseHelper.execSQL(db, sql);
			sql = "DROP TABLE company_;";
			DatabaseHelper.execSQL(db, sql);
			break;
		default:
			break;
		}
	}

}
