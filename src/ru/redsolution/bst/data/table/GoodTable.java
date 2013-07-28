package ru.redsolution.bst.data.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Список товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class GoodTable extends BaseGoodTable {

	private static final String NAME = "good";

	private final static GoodTable instance;

	static {
		instance = new GoodTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static GoodTable getInstance() {
		return instance;
	}

	private GoodTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

	/**
	 * @param productCode
	 * @return Товар.
	 * @throws ObjectDoesNotExistException
	 * @throws MultipleObjectsReturnedException
	 */
	public ContentValues getByProductCode(String productCode)
			throws ObjectDoesNotExistException,
			MultipleObjectsReturnedException {
		return get(Fields.PRODUCT_CODE + " = ?", new String[] { productCode });
	}

	/**
	 * Добавить объект.
	 * 
	 * @param id
	 * @param name
	 * @param buyPrice
	 * @param salePrice
	 * @param uom
	 * @param folder
	 * @param productCode
	 * @param code
	 */
	public void add(String id, String name, String buyPrice, String uom,
			String folder, String productCode, String code) {
		ContentValues values = getValues(id, name);
		values.put(Fields.BUY_PRICE, buyPrice);
		values.put(Fields.UOM, uom);
		values.put(Fields.GOOD_FOLDER, folder);
		values.put(Fields.PRODUCT_CODE, productCode);
		values.put(Fields.CODE, code);
		add(values);
	}

	/**
	 * @param folder
	 * @return Товары в указанной папке.
	 */
	public Cursor list(String folder) {
		return filter(Fields.GOOD_FOLDER + " = ?", new String[] { folder },
				Fields.NAME);
	}

	@Override
	public void migrate(SQLiteDatabase db, int toVersion) {
		super.migrate(db, toVersion);
		String sql;
		switch (toVersion) {
		case 7:
			sql = "ALTER TABLE good RENAME TO good_;";
			DatabaseHelper.execSQL(db, sql);
			sql = "CREATE TABLE good (" + "_id TEXT,"
					+ "name TEXT COLLATE UNICODE, " + "buy_price TEXT,"
					+ "uom TEXT," + "good_folder TEXT," + "product_code TEXT,"
					+ "code TEXT," + "lower_cased_name TEXT);";
			DatabaseHelper.execSQL(db, sql);
			sql = "INSERT INTO good ("
					+ "_id, name, buy_price, uom, good_folder, "
					+ "product_code, code, lower_cased_name) "
					+ "SELECT _id, name, buy_price, uom, good_folder, "
					+ "product_code, code, lower_cased_name FROM good_;";
			DatabaseHelper.execSQL(db, sql);
			sql = "DROP TABLE good_;";
			DatabaseHelper.execSQL(db, sql);
			break;
		default:
			break;
		}
	}

}
