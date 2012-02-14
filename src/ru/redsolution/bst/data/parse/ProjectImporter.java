package ru.redsolution.bst.data.parse;

import ru.redsolution.bst.data.table.ProjectTable;

public class ProjectImporter extends NamedImporter {

	@Override
	protected void save() {
		ProjectTable.getInstance().add(id, name);
	}

}
