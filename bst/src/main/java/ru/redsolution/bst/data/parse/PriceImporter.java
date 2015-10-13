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
package ru.redsolution.bst.data.parse;

import org.xmlpull.v1.XmlPullParser;

import ru.redsolution.bst.data.table.PriceTable;
import android.util.Log;

/**
 * Реализует парсинг цен.
 * 
 * @author alexander.ivanov
 * 
 */
public class PriceImporter extends BaseImporter implements ValidatableImporter {

	protected String priceType = null;
	protected String value = null;
	protected final GoodImporter importer;

	public PriceImporter(GoodImporter importer) {
		super();
		this.importer = importer;
	}

	@Override
	protected void preProcess(XmlPullParser parser) {
		super.preProcess(parser);
		priceType = parser.getAttributeValue(null, "priceTypeUuid");
		value = parser.getAttributeValue(null, "value");
	}

	@Override
	protected void postProcess() {
		super.postProcess();
		if (isValid())
			importer.addPrice(this);
	}

	@Override
	public boolean isValid() {
		if (priceType == null) {
			Log.w(this.getClass().toString(), "price type is null");
			return false;
		}
		if (value == null) {
			Log.w(this.getClass().toString(), "value is null");
			return false;
		}
		return true;
	}

	/**
	 * Сохраняет полученную цену.
	 * 
	 * @param id
	 *            Идентификатор родительского объекта в базе данных.
	 */
	protected void save(String id) {
		PriceTable.getInstance().add(id, priceType, value);
	}

}
