package ru.redsolution.bst.data.parse;

import ru.redsolution.bst.data.tables.UomTable;

public class UomImporter extends NamedImporter {

	@Override
	protected void save() {
		UomTable.getInstance().add(id, name);
	}

}
