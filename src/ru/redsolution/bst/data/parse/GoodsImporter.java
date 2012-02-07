package ru.redsolution.bst.data.parse;

public class GoodsImporter extends ContainerImporter {

	@Override
	protected String getItemName() {
		return "good";
	}

	@Override
	protected Importer createItemImporter() {
		return new GoodImporter();
	}

}
