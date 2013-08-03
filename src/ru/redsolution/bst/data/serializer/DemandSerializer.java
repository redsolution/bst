package ru.redsolution.bst.data.serializer;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.PriceTable;

/**
 * Сереализация документа отгрузки товаров.
 * 
 * @author alexander.ivanov
 * 
 */
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
			serializer.attribute("", "contractUuid", contract);
		if (!"".equals(project))
			serializer.attribute("", "projectUuid", project);
		serializer.attribute("", "applicable", "true");
		serializer.attribute("", "payerVat", "true");
		serializer.attribute("", "vatIncluded", "true");
		serializer.attribute("", "sourceStoreUuid", warehouse);
		serializer.attribute("", "sourceAgentUuid", myCompany);
		serializer.attribute("", "targetAgentUuid", company);
	}

	@Override
	protected String getItemName() {
		return "shipmentOut";
	}

	@Override
	protected String getPrice(String id) {
		String priceType = BST.getInstance().getSelectedPriceType();
		return PriceTable.getInstance().getPrice(id, priceType);
	}

}
