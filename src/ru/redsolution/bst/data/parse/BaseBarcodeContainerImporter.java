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

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Реализует импорт данных, содержащих штрих коды.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class BaseBarcodeContainerImporter extends NamedImporter
		implements BarcodeContainerImporter {

	private final ArrayList<BaseBarcodeImporter> barcodeImporters = new ArrayList<BaseBarcodeImporter>();

	/**
	 * Создаёт объект для импорта штрих кода.
	 * 
	 * @return
	 */
	protected abstract BaseBarcodeImporter createBaseBarcodeImporter();

	@Override
	protected boolean parseInnerElement(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		if (super.parseInnerElement(parser))
			return true;
		if (parser.getName().equals("barcode")) {
			createBaseBarcodeImporter().parse(parser);
			return true;
		}
		return false;
	}

	/**
	 * Сохраняет полученные данные об объекте.
	 */
	protected abstract void saveInsatce();

	@Override
	protected void save() {
		saveInsatce();
		for (BaseBarcodeImporter barcodeImporter : barcodeImporters)
			barcodeImporter.save(id);
	}

	@Override
	public void addBarcode(BaseBarcodeImporter barcodeImporter) {
		barcodeImporters.add(barcodeImporter);
	}

}
