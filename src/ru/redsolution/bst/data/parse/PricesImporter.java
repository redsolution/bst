package ru.redsolution.bst.data.parse;

/**
 * Реализует импорт данных, содержащих цены.
 * 
 * @author alexander.ivanov
 * 
 */
public class PricesImporter extends ContainerImporter {

	private final GoodImporter importer;

	public PricesImporter(GoodImporter importer) {
		this.importer = importer;
	}

	@Override
	protected String getItemName() {
		return "price";
	}

	@Override
	protected Importer createItemImporter() {
		return new PriceImporter(importer);
	}

}
