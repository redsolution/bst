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
