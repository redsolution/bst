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

	/**
	 * Количество загруженных записей.
	 */
	private final int progress;

	/**
	 * Общее количество записей. -1, если значение не известно.
	 */
	private final int maximum;

	public ImportProgress(int sourceName, int sourceIndex, int sourceCount,
			int progress, int maximum) {
		super();
		this.sourceName = sourceName;
		this.sourceIndex = sourceIndex;
		this.sourceCount = sourceCount;
		this.progress = progress;
		this.maximum = maximum;
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

	public int getProgress() {
		return progress;
	}

	public int getMaximum() {
		return maximum;
	}

}
