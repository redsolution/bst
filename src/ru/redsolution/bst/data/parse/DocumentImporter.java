package ru.redsolution.bst.data.parse;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Реализует импорт корневого элемента.
 * 
 * @author alexander.ivanov
 * 
 */
public class DocumentImporter extends BaseImporter {

	@Override
	protected boolean parseInnerElement(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		if (super.parseInnerElement(parser))
			return true;
		if (parser.getName().equals("exchange")) {
			new ExchangeImporter().parse(parser);
			return true;
		}
		return false;
	}

}
