package ru.redsolution.bst.data.table;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Список договоров.
 * 
 * @author alexander.ivanov
 * 
 */
public class ContractTable extends NamedTable {
	public static final class Fields implements NamedTable.Fields {
		private Fields() {
		}

		public static final String COMPANY = "company";
		public static final String MY_COMPANY = "my_company";
	}

	private static final String NAME = "contract";

	private final static ContractTable instance;

	static {
		instance = new ContractTable();
		DatabaseHelper.getInstance().addTable(instance);
	}

	public static ContractTable getInstance() {
		return instance;
	}

	private ContractTable() {
	}

	@Override
	protected String getTableName() {
		return NAME;
	}

	@Override
	protected Collection<String> getFields() {
		Collection<String> collection = new ArrayList<String>(super.getFields());
		collection.add(Fields.COMPANY);
		collection.add(Fields.MY_COMPANY);
		return collection;
	}

	@Override
	public Cursor list() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param company
	 * @param myCompany
	 * @return Список объетов.
	 */
	public Cursor list(String company, String myCompany) {
		return filter(
				Fields.COMPANY + " = ? AND " + Fields.MY_COMPANY + " = ?",
				new String[] { company, myCompany }, null);
	}

	/**
	 * @param id
	 * @param company
	 * @param myCompany
	 * @return Относится ли контракт к указанному контрагенту и организации.
	 */
	public boolean acceptable(String id, String company, String myCompany) {
		ContentValues values;
		try {
			values = getById(id);
		} catch (BaseDatabaseException e) {
			return false;
		}
		return values.getAsString(Fields.COMPANY).equals(company)
				&& values.getAsString(Fields.MY_COMPANY).equals(myCompany);
	}

	@Override
	public void add(String id, String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Добавить элемент.
	 * 
	 * @param id
	 * @param name
	 * @param company
	 * @param myCompany
	 */
	public void add(String id, String name, String company, String myCompany) {
		ContentValues values = getValues(id, name);
		values.put(Fields.COMPANY, company);
		values.put(Fields.MY_COMPANY, myCompany);
		add(values);
	}
}
