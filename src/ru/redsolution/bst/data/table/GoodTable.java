package ru.redsolution.bst.data.table;

import android.content.ContentValues;
import android.database.Cursor;

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
	public void add(String id, String name, String buyPrice, String salePrice,
			String uom, String folder, String productCode, String code) {
		ContentValues values = getValues(id, name);
		values.put(Fields.BUY_PRICE, buyPrice);
		values.put(Fields.SALE_PRICE, salePrice);
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

}
