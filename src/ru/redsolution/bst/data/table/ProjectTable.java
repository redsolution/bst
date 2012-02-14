package ru.redsolution.bst.data.table;

/**
 * Список проектов.
 * 
 * @author alexander.ivanov
 * 
 */
public class ProjectTable extends NamedTable {

	private static final String NAME = "project";

	private final static ProjectTable instance;

	static {
		instance = new ProjectTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static ProjectTable getInstance() {
		return instance;
	}

	private ProjectTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

}
