package ru.redsolution.bst.data.parse;

import ru.redsolution.bst.data.table.CompanyTable;

public class CompanyImporter extends NamedImporter {

	@Override
	protected void save() {
		CompanyTable.getInstance().add(id, name);
	}

}
