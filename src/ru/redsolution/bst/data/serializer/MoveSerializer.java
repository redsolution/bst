package ru.redsolution.bst.data.serializer;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.data.BST;

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
		serializer.attribute("", "sourceStoreId", warehouse);
		serializer.attribute("", "targetStoreId", targetWarehouse);
		serializer.attribute("", "sourceAgentId", myCompany);
		serializer.attribute("", "targetAgentId", myCompany);
		if (!"".equals(project))
			serializer.attribute("", "projectId", project);
	}

	@Override
	protected String getItemName() {
		return "movePosition";
	}

}
