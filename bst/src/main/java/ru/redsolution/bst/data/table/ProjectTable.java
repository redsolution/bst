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
 * Список проектов.
 * 
 * @author alexander.ivanov
 * 
 */
public class ProjectTable extends NamedTable {

	private static final String NAME = "project";

	private final static ProjectTable instance;

	static {
		instance = new ProjectTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static ProjectTable getInstance() {
		return instance;
	}

	private ProjectTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

}
