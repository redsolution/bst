/**
 * Copyright (c) 2013, Redsolution LTD. All rights reserved.
 *
 * This file is part of Barcode Scanner Terminal project;
 * you can redistribute it and/or modify it under the terms of
 *
 * Barcode Scanner Terminal is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
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
