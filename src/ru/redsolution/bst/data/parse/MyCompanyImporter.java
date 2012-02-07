package ru.redsolution.bst.data.parse;

import ru.redsolution.bst.data.tables.MyCompanyTable;

public class MyCompanyImporter extends ParentableImporter {

	@Override
	protected void save() {
		MyCompanyTable.getInstance().add(id, name, parent);
	}

}
