package ru.redsolution.bst.data.serializer;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.GoodTable;
import ru.redsolution.bst.data.table.SelectedTable;
import android.database.Cursor;

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
		serializer.attribute("", "targetRequisiteId", company);
		serializer.attribute("", "sourceRequisiteId", myCompany);
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
	protected void renderItemAttrs(XmlSerializer serializer, Cursor cursor)
			throws IllegalArgumentException, IllegalStateException, IOException {
		String good = cursor.getString(cursor
				.getColumnIndex(SelectedTable.Fields._ID));
		int quantity = cursor.getInt(cursor
				.getColumnIndex(SelectedTable.Fields.QUANTITY));
		serializer.attribute("", "quantity", String.valueOf(quantity));
		serializer.attribute("", "goodId", good);
	}

	@Override
	protected String getPriceFieldName() {
		return GoodTable.Fields.SALE_PRICE;
	}

}
