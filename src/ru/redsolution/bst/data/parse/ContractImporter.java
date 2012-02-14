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
		company = parser.getAttributeValue(null, "agentId");
		myCompany = parser.getAttributeValue(null, "ownCompanyId");
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
