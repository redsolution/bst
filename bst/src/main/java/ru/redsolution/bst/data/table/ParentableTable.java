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

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;

/**
 * Таблица со столбцом ссылки на строку в собственной таблице.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class ParentableTable extends NamedTable {
	public static interface Fields extends NamedTable.Fields {
		public static final String PARENT = "parent";
	}

	@Override
	public abstract String getTableName();

	@Override
	protected Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.PARENT);
		return collection;
	}

	@Override
	protected ContentValues getValues(String id, String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(String id, String name) {
		throw new UnsupportedOperationException();
	}

	protected ContentValues getValues(String id, String name, String parent) {
		ContentValues values = super.getValues(id, name);
		values.put(Fields.PARENT, parent);
		return values;
	}

	/**
	 * Добавить элемент.
	 * 
	 * @param id
	 * @param name
	 * @param parent
	 */
	public void add(String id, String name, String parent) {
		add(getValues(id, name, parent));
	}

}
