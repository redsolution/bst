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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Реализует парсинг списка объектов.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class ContainerImporter extends BaseImporter {

	private int count;

	private Integer total;

	public ContainerImporter() {
		count = 0;
	}

	/**
	 * @return Имя вложенного элемента.
	 */
	protected abstract String getItemName();

	/**
	 * @return Объект для импорта объекта.
	 */
	protected abstract Importer createItemImporter();

	@Override
	protected void preProcess(XmlPullParser parser) {
		super.preProcess(parser);
		String value = parser.getAttributeValue(null, "total");
		if (value != null)
			total = Integer.valueOf(value);
	}

	@Override
	protected boolean parseInnerElement(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		if (super.parseInnerElement(parser))
			return true;
		if (parser.getName().equals(getItemName())) {
			createItemImporter().parse(parser);
			count += 1;
			return true;
		}
		return false;
	}

	/**
	 * @return Количество созданных парсеров элементов.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Обнуляет счетик созданных парсеров элементов.
	 */
	public void resetCount() {
		count = 0;
	}

	/**
	 * @return Общее количество элементов.
	 */
	public Integer getTotal() {
		return total;
	}

}
