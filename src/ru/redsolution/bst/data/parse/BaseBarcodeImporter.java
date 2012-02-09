package ru.redsolution.bst.data.parse;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

/**
 * Реализует парсинг штрих кодов.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class BaseBarcodeImporter extends BaseImporter implements
		ValidatableImporter {

	protected String type = null;
	protected String value = null;
	protected final BarcodeContainerImporter importer;

	public BaseBarcodeImporter(BarcodeContainerImporter importer) {
		super();
		this.importer = importer;
	}

	@Override
	protected void preProcess(XmlPullParser parser) {
		super.preProcess(parser);
		type = parser.getAttributeValue(null, "barcodeType");
		value = parser.getAttributeValue(null, "barcode");
	}

	@Override
	protected void postProcess() {
		super.postProcess();
		if (isValid())
			importer.addBarcode(this);
	}

	@Override
	public boolean isValid() {
		if (type == null) {
			Log.w(this.getClass().toString(), "type is null");
			return false;
		}
		if (value == null) {
			Log.w(this.getClass().toString(), "value is null");
			return false;
		}
		return true;
	}

	/**
	 * Сохраняет полученный шрих код.
	 * 
	 * @param id
	 *            Идентификатор родительского объекта в базе данных.
	 */
	protected abstract void save(String id);

}
