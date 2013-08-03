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
