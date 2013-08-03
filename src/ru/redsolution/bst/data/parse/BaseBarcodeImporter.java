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

import android.util.Log;

/**
 * Реализует парсинг штрих кодов.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class BaseBarcodeImporter extends BaseImporter implements
		ValidatableImporter {

	protected String type = null;
	protected String value = null;
	protected final BarcodeContainerImporter importer;

	public BaseBarcodeImporter(BarcodeContainerImporter importer) {
		super();
		this.importer = importer;
	}

	@Override
	protected void preProcess(XmlPullParser parser) {
		super.preProcess(parser);
		type = parser.getAttributeValue(null, "barcodeType");
		value = parser.getAttributeValue(null, "barcode");
	}

	@Override
	protected void postProcess() {
		super.postProcess();
		if (isValid())
			importer.addBarcode(this);
	}

	@Override
	public boolean isValid() {
		if (type == null) {
			Log.w(this.getClass().toString(), "type is null");
			return false;
		}
		if (value == null) {
			Log.w(this.getClass().toString(), "value is null");
			return false;
		}
		return true;
	}

	/**
	 * Сохраняет полученный шрих код.
	 * 
	 * @param id
	 *            Идентификатор родительского объекта в базе данных.
	 */
	protected abstract void save(String id);

}
