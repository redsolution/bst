package ru.redsolution.bst.data.table;

/**
 * Список новых штрих кодов для товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class NewGoodBarcodeTable extends BaseBarcodeTable {

	private static final String NAME = "new_good_barcode";

	private final static NewGoodBarcodeTable instance;

	static {
		instance = new NewGoodBarcodeTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static NewGoodBarcodeTable getInstance() {
		return instance;
	}

	private NewGoodBarcodeTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}
}
