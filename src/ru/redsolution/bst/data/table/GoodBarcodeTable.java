package ru.redsolution.bst.data.table;

/**
 * Список штрих кодов для товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class GoodBarcodeTable extends BaseBarcodeTable {

	private static final String NAME = "good_barcode";

	private final static GoodBarcodeTable instance;

	static {
		instance = new GoodBarcodeTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static GoodBarcodeTable getInstance() {
		return instance;
	}

	private GoodBarcodeTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}

}
