package ru.redsolution.bst.data.parse;

public class WarehousesImporter extends ContainerImporter {

	@Override
	protected String getItemName() {
		return "warehouse";
	}

	@Override
	protected Importer createItemImporter() {
		return new WarehouseImporter();
	}

}
