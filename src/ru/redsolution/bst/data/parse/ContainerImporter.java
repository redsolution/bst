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

	/**
	 * @return Имя вложенного элемента.
	 */
	protected abstract String getItemName();

	/**
	 * @return Объект для импорта объекта.
	 */
	protected abstract Importer createItemImporter();

	@Override
	protected boolean parseInnerElement(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		if (super.parseInnerElement(parser))
			return true;
		if (parser.getName().equals(getItemName())) {
			createItemImporter().parse(parser);
			return true;
		}
		return false;
	}

}
