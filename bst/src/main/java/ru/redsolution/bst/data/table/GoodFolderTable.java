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

import android.database.Cursor;

/**
 * Список групп товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class GoodFolderTable extends ParentableTable {

	private static final String NAME = "good_folder";

	private final static GoodFolderTable instance;

	static {
		instance = new GoodFolderTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static GoodFolderTable getInstance() {
		return instance;
	}

	private GoodFolderTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

	@Override
	public Cursor list() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param parent
	 * @return Список групп товаров.
	 */
	public Cursor list(String parent) {
		return filter(Fields.PARENT + " = ?", new String[] { parent },
				Fields.NAME);
	}

}
