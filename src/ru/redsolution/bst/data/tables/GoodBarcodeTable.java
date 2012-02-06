package ru.redsolution.bst.data.tables;

/**
 * Список штрих кодов для товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class GoodBarcodeTable extends ParantableTable {

	private static final String NAME = "good_barcode";

	private final static GoodBarcodeTable instance;

	static {
		instance = new GoodBarcodeTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static GoodBarcodeTable getInstance() {
		return instance;
	}

	@Override
	public String getTableName() {
		return NAME;
	}
}
