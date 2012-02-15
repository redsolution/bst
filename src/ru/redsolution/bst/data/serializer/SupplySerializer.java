package ru.redsolution.bst.data.serializer;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.SelectedTable;
import android.database.Cursor;

public class SupplySerializer extends BaseSerializer {

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
		serializer.attribute("", "targetRequisiteId", myCompany);
		serializer.attribute("", "sourceRequisiteId", company);
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
	protected void renderItemAttrs(XmlSerializer serializer, Cursor cursor)
			throws IllegalArgumentException, IllegalStateException, IOException {
		String good = cursor.getString(cursor
				.getColumnIndex(SelectedTable.Fields._ID));
		int quantity = cursor.getInt(cursor
				.getColumnIndex(SelectedTable.Fields.QUANTITY));
		serializer.attribute("", "quantity", String.valueOf(quantity));
		serializer.attribute("", "goodId", good);
	}

}
