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

/**
 * Сереализация документа перемещения товаров.
 * 
 * @author alexander.ivanov
 * 
 */
public class MoveSerializer extends BaseSerializer {

	@Override
	protected String getContainerName() {
		return "move";
	}

	@Override
	protected void renderContainerAttrs(XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		String myCompany = BST.getInstance().getSelectedMyCompany();
		String warehouse = BST.getInstance().getSelectedWarehouse();
		String targetWarehouse = BST.getInstance().getSelectedTargetWarehouse();
		String project = BST.getInstance().getSelectedProject();
		serializer.attribute("", "applicable", "true");
		serializer.attribute("", "payerVat", "true");
		serializer.attribute("", "vatIncluded", "true");
		serializer.attribute("", "sourceStoreUuid", warehouse);
		serializer.attribute("", "targetStoreUuid", targetWarehouse);
		serializer.attribute("", "sourceAgentUuid", myCompany);
		serializer.attribute("", "targetAgentUuid", myCompany);
		if (!"".equals(project))
			serializer.attribute("", "projectUuid", project);
	}

	@Override
	protected String getItemName() {
		return "movePosition";
	}

}
