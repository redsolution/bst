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
package ru.redsolution.bst.data.serializer;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.data.table.SelectedGoodTable;
import android.content.ContentValues;

/**
 * Класс для сериализации данных и учета стоимости ТМЦ в документе.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class PricedSerializer extends BaseSerializer {

	private static final String PRICE_TAG = "price";
	private static final String BASE_PRICE_TAG = "basePrice";

	/**
	 * @param good
	 *            Идентификатор товара.
	 * @return Цена товара.
	 */
	protected abstract String getPrice(String good);

	@Override
	protected void renderItemBody(XmlSerializer serializer, ContentValues values)
			throws IllegalArgumentException, IllegalStateException, IOException {
		super.renderItemBody(serializer, values);
		String id = values.getAsString(SelectedGoodTable.Fields._ID);
		String price = getPrice(id);
		if ("".equals(price))
			return;
		serializer.startTag("", BASE_PRICE_TAG);
		serializer.attribute("", "sumInCurrency", price);
		serializer.attribute("", "sum", price);
		serializer.endTag("", BASE_PRICE_TAG);
		serializer.startTag("", PRICE_TAG);
		serializer.attribute("", "sumInCurrency", price);
		serializer.attribute("", "sum", price);
		serializer.endTag("", PRICE_TAG);
	}

}
