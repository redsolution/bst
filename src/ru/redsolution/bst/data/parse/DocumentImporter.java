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

	private final BaseImporter importer;

	public DocumentImporter(BaseImporter importer) {
		this.importer = importer;
	}

	@Override
	protected boolean parseInnerElement(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		if (super.parseInnerElement(parser))
			return true;
		if (parser.getName().equals("collection")) {
			importer.parse(parser);
			return true;
		}
		return false;
	}

}
