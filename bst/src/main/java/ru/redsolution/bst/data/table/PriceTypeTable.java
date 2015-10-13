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
