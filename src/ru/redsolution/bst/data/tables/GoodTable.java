package ru.redsolution.bst.data.tables;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;

/**
 * Список складов.
 * 
 * @author alexander.ivanov
 * 
 */
public class GoodTable extends NamedTable {
	public static interface Fields extends NamedTable.Fields {
		public static final String BUY_PRICE = "buy_price";
		public static final String SALE_PRICE = "sale_price";
		public static final String UOM = "uom";
		public static final String GOOD_FOLDER = "good_folder";
		public static final String PRODUCT_CODE = "product_code";
		public static final String CODE = "code";
	}

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

	@Override
	public Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.BUY_PRICE);
		collection.add(Fields.SALE_PRICE);
		collection.add(Fields.UOM);
		collection.add(Fields.GOOD_FOLDER);
		collection.add(Fields.PRODUCT_CODE);
		collection.add(Fields.CODE);
		return collection;
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

	@Override
	public void add(String id, String name) {
		throw new UnsupportedOperationException();
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

}
