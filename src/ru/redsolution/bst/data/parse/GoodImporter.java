package ru.redsolution.bst.data.parse;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ru.redsolution.bst.data.table.GoodTable;

public class GoodImporter extends BaseBarcodeContainerImporter {

	private final ArrayList<PriceImporter> priceImporters = new ArrayList<PriceImporter>();

	private String buyPrice = null;
	private String uom = null;
	private String folder = null;
	private String productCode = null;
	private String code = null;

	@Override
	protected void preProcess(XmlPullParser parser) {
		super.preProcess(parser);
		buyPrice = parser.getAttributeValue(null, "buyPrice");
		uom = parser.getAttributeValue(null, "uomUuid");
		folder = parser.getAttributeValue(null, "parentUuid");
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
		if (parser.getName().equals("salePrices")) {
			new PricesImporter(this).parse(parser);
			return true;
		}
		return false;
	}

	@Override
	public boolean isValid() {
		if (buyPrice == null)
			buyPrice = "";
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
		GoodTable.getInstance().add(id, name, buyPrice, uom, folder,
				productCode, code);
	}

	@Override
	protected void save() {
		super.save();
		for (PriceImporter priceImporter : priceImporters)
			priceImporter.save(id);
	}

	public void addPrice(PriceImporter priceImporter) {
		priceImporters.add(priceImporter);
	}

}
