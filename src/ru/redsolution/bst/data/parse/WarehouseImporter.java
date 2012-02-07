package ru.redsolution.bst.data.parse;

import ru.redsolution.bst.data.tables.WarehouseTable;

public class WarehouseImporter extends ParentableImporter {

	@Override
	protected void save() {
		WarehouseTable.getInstance().add(id, name, parent);
	}

}
