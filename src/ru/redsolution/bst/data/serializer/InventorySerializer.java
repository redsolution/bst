package ru.redsolution.bst.data.serializer;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.SelectedGoodTable;
import android.content.ContentValues;

/**
 * Сереализация документа инвентаризации.
 * 
 * @author alexander.ivanov
 * 
 */
public class InventorySerializer extends BaseSerializer {

	@Override
	protected String getContainerName() {
		return "inventory";
	}

	@Override
	protected void renderContainerAttrs(XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		String myCompany = BST.getInstance().getSelectedMyCompany();
		String warehouse = BST.getInstance().getSelectedWarehouse();
		serializer.attribute("", "applicable", "true");
		serializer.attribute("", "payerVat", "true");
		serializer.attribute("", "vatIncluded", "true");
		serializer.attribute("", "sourceStoreUuid", warehouse);
		serializer.attribute("", "sourceAgentUuid", myCompany);
		serializer.attribute("", "targetAgentUuid", myCompany);
	}

	@Override
	protected String getItemName() {
		return "inventoryPosition";
	}

	@Override
	protected void renderItemAttrs(XmlSerializer serializer,
			ContentValues values) throws IllegalArgumentException,
			IllegalStateException, IOException {
		super.renderItemAttrs(serializer, values);
		String quantity = values.getAsString(SelectedGoodTable.Fields.QUANTITY);
		serializer.attribute("", "correctionAmount", String.valueOf(quantity));
	}

}
