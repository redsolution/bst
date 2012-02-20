package ru.redsolution.bst.data;

/**
 * Тип документа.
 * 
 * @author alexander.ivanov
 * 
 */
public enum DocumentType {

	/**
	 * Инвентаризация.
	 */
	inventory,

	/**
	 * Приемка товара.
	 */
	supply,

	/**
	 * Отгрузка.
	 */
	demand,

	/**
	 * Перемещение.
	 */
	move;

	/**
	 * @return Использовать цены реализации.
	 */
	public boolean useSalePrice() {
		return this == demand || this == move;
	}

}
