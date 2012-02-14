package ru.redsolution.bst.data.parse;

import ru.redsolution.bst.data.table.GoodFolderTable;

public class GoodFolderImporter extends ParentableImporter {

	@Override
	protected void save() {
		GoodFolderTable.getInstance().add(id, name, parent);
	}

}
