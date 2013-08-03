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
