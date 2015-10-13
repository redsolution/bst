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
 * Список абстрактных товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class BaseGoodTable extends NamedTable {
	public static interface Fields extends NamedTable.Fields {
		public static final String BUY_PRICE = "buy_price";
		public static final String UOM = "uom";
		public static final String GOOD_FOLDER = "good_folder";
		public static final String PRODUCT_CODE = "product_code";
		public static final String CODE = "code";
		public static final String LOWER_CASED_NAME = "lower_cased_name";
	}

	@Override
	protected Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.BUY_PRICE);
		collection.add(Fields.UOM);
		collection.add(Fields.GOOD_FOLDER);
		collection.add(Fields.PRODUCT_CODE);
		collection.add(Fields.CODE);
		collection.add(Fields.LOWER_CASED_NAME);
		return collection;
	}

	@Override
	public void add(String id, String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected long add(ContentValues values) {
		values.put(Fields.LOWER_CASED_NAME, values.getAsString(Fields.NAME)
				.toLowerCase());
		return super.add(values);
	}

	/**
	 * @param text
	 * @return Товары, содержащие текст в наименовании или артикуле.
	 */
	public Cursor filterByText(String text) {
		text = "%" + text + "%";
		return filter(Fields.PRODUCT_CODE + " LIKE ? OR "
				+ Fields.LOWER_CASED_NAME + " LIKE ?", new String[] { text,
				text, }, Fields.NAME);
	}

}
