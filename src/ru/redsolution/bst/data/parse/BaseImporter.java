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

import ru.redsolution.bst.data.table.DatabaseHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Реализует базовый парсинг данных.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class BaseImporter implements Importer {

	/**
	 * Парсинг вложенного элемента.
	 * 
	 * @param parser
	 * @return Истину, если элемент был обработан.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	protected boolean parseInnerElement(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		return false;
	}

	/**
	 * Подготовка к парсингу вложенных элементов.
	 * 
	 * @param parser
	 */
	protected void preProcess(XmlPullParser parser) {
	}

	/**
	 * Разбор вложенных тегов.
	 * 
	 * @param parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	protected void parseInner(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		int inner = 1;
		while (inner > 0) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (inner == 1)
					if (parseInnerElement(parser))
						continue;
				inner += 1;
			} else if (eventType == XmlPullParser.END_TAG) {
				inner -= 1;
			} else if (eventType == XmlPullParser.END_DOCUMENT)
				break;
		}
	}

	@Override
	public void parse(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		SQLiteDatabase db = DatabaseHelper.getInstance().getWritableDatabase();
		try {
			db.beginTransaction();
			preProcess(parser);
			parseInner(parser);
			postProcess();
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * Завершение обработки элемента.
	 */
	protected void postProcess() {
	}

	/**
	 * @param parser
	 * @return Текст внутри тега и вложенных гетов.
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public static String parseText(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		final StringBuilder builder = new StringBuilder();
		int inner = 1;
		while (inner > 0) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.TEXT)
				builder.append(parser.getText());
			if (eventType == XmlPullParser.START_TAG) {
				inner += 1;
			} else if (eventType == XmlPullParser.END_TAG) {
				inner -= 1;
			} else if (eventType == XmlPullParser.END_DOCUMENT)
				break;
		}
		return builder.toString();
	}

}
