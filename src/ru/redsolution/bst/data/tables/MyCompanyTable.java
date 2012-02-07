package ru.redsolution.bst.data.tables;


/**
 * Список собственных организаций.
 * 
 * @author alexander.ivanov
 * 
 */
public class MyCompanyTable extends ParentableTable {

	private static final String NAME = "my_company";

	private final static MyCompanyTable instance;

	static {
		instance = new MyCompanyTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static MyCompanyTable getInstance() {
		return instance;
	}

	@Override
	public String getTableName() {
		return NAME;
	}
}
