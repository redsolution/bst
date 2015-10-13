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
import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.BaseGoodTable;
import ru.redsolution.bst.data.table.CustomGoodTable;
import ru.redsolution.bst.data.table.GoodTable;
import ru.redsolution.bst.data.table.NewGoodBarcodeTable;
import ru.redsolution.bst.data.table.SelectedGoodTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Xml;

/**
 * Базовый класс для сериализации данных для отправки.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class BaseSerializer {

	private static final String DESCRIPTION_TAG = "description";

	/**
	 * @return XML для отправки.
	 */
	public String getXml() throws IllegalArgumentException,
			IllegalStateException, IOException {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer);
		serializer.startDocument("UTF-8", true);
		serializer.startTag("", getContainerName());
		renderContainerAttrs(serializer);
		renderContainerBody(serializer);
		serializer.endTag("", getContainerName());
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * @return Имя контейнера.
	 */
	protected abstract String getContainerName();

	/**
	 * Задаёт атрибуты для контейнера.
	 * 
	 * @param serializer
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected abstract void renderContainerAttrs(XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException;

	/**
	 * Задаёт контент для контейнера.
	 * 
	 * @param serializer
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected void renderContainerBody(XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", DESCRIPTION_TAG);
		Cursor cursor = SelectedGoodTable.getInstance().listCustom(true);
		try {
			if (cursor.moveToFirst()) {
				serializer.text(BST.getInstance().getString(
						R.string.custom_good_description));
				do {
					ContentValues values = SelectedGoodTable.getInstance()
							.getValues(cursor);
					String id = values
							.getAsString(SelectedGoodTable.Fields._ID);
					ContentValues good;
					try {
						good = CustomGoodTable.getInstance().getById(id);
					} catch (BaseDatabaseException e) {
						continue;
					}
					serializer.text(values
							.getAsString(SelectedGoodTable.Fields.QUANTITY));
					serializer.text(" ");
					serializer.text(good
							.getAsString(CustomGoodTable.Fields.NAME));
					serializer.text("\n");
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		cursor = NewGoodBarcodeTable.getInstance().list();
		try {
			if (cursor.moveToFirst()) {
				serializer.text(BST.getInstance().getString(
						R.string.new_barcode_description));
				do {
					ContentValues values = NewGoodBarcodeTable.getInstance()
							.getValues(cursor);
					String id = values
							.getAsString(NewGoodBarcodeTable.Fields._ID);
					ContentValues good;
					try {
						if (values
								.getAsBoolean(NewGoodBarcodeTable.Fields.IS_CUSTOM))
							good = CustomGoodTable.getInstance().getById(id);
						else
							good = GoodTable.getInstance().getById(id);
					} catch (BaseDatabaseException e) {
						continue;
					}
					serializer.text(values
							.getAsString(NewGoodBarcodeTable.Fields.VALUE));
					serializer.text(" ");
					serializer.text(values
							.getAsString(NewGoodBarcodeTable.Fields.TYPE));
					serializer.text(" ");
					String value = good
							.getAsString(BaseGoodTable.Fields.PRODUCT_CODE);
					if ("".equals(value))
						value = good.getAsString(BaseGoodTable.Fields.NAME);
					serializer.text(value);
					serializer.text("\n");
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		serializer.endTag("", DESCRIPTION_TAG);
		cursor = SelectedGoodTable.getInstance().listCustom(false);
		try {
			if (cursor.moveToFirst()) {
				do {
					serializer.startTag("", getItemName());
					ContentValues values = SelectedGoodTable.getInstance()
							.getValues(cursor);
					renderItemAttrs(serializer, values);
					renderItemBody(serializer, values);
					serializer.endTag("", getItemName());
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
	}

	/**
	 * @return Имя элемента.
	 */
	protected abstract String getItemName();

	/**
	 * Задаёт атрибуты для элемента.
	 * 
	 * @param serializer
	 * @param cursor
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected void renderItemAttrs(XmlSerializer serializer,
			ContentValues values) throws IllegalArgumentException,
			IllegalStateException, IOException {
		String good = values.getAsString(SelectedGoodTable.Fields._ID);
		String quantity = values.getAsString(SelectedGoodTable.Fields.QUANTITY);
		serializer.attribute("", "quantity", String.valueOf(quantity));
		serializer.attribute("", "goodUuid", good);
	}

	/**
	 * Задаёт контент для элемента.
	 * 
	 * @param serializer
	 * @param cursor
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected void renderItemBody(XmlSerializer serializer, ContentValues values)
			throws IllegalArgumentException, IllegalStateException, IOException {
	}

	/**
	 * Записывает тег с текстовым значением.
	 * 
	 * @param serializer
	 * @param tag
	 * @param text
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected void renderTag(XmlSerializer serializer, String tag, String text)
			throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", tag);
		serializer.text(text);
		serializer.endTag("", tag);
	}

}
