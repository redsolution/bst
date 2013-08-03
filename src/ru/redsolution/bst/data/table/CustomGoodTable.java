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

import android.content.ContentValues;

/**
 * Список пользовательских товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class CustomGoodTable extends BaseGoodTable {

	private static final String NAME = "custom_good";

	private final static CustomGoodTable instance;

	static {
		instance = new CustomGoodTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static CustomGoodTable getInstance() {
		return instance;
	}

	private CustomGoodTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

	@Override
	protected String getFieldType(String name) {
		if (Fields._ID.equals(name))
			return "INTEGER PRIMARY KEY";
		return super.getFieldType(name);
	}

	/**
	 * Добавить объект.
	 * 
	 * @param name
	 * @return
	 */
	public long add(String name) {
		ContentValues values = new ContentValues();
		values.put(Fields.NAME, name);
		values.put(Fields.BUY_PRICE, "");
		values.put(Fields.UOM, "");
		values.put(Fields.GOOD_FOLDER, "");
		values.put(Fields.PRODUCT_CODE, "");
		values.put(Fields.CODE, "");
		return add(values);
	}

}
