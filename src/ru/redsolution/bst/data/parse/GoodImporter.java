package ru.redsolution.bst.data.parse;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ru.redsolution.bst.data.table.GoodTable;

public class GoodImporter extends BaseBarcodeContainerImporter {

	private String buyPrice = null;
	private String salePrice = null;
	private String uom = null;
	private String folder = null;
	private String productCode = null;
	private String code = null;

	@Override
	protected void preProcess(XmlPullParser parser) {
		super.preProcess(parser);
		buyPrice = parser.getAttributeValue(null, "buyPrice");
		salePrice = parser.getAttributeValue(null, "salePrice");
		uom = parser.getAttributeValue(null, "uomId");
		folder = parser.getAttributeValue(null, "parentId");
		productCode = parser.getAttributeValue(null, "productCode");
	}

	@Override
	protected BaseBarcodeImporter createBaseBarcodeImporter() {
		return new GoodBarcodeImporter(this);
	}

	@Override
	protected boolean parseInnerElement(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		if (super.parseInnerElement(parser))
			return true;
		if (parser.getName().equals("code")) {
			code = parseText(parser);
			return true;
		}
		return false;
	}

	@Override
	public boolean isValid() {
		if (buyPrice == null)
			buyPrice = "";
		if (salePrice == null)
			salePrice = "";
		if (uom == null)
			uom = "";
		if (folder == null)
			folder = "";
		if (productCode == null)
			productCode = "";
		if (code == null)
			code = "";
		return super.isValid();
	}

	@Override
	protected void saveInsatce() {
		GoodTable.getInstance().add(id, name, buyPrice, salePrice, uom, folder,
				productCode, code);
	}

}
