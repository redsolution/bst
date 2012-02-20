package ru.redsolution.bst.data.table;

import android.database.Cursor;

/**
 * Список групп товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class GoodFolderTable extends ParentableTable {

	private static final String NAME = "good_folder";

	private final static GoodFolderTable instance;

	static {
		instance = new GoodFolderTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static GoodFolderTable getInstance() {
		return instance;
	}

	private GoodFolderTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

	@Override
	public Cursor list() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param parent
	 * @return Список групп товаров.
	 */
	public Cursor list(String parent) {
		return filter(Fields.PARENT + " = ?", new String[] { parent },
				Fields.NAME);
	}

}
