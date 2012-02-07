package ru.redsolution.bst.data.parse;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

/**
 * Импорт, валидаций и сохранение записи в базе данных.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class RowImporter extends BaseImporter implements
		ValidatableImporter {

	protected String id = null;

	@Override
	protected boolean parseInnerElement(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		if (super.parseInnerElement(parser))
			return true;
		if (parser.getName().equals("id")) {
			id = parseText(parser);
			return true;
		}
		return false;
	}

	@Override
	protected void postProcess() {
		super.postProcess();
		if (isValid())
			save();
	}

	@Override
	public boolean isValid() {
		if (id == null) {
			Log.w(this.getClass().toString(), "id is null");
			return false;
		}
		return true;
	}

	/**
	 * Сохраняет полученные данные.
	 */
	protected abstract void save();
}
