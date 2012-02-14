package ru.redsolution.bst.data.parse;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Реализует импорт списков объектов.
 * 
 * @author alexander.ivanov
 * 
 */
public class ExchangeImporter extends BaseImporter {

	@Override
	protected boolean parseInnerElement(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		if (super.parseInnerElement(parser))
			return true;
		if (parser.getName().equals("uoms")) {
			new UomsImporter().parse(parser);
			return true;
		} else if (parser.getName().equals("goodFolders")) {
			new GoodFoldersImporter().parse(parser);
			return true;
		} else if (parser.getName().equals("goods")) {
			new GoodsImporter().parse(parser);
			return true;
		} else if (parser.getName().equals("myCompany")) {
			new MyCompaniesImporter().parse(parser);
			return true;
		} else if (parser.getName().equals("agent")) {
			new CompanyFoldersImporter().parse(parser);
			return true;
		} else if (parser.getName().equals("companies")) {
			new CompaniesImporter().parse(parser);
			return true;
		} else if (parser.getName().equals("warehouses")) {
			new WarehousesImporter().parse(parser);
			return true;
			// } else if (parser.getName().equals("consignments")) {
			// new ConsignmentsImporter().parse(parser);
			// return true;
		} else if (parser.getName().equals("project")) {
			new ProjectsImporter().parse(parser);
			return true;
		} else if (parser.getName().equals("contract")) {
			new ContractsImporter().parse(parser);
			return true;
		}
		return false;
	}

}
