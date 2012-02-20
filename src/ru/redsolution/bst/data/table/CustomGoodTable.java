package ru.redsolution.bst.data.table;

import android.content.ContentValues;

/**
 * Список пользовательских товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class CustomGoodTable extends BaseGoodTable {

	private static final String NAME = "custom_good";

	private final static CustomGoodTable instance;

	static {
		instance = new CustomGoodTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static CustomGoodTable getInstance() {
		return instance;
	}

	private CustomGoodTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

	@Override
	public String getFieldType(String name) {
		if (Fields._ID.equals(name))
			return "INTEGER PRIMARY KEY";
		return super.getFieldType(name);
	}

	/**
	 * Добавить объект.
	 * 
	 * @param name
	 * @return
	 */
	public long add(String name) {
		ContentValues values = new ContentValues();
		values.put(Fields.NAME, name);
		values.put(Fields.BUY_PRICE, "");
		values.put(Fields.SALE_PRICE, "");
		values.put(Fields.UOM, "");
		values.put(Fields.GOOD_FOLDER, "");
		values.put(Fields.PRODUCT_CODE, "");
		values.put(Fields.CODE, "");
		return add(values);
	}

}
