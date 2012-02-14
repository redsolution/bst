package ru.redsolution.bst.data.parse;

public class ContractsImporter extends ContainerImporter {

	@Override
	protected String getItemName() {
		return "contract";
	}

	@Override
	protected Importer createItemImporter() {
		return new ContractImporter();
	}

}
