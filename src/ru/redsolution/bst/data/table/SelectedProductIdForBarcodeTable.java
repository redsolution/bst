package ru.redsolution.bst.data.table;

/**
 * Список штрих кодов для товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class SelectedProductIdForBarcodeTable extends BaseBarcodeTable {

	private static final String NAME = "selected_product_id_for_barcode";

	private final static SelectedProductIdForBarcodeTable instance;

	static {
		instance = new SelectedProductIdForBarcodeTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static SelectedProductIdForBarcodeTable getInstance() {
		return instance;
	}

	private SelectedProductIdForBarcodeTable() {
	}

	@Override
	public String getTableName() {
		return NAME;
	}
}
