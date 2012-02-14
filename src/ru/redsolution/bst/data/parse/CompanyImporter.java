package ru.redsolution.bst.data.parse;

import org.xmlpull.v1.XmlPullParser;

import ru.redsolution.bst.data.table.CompanyTable;

public class CompanyImporter extends NamedImporter {

	protected String folder = null;

	@Override
	protected void preProcess(XmlPullParser parser) {
		super.preProcess(parser);
		folder = parser.getAttributeValue(null, "parentId");
	}

	@Override
	public boolean isValid() {
		if (folder == null)
			folder = "";
		return super.isValid();
	}

	@Override
	protected void save() {
		CompanyTable.getInstance().add(id, name, folder);
	}

}
