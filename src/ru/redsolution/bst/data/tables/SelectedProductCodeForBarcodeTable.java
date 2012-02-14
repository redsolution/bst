package ru.redsolution.bst.data.tables;

/**
 * Список штрих кодов для артикулов товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class SelectedProductCodeForBarcodeTable extends BaseBarcodeTable {

	private static final String NAME = "selected_product_code_for_barcode";

	private final static SelectedProductCodeForBarcodeTable instance;

	static {
		instance = new SelectedProductCodeForBarcodeTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static SelectedProductCodeForBarcodeTable getInstance() {
		return instance;
	}

	private SelectedProductCodeForBarcodeTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}
}
