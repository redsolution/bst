package ru.redsolution.bst.data.parse;

import org.xmlpull.v1.XmlPullParser;

/**
 * Реализует импорт данных со ссылкой на запись в собственной таблице данных.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class ParentableImporter extends NamedImporter {

	protected String parent = null;

	@Override
	protected void preProcess(XmlPullParser parser) {
		super.preProcess(parser);
		parent = parser.getAttributeValue(null, "parentUuid");
	}

	@Override
	public boolean isValid() {
		if (parent == null)
			parent = "";
		return super.isValid();
	}

}
