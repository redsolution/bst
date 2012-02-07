package ru.redsolution.bst.data.parse;

public class GoodFoldersImporter extends ContainerImporter {

	@Override
	protected String getItemName() {
		return "goodFolder";
	}

	@Override
	protected Importer createItemImporter() {
		return new GoodFolderImporter();
	}

}
