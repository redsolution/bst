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
import android.database.Cursor;

/**
 * Таблица с именен объекта и его идентификатором.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class NamedTable extends BaseTable {
	public static interface Fields extends BaseTable.Fields {
		public static final String NAME = "name";
	}

	@Override
	protected Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.NAME);
		return collection;
	}

	@Override
	protected String getFieldType(String name) {
		String type = super.getFieldType(name);
		if (Fields.NAME.equals(name))
			type += " COLLATE UNICODE";
		return type;
	}

	@Override
	public Cursor list() {
		return filter(null, null, Fields.NAME);
	}

	/**
	 * @param id
	 * @return Имя объекта по его идентификатору.
	 * @throws MultipleObjectsReturnedException
	 * @throws ObjectDoesNotExistException
	 */
	public String getName(String id) throws ObjectDoesNotExistException,
			MultipleObjectsReturnedException {
		return getById(id).getAsString(Fields.NAME);
	}

	/**
	 * @param id
	 * @param name
	 * @return Набор значений для добавления элемента.
	 */
	protected ContentValues getValues(String id, String name) {
		ContentValues values = new ContentValues();
		values.put(Fields._ID, id);
		values.put(Fields.NAME, name);
		return values;
	}

	/**
	 * Добавить элемент.
	 * 
	 * @param id
	 * @param name
	 */
	public void add(String id, String name) {
		add(getValues(id, name));
	}

}
