package ru.redsolution.bst.data.parse;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

/**
 * Реализует импорт именованных записей.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class NamedImporter extends RowImporter {

	protected String name = null;

	@Override
	protected void preProcess(XmlPullParser parser) {
		super.preProcess(parser);
		name = parser.getAttributeValue(null, "name");
	}

	@Override
	public boolean isValid() {
		if (!super.isValid())
			return false;
		if (name == null) {
			Log.w(this.getClass().toString(), "name is null ");
			return false;
		}
		return true;
	}

}
