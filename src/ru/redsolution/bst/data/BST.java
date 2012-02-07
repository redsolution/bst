package ru.redsolution.bst.data;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.tables.CompanyFolderTable;
import ru.redsolution.bst.data.tables.CompanyTable;
import ru.redsolution.bst.data.tables.DatabaseHelper;
import ru.redsolution.bst.data.tables.GoodBarcodeTable;
import ru.redsolution.bst.data.tables.GoodFolderTable;
import ru.redsolution.bst.data.tables.GoodTable;
import ru.redsolution.bst.data.tables.MyCompanyTable;
import ru.redsolution.bst.data.tables.UomTable;
import ru.redsolution.bst.data.tables.WarehouseTable;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Класс приложения.
 * 
 * @author alexander.ivanov
 * 
 */
public class BST extends Application {

	private static final String IMPORT_URL = "https://online.moysklad.ru/exchange/xml/export?name=Dictionary";

	private static BST instance;

	public static BST getInstance() {
		if (instance == null)
			throw new IllegalStateException();
		return instance;
	}

	private SharedPreferences settings;

	private String warehouse;
	private String myCompany;

	private final TrustedHttpClient httpClient;

	public BST() {
		super();
		if (instance != null)
			throw new IllegalStateException();
		instance = this;
		warehouse = "";
		myCompany = "";
		httpClient = new TrustedHttpClient(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		settings = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		DatabaseHelper.getInstance();
		CompanyFolderTable.getInstance();
		CompanyTable.getInstance();
		GoodTable.getInstance();
		GoodFolderTable.getInstance();
		GoodBarcodeTable.getInstance();
		MyCompanyTable.getInstance();
		UomTable.getInstance();
		WarehouseTable.getInstance();
	}

	/**
	 * Импорт данных.
	 */
	public void importData() {
		DatabaseHelper.getInstance().clear();

		WarehouseTable.getInstance().add("z", "Основной склад", "");
		MyCompanyTable.getInstance().add("a", "Моя организация", "");
		MyCompanyTable.getInstance().add("b", "Ещё одна организация", "");
		// HttpGet get = new HttpGet(IMPORT_URL);
		Editor editor = settings.edit();
		editor.putBoolean(getString(R.string.imported_key), true);
		editor.commit();
	}

	public boolean isImported() {
		return settings.getBoolean(getString(R.string.imported_key), false);
	}

	public String getLogin() {
		return settings.getString(getString(R.string.login_key), "");
	}

	public String getPassword() {
		return settings.getString(getString(R.string.password_key), "");
	}

	public void setLoginAndPassword(String login, String password) {
		Editor editor = settings.edit();
		editor.putString(getString(R.string.login_key), login);
		editor.putString(getString(R.string.password_key), password);
		editor.commit();
	}

	public String getDefaultWarehouse() {
		return settings.getString(getString(R.string.warehouse_key), "");
	}

	public void setDefaultWarehouse(String value) {
		Editor editor = settings.edit();
		editor.putString(getString(R.string.warehouse_key), value);
		editor.commit();
	}

	public String getDefaultMyCompany() {
		return settings.getString(getString(R.string.my_company_key), "");
	}

	public void setDefaultMyCompany(String value) {
		Editor editor = settings.edit();
		editor.putString(getString(R.string.my_company_key), value);
		editor.commit();
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getMyCompany() {
		return myCompany;
	}

	public void setMyCompany(String myCompany) {
		this.myCompany = myCompany;
	}

}
