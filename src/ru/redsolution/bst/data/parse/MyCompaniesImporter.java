package ru.redsolution.bst.data.parse;

public class MyCompaniesImporter extends ContainerImporter {

	@Override
	protected String getItemName() {
		return "myCompany";
	}

	@Override
	protected Importer createItemImporter() {
		return new MyCompanyImporter();
	}

}
