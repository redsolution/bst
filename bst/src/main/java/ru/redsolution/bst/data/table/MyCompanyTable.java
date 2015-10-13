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

/**
 * Список собственных организаций.
 * 
 * @author alexander.ivanov
 * 
 */
public class MyCompanyTable extends ParentableTable {

	private static final String NAME = "my_company";

	private final static MyCompanyTable instance;

	static {
		instance = new MyCompanyTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static MyCompanyTable getInstance() {
		return instance;
	}

	private MyCompanyTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

}
