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

import ru.redsolution.bst.data.table.ContractTable;
import android.util.Log;

public class ContractImporter extends NamedImporter {

	protected String company = null;
	protected String myCompany = null;

	@Override
	protected void preProcess(XmlPullParser parser) {
		super.preProcess(parser);
		company = parser.getAttributeValue(null, "agentUuid");
		myCompany = parser.getAttributeValue(null, "ownCompanyUuid");
	}

	@Override
	public boolean isValid() {
		if (!super.isValid())
			return false;
		if (company == null) {
			Log.w(this.getClass().toString(), "company is null ");
			return false;
		}
		if (myCompany == null) {
			Log.w(this.getClass().toString(), "my company is null ");
			return false;
		}
		return true;
	}

	@Override
	protected void save() {
		ContractTable.getInstance().add(id, name, company, myCompany);
	}

}
