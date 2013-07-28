package ru.redsolution.bst.data.parse;

import ru.redsolution.bst.data.table.PriceTypeTable;

public class PriceTypeImporter extends NamedImporter {

	@Override
	protected void save() {
		PriceTypeTable.getInstance().add(id, name);
	}

}
