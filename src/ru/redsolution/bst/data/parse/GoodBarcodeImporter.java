package ru.redsolution.bst.data.parse;

import ru.redsolution.bst.data.table.GoodBarcodeTable;

public class GoodBarcodeImporter extends BaseBarcodeImporter {

	public GoodBarcodeImporter(BarcodeContainerImporter importer) {
		super(importer);
	}

	@Override
	protected void save(String id) {
		GoodBarcodeTable.getInstance().add(id, type, value);
	}

}
