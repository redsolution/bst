package ru.redsolution.bst.data.parse;

public class ProjectsImporter extends ContainerImporter {

	@Override
	protected String getItemName() {
		return "project";
	}

	@Override
	protected Importer createItemImporter() {
		return new ProjectImporter();
	}

}
