package ru.redsolution.bst.data.serializer;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.GoodTable;

public class DemandSerializer extends PricedSerializer {

	@Override
	protected String getContainerName() {
		return "demand";
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
			serializer.attribute("", "contractId", contract);
		if (!"".equals(project))
			serializer.attribute("", "projectId", project);
		serializer.attribute("", "applicable", "true");
		serializer.attribute("", "payerVat", "true");
		serializer.attribute("", "vatIncluded", "true");
		serializer.attribute("", "sourceStoreId", warehouse);
		serializer.attribute("", "sourceAgentId", myCompany);
		serializer.attribute("", "targetAgentId", company);
	}

	@Override
	protected String getItemName() {
		return "shipmentOut";
	}

	@Override
	protected String getPriceFieldName() {
		return GoodTable.Fields.SALE_PRICE;
	}

}
