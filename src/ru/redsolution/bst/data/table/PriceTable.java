package ru.redsolution.bst.data.table;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Список цен на товар.
 * 
 * @author alexander.ivanov
 * 
 */
public class PriceTable extends BaseTable {
	public static interface Fields extends BaseTable.Fields {
		public static final String PRICE_TYPE = "price_type";
		public static final String VALUE = "value";
	}

	private static final String NAME = "price";

	private final static PriceTable instance;

	static {
		instance = new PriceTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static PriceTable getInstance() {
		return instance;
	}

	private PriceTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

	@Override
	protected Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.PRICE_TYPE);
		collection.add(Fields.VALUE);
		return collection;
	}

	@Override
	public ContentValues getById(String id) throws ObjectDoesNotExistException,
			MultipleObjectsReturnedException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param good
	 * @param priceType
	 * @return Цену товара или пустую стоку, если цена не известна.
	 */
	public String getPrice(String good, String priceType) {
		ContentValues values;
		try {
			values = get(Fields._ID + " = ? AND " + Fields.PRICE_TYPE + " = ?",
					new String[] { good, priceType });
		} catch (BaseDatabaseException e) {
			return "";
		}
		return values.getAsString(Fields.VALUE);
	}

	public void add(String good, String priceType, String value) {
		ContentValues values = new ContentValues();
		values.put(Fields._ID, good);
		values.put(Fields.PRICE_TYPE, priceType);
		values.put(Fields.VALUE, value);
		add(values);
	}

	@Override
	public void migrate(SQLiteDatabase db, int toVersion) {
		super.migrate(db, toVersion);
		String sql;
		switch (toVersion) {
		case 6:
			sql = "CREATE TABLE price (" + "_id TEXT," + "price_type TEXT,"
					+ "value TEXT);";
			DatabaseHelper.execSQL(db, sql);
			sql = "INSERT INTO price_type (_id, name) VALUES ('sale', 'sale');";
			DatabaseHelper.execSQL(db, sql);
			sql = "INSERT INTO price (_id, price_type, value) "
					+ "SELECT _id, 'sale', sale_price FROM good;";
			DatabaseHelper.execSQL(db, sql);
			break;
		default:
			break;
		}
	}

}
