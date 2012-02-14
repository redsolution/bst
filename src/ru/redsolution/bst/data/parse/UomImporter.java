package ru.redsolution.bst.data.parse;

import ru.redsolution.bst.data.table.UomTable;

public class UomImporter extends NamedImporter {

	@Override
	protected void save() {
		UomTable.getInstance().add(id, name);
	}

}
