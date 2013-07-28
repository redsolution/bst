package ru.redsolution.bst.data;

/**
 * Информация о состояния импорта.
 * 
 * @author alexander.ivanov
 * 
 */
public class ImportProgress {

	/**
	 * ID ресурса с именем источника данных.
	 */
	private final int sourceName;

	/**
	 * Номер загружаемого источника данных.
	 */
	private final int sourceIndex;

	/**
	 * Общее количество источников данных.
	 */
	private final int sourceCount;

	public ImportProgress(int sourceName, int sourceIndex, int sourceCount,
			int progress, int maximum) {
		super();
		this.sourceName = sourceName;
		this.sourceIndex = sourceIndex;
		this.sourceCount = sourceCount;
	}

	public int getSourceName() {
		return sourceName;
	}

	public int getSourceIndex() {
		return sourceIndex;
	}

	public int getSourceCount() {
		return sourceCount;
	}

}
