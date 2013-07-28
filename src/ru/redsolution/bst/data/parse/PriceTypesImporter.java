package ru.redsolution.bst.data.parse;

public class PriceTypesImporter extends ContainerImporter {

	@Override
	protected String getItemName() {
		return "priceType";
	}

	@Override
	protected Importer createItemImporter() {
		return new PriceTypeImporter();
	}

}
