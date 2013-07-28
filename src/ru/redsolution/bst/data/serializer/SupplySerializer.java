package ru.redsolution.bst.data.serializer;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.GoodTable;
import android.content.ContentValues;

public class SupplySerializer extends PricedSerializer {

	@Override
	protected String getContainerName() {
		return "supply";
	}

	@Override
	protected void renderContainerAttrs(XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		String myCompany = BST.getInstance().getSelectedMyCompany();
		String warehouse = BST.getInstance().getSelectedWarehouse();
		String company = BST.getInstance().getSelectedCompany();
		String contract = BST.getInstance().getSelectedContract();
		String project = BST.getInstance().getSelectedProject();
		if (!"".equals(contract))
			serializer.attribute("", "contractUuid", contract);
		if (!"".equals(project))
			serializer.attribute("", "projectUuid", project);
		serializer.attribute("", "applicable", "true");
		serializer.attribute("", "payerVat", "true");
		serializer.attribute("", "vatIncluded", "true");
		serializer.attribute("", "sourceAgentUuid", company);
		serializer.attribute("", "targetStoreUuid", warehouse);
		serializer.attribute("", "targetAgentUuid", myCompany);
	}

	@Override
	protected String getItemName() {
		return "shipmentIn";
	}

	@Override
	protected String getPrice(String id) {
		ContentValues good;
		try {
			good = GoodTable.getInstance().getById(id);
		} catch (BaseDatabaseException e) {
			return "";
		}
		return good.getAsString(GoodTable.Fields.BUY_PRICE);
	}

}
