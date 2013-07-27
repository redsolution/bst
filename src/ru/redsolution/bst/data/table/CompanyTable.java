package ru.redsolution.bst.data.table;


/**
 * Список организаций.
 * 
 * @author alexander.ivanov
 * 
 */
public class CompanyTable extends NamedTable {

	private static final String NAME = "company";

	private final static CompanyTable instance;

	static {
		instance = new CompanyTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static CompanyTable getInstance() {
		return instance;
	}

	private CompanyTable() {
	}

	@Override
	protected String getTableName() {
		return NAME;
	}

}
