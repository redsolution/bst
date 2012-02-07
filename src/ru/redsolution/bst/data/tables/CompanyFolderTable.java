package ru.redsolution.bst.data.tables;

/**
 * Список групп организаций.
 * 
 * @author alexander.ivanov
 * 
 */
public class CompanyFolderTable extends ParentableTable {

	private static final String NAME = "company_folder";

	private final static CompanyFolderTable instance;

	static {
		instance = new CompanyFolderTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static CompanyFolderTable getInstance() {
		return instance;
	}

	@Override
	public String getTableName() {
		return NAME;
	}
}
