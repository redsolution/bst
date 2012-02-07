package ru.redsolution.bst.data.parse;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Интерфейс для парсинга и импорта данных.
 * 
 * @author alexander.ivanov
 * 
 */
public interface Importer {

	/**
	 * Обработка данных.
	 * 
	 * @param parser
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	void parse(XmlPullParser parser) throws XmlPullParserException, IOException;

}
