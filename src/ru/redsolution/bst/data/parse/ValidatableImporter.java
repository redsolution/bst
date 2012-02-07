package ru.redsolution.bst.data.parse;

public interface ValidatableImporter {

	/**
	 * @return Истину, если полученны корректные данные.
	 */
	boolean isValid();

}
