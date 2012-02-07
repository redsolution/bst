package ru.redsolution.bst.data.parse;

/**
 * Хранилище для штрих кодов.
 * 
 * @author alexander.ivanov
 * 
 */
public interface BarcodeContainerImporter {

	/**
	 * Добавляет парсер, содержащий корректный штрих код.
	 * 
	 * @param barcodeImporter
	 */
	void addBarcode(BaseBarcodeImporter barcodeImporter);

}
