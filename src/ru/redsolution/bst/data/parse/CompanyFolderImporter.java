package ru.redsolution.bst.data.parse;

import ru.redsolution.bst.data.table.CompanyFolderTable;

public class CompanyFolderImporter extends ParentableImporter {

	@Override
	protected void save() {
		CompanyFolderTable.getInstance().add(id, name, parent);
	}

}
