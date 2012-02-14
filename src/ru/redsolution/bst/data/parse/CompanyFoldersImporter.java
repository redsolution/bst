package ru.redsolution.bst.data.parse;

public class CompanyFoldersImporter extends ContainerImporter {

	@Override
	protected String getItemName() {
		return "agent";
	}

	@Override
	protected Importer createItemImporter() {
		return new CompanyFolderImporter();
	}

}
