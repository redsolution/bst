package ru.redsolution.bst.data.parse;

public class CompaniesImporter extends ContainerImporter {

	@Override
	protected String getItemName() {
		return "company";
	}

	@Override
	protected Importer createItemImporter() {
		return new CompanyImporter();
	}

}
