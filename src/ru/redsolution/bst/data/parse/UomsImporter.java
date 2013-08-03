package ru.redsolution.bst.data.parse;

public class UomsImporter extends ContainerImporter {

	@Override
	protected String getItemName() {
		return "uom";
	}

	@Override
	protected Importer createItemImporter() {
		return new UomImporter();
	}

}
