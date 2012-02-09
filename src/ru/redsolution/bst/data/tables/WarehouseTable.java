package ru.redsolution.bst.data.tables;

/**
 * Список складов.
 * 
 * @author alexander.ivanov
 * 
 */
public class WarehouseTable extends ParentableTable {

	private static final String NAME = "warehouse";

	private final static WarehouseTable instance;

	static {
		instance = new WarehouseTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static WarehouseTable getInstance() {
		return instance;
	}

	private WarehouseTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

}
