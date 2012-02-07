package ru.redsolution.bst.data;

import java.io.IOException;

import net.iharder.base64.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.parse.DocumentImporter;
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

	private static final String HOST_URL = "https://online.moysklad.ru";
	private static final String IMPORT_URL = HOST_URL
			+ "/exchange/xml/export?name=Dictionary";

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
	 * Отправляет запрос, добавляя информацию для авторизации.
	 * 
	 * @param request
	 * @return
	 */
	private HttpResponse executeRequest(HttpUriRequest request) {
		request.setHeader(
				"Authorization",
				"Basic "
						+ Base64.encodeBytes((getLogin() + ":" + getPassword())
								.getBytes()));
		HttpResponse response;
		try {
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (response.getStatusLine().getStatusCode() != 200)
			throw new RuntimeException(new AuthenticationException(response
					.getStatusLine().toString()));
		return response;
	}

	/**
	 * Проверка авторизации.
	 * 
	 * @return
	 */
	public boolean checkAuth() {
		try {
			executeRequest(new HttpGet(HOST_URL));
		} catch (RuntimeException e) {
			if (e.getCause() instanceof AuthenticationException)
				return false;
			throw new RuntimeException(e);
		}
		return true;
	}

	/**
	 * Импорт данных.
	 */
	public void importData() {
		DatabaseHelper.getInstance().clear();
		HttpResponse response = executeRequest(new HttpGet(IMPORT_URL));
		XmlPullParser parser;
		try {
			parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(response.getEntity().getContent(), "UTF-8");
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (XmlPullParserException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			new DocumentImporter().parse(parser);
		} catch (XmlPullParserException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

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
