package ru.redsolution.bst.data.serializer;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.GoodTable;

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
		serializer.attribute("", "targetRequisiteId", myCompany);
		serializer.attribute("", "sourceRequisiteId", company);
		if (!"".equals(contract))
			serializer.attribute("", "contractId", contract);
		if (!"".equals(project))
			serializer.attribute("", "projectId", project);
		serializer.attribute("", "applicable", "true");
		serializer.attribute("", "payerVat", "true");
		serializer.attribute("", "vatIncluded", "true");
		serializer.attribute("", "sourceAgentId", company);
		serializer.attribute("", "targetStoreId", warehouse);
		serializer.attribute("", "targetAgentId", myCompany);
	}

	@Override
	protected String getItemName() {
		return "shipmentIn";
	}

	@Override
	protected String getPriceFieldName() {
		return GoodTable.Fields.BUY_PRICE;
	}

}
