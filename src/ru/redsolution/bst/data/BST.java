package ru.redsolution.bst.data;

import java.util.Random;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.tables.CompanyTable;
import ru.redsolution.bst.data.tables.DatabaseHelper;
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
		WarehouseTable.getInstance();
		CompanyTable.getInstance();
	}

	/**
	 * Импорт данных.
	 */
	public void importData() {
		DatabaseHelper.getInstance().clear();

		WarehouseTable.getInstance()
				.add("1", "Основной склад #" + random.nextInt());
		CompanyTable.getInstance().add("1",
				"Моя организация #" + random.nextInt());
		CompanyTable.getInstance().add("2",
				"Ещё одна организация #" + random.nextInt());
	}

	public String getLogin() {
		return settings.getString(getString(R.string.login_key), "");
	}

	public String getPassword() {
		return settings.getString(getString(R.string.password_key), "");
	}

}
