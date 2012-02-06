package ru.redsolution.bst.data;

import java.util.Random;

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
import android.preference.PreferenceManager;

/**
 * Класс приложения.
 * 
 * @author alexander.ivanov
 * 
 */
public class BST extends Application {

	private static BST instance;

	public static BST getInstance() {
		if (instance == null)
			throw new IllegalStateException();
		return instance;
	}

	private final Random random = new Random();
	private SharedPreferences settings;

	public BST() {
		super();
		if (instance != null)
			throw new IllegalStateException();
		instance = this;
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
	}

	public String getLogin() {
		return settings.getString(getString(R.string.login_key), "");
	}

	public String getPassword() {
		return settings.getString(getString(R.string.password_key), "");
	}

}
