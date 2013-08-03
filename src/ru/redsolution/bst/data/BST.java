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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.iharder.base64.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.parse.CompaniesImporter;
import ru.redsolution.bst.data.parse.ContainerImporter;
import ru.redsolution.bst.data.parse.ContractsImporter;
import ru.redsolution.bst.data.parse.DocumentImporter;
import ru.redsolution.bst.data.parse.GoodFoldersImporter;
import ru.redsolution.bst.data.parse.GoodsImporter;
import ru.redsolution.bst.data.parse.MyCompaniesImporter;
import ru.redsolution.bst.data.parse.PriceTypesImporter;
import ru.redsolution.bst.data.parse.ProjectsImporter;
import ru.redsolution.bst.data.parse.UomsImporter;
import ru.redsolution.bst.data.parse.WarehousesImporter;
import ru.redsolution.bst.data.serializer.BaseSerializer;
import ru.redsolution.bst.data.serializer.DemandSerializer;
import ru.redsolution.bst.data.serializer.InventorySerializer;
import ru.redsolution.bst.data.serializer.MoveSerializer;
import ru.redsolution.bst.data.serializer.SupplySerializer;
import ru.redsolution.bst.data.table.CompanyTable;
import ru.redsolution.bst.data.table.ContractTable;
import ru.redsolution.bst.data.table.CustomGoodTable;
import ru.redsolution.bst.data.table.DatabaseHelper;
import ru.redsolution.bst.data.table.GoodBarcodeTable;
import ru.redsolution.bst.data.table.GoodFolderTable;
import ru.redsolution.bst.data.table.GoodTable;
import ru.redsolution.bst.data.table.MyCompanyTable;
import ru.redsolution.bst.data.table.NewGoodBarcodeTable;
import ru.redsolution.bst.data.table.PriceTable;
import ru.redsolution.bst.data.table.PriceTypeTable;
import ru.redsolution.bst.data.table.ProjectTable;
import ru.redsolution.bst.data.table.SelectedGoodTable;
import ru.redsolution.bst.data.table.UomTable;
import ru.redsolution.bst.data.table.WarehouseTable;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Environment;
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
			+ "/exchange/rest/ms/xml/%s/list?start=%d&count=%d";
	private static final String INVENTORY_URL = HOST_URL
			+ "/exchange/rest/ms/xml/Inventory";

	private static final int ELEMENTS_IN_REQUEST = 200;

	private static BST instance;

	public static BST getInstance() {
		if (instance == null)
			throw new IllegalStateException();
		return instance;
	}

	private SharedPreferences settings;
	private ImportTask importTask;
	private SendTask sendTask;
	private OperationListener operationListener;

	private final AbstractHttpClient httpClient;

	private boolean wellcomeScreenShown;

	private final Object lock;

	public BST() {
		super();
		if (instance != null)
			throw new IllegalStateException();
		instance = this;
		httpClient = new TrustedHttpClient(this);
		importTask = null;
		sendTask = null;
		operationListener = null;
		wellcomeScreenShown = false;
		lock = new Object();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		settings = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		// Singletons
		DatabaseHelper.getInstance();
		CompanyTable.getInstance();
		GoodTable.getInstance();
		GoodFolderTable.getInstance();
		GoodBarcodeTable.getInstance();
		MyCompanyTable.getInstance();
		UomTable.getInstance();
		WarehouseTable.getInstance();
		ProjectTable.getInstance();
		ContractTable.getInstance();
		SelectedGoodTable.getInstance();
		NewGoodBarcodeTable.getInstance();
		CustomGoodTable.getInstance();
		PriceTypeTable.getInstance();
		PriceTable.getInstance();
	}

	public boolean showWellcomeScreen() {
		if (wellcomeScreenShown)
			return false;
		wellcomeScreenShown = true;
		return true;
	}

	public void setOperationListener(OperationListener operationListener) {
		this.operationListener = operationListener;
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
		if (response.getStatusLine().getStatusCode() != 200) {
			if (Debugger.ENABLED) {
				HttpEntity entity = response.getEntity();
				try {
					System.err.println(EntityUtils.toString(entity));
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						entity.consumeContent();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if (response.getStatusLine().getStatusCode() == 401
					|| response.getStatusLine().getStatusCode() == 403)
				throw new RuntimeException(new AuthenticationException(response
						.getStatusLine().toString()));
			else if (response.getStatusLine().getStatusCode() == 500)
				throw new RuntimeException(new InternalServerException(response
						.getStatusLine().toString()));
			else
				throw new RuntimeException(response.getStatusLine().toString());
		}
		return response;
	}

	/**
	 * @return Происходит ли сейчас импорт данных.
	 */
	public boolean isImporting() {
		return importTask != null;
	}

	/**
	 * Импорт данных.
	 */
	public void importData() {
		if (isImporting())
			return;
		importTask = new ImportTask();
		importTask.execute();
	}

	/**
	 * Отмена импорта данных.
	 */
	public void cancelImport() {
		if (!isImporting())
			return;
		importTask.cancel(true);
	}

	/**
	 * @return Происходит ли сейчас отправка данных.
	 */
	public boolean isSending() {
		return sendTask != null;
	}

	/**
	 * Отправка данных.
	 */
	public void sendData() {
		if (isSending())
			return;
		sendTask = new SendTask();
		sendTask.execute();
	}

	/**
	 * Отмена импорта данных.
	 */
	public void cancelSend() {
		if (!isSending())
			return;
		sendTask.cancel(true);
	}

	/**
	 * @return Производился ли импорт.
	 */
	public boolean isImported() {
		return settings.getBoolean(getString(R.string.imported_key), false);
	}

	/**
	 * @param resourceId
	 * @return Значение поля настройки.
	 */
	private String getValue(int resourceId) {
		return settings.getString(getString(resourceId), "");
	}

	/**
	 * Устанавливает значение поля настройки.
	 * 
	 * @param resourceId
	 * @param value
	 */
	private void setValue(int resourceId, String value) {
		Editor editor = settings.edit();
		editor.putString(getString(resourceId), value);
		editor.commit();
	}

	public String getLogin() {
		return getValue(R.string.login_key);
	}

	public String getPassword() {
		return getValue(R.string.password_key);
	}

	public void setLoginAndPassword(String login, String password) {
		Editor editor = settings.edit();
		editor.putString(getString(R.string.login_key), login);
		editor.putString(getString(R.string.password_key), password);
		editor.commit();
		httpClient.getCookieStore().clear();
	}

	public String getDefaultMyCompany() {
		return getValue(R.string.default_my_company_key);
	}

	public void setDefaultMyCompany(String value) {
		setValue(R.string.default_my_company_key, value);
	}

	public String getDefaultWarehouse() {
		return getValue(R.string.default_warehouse_key);
	}

	public void setDefaultWarehouse(String value) {
		setValue(R.string.default_warehouse_key, value);
	}

	public String getDefaultSupplyCompany() {
		return getValue(R.string.default_supply_company_key);
	}

	public void setDefaultSupplyCompany(String value) {
		setValue(R.string.default_supply_company_key, value);
	}

	public String getDefaultSupplyContract() {
		return getValue(R.string.default_supply_contract_key);
	}

	public void setDefaultSupplyContract(String value) {
		setValue(R.string.default_supply_contract_key, value);
	}

	public String getDefaultDemandCompany() {
		return getValue(R.string.default_demand_company_key);
	}

	public void setDefaultDemandCompany(String value) {
		setValue(R.string.default_demand_company_key, value);
	}

	public String getDefaultDemandContract() {
		return getValue(R.string.default_demand_contract_key);
	}

	public void setDefaultDemandContract(String value) {
		setValue(R.string.default_demand_contract_key, value);
	}

	public String getDefaultProject() {
		return getValue(R.string.default_project_key);
	}

	public void setDefaultProject(String value) {
		setValue(R.string.default_project_key, value);
	}

	public String getDefaultPriceType() {
		return getValue(R.string.default_price_type_key);
	}

	public void setDefaultPriceType(String value) {
		setValue(R.string.default_price_type_key, value);
	}

	/**
	 * @return Тип редактируемого документа. <code>null</code>, если тип не
	 *         выбран.
	 */
	public DocumentType getDocumentType() {
		try {
			return DocumentType.valueOf(getValue(R.string.document_type));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Устанавливает тип документа.
	 * 
	 * <b>Приводит к удалению списка выбранных товаров.</b>
	 * 
	 * @param documentType
	 *            Может быть <code>null</code>.
	 */
	public void setDocumentType(DocumentType documentType) {
		if (documentType == null)
			setValue(R.string.document_type, "");
		else
			setValue(R.string.document_type, documentType.name());
		SelectedGoodTable.getInstance().clear();
		NewGoodBarcodeTable.getInstance().clear();
		CustomGoodTable.getInstance().clear();
	}

	public String getSelectedMyCompany() {
		return getValue(R.string.selected_my_company_key);
	}

	public void setSelectedMyCompany(String value) {
		setValue(R.string.selected_my_company_key, value);
	}

	public String getSelectedWarehouse() {
		return getValue(R.string.selected_warehouse_key);
	}

	public void setSelectedWarehouse(String value) {
		setValue(R.string.selected_warehouse_key, value);
	}

	public String getSelectedTargetWarehouse() {
		return getValue(R.string.selected_target_warehouse_key);
	}

	public void setSelectedTargetWarehouse(String value) {
		setValue(R.string.selected_target_warehouse_key, value);
	}

	public String getSelectedCompany() {
		return getValue(R.string.selected_company_key);
	}

	public void setSelectedCompany(String value) {
		setValue(R.string.selected_company_key, value);
	}

	public String getSelectedContract() {
		return getValue(R.string.selected_contract_key);
	}

	public void setSelectedContract(String value) {
		setValue(R.string.selected_contract_key, value);
	}

	public String getSelectedProject() {
		return getValue(R.string.selected_project_key);
	}

	public void setSelectedProject(String value) {
		setValue(R.string.selected_project_key, value);
	}

	public String getSelectedPriceType() {
		return getValue(R.string.selected_price_type_key);
	}

	public void setSelectedPriceType(String value) {
		setValue(R.string.selected_price_type_key, value);
	}

	/**
	 * @return Отображать список товаров по папкам.
	 */
	public boolean isShowFolders() {
		return settings.getBoolean(getString(R.string.show_folders_key), false);
	}

	/**
	 * @param value
	 *            Отображать список товаров по папкам.
	 */
	public void setShowFolders(boolean value) {
		Editor editor = settings.edit();
		editor.putBoolean(getString(R.string.show_folders_key), value);
		editor.commit();
	}

	/**
	 * Абстрактная асинхронная задача, которая может завершиться успешно, либо
	 * вернуть ошибку.
	 * 
	 * @author alexander.ivanov
	 * 
	 */
	private abstract class AbstractTask<T> extends
			AsyncTask<Void, T, RuntimeException> {

		protected abstract void executeInBackground();

		@Override
		protected final RuntimeException doInBackground(Void... params) {
			try {
				executeInBackground();
			} catch (RuntimeException e) {
				return e;
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (operationListener != null)
				operationListener.onBegin();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			if (operationListener != null)
				operationListener.onCancelled();
		}

		@Override
		protected void onPostExecute(RuntimeException result) {
			super.onPostExecute(result);
			if (operationListener != null)
				if (result == null)
					operationListener.onDone();
				else {
					if (Debugger.ENABLED)
						result.printStackTrace();
					operationListener.onError(result);
				}
		}

	}

	/**
	 * Источник для импорта.
	 * 
	 * @author alexander.ivanov
	 */
	private static class ImportSource {

		/**
		 * Тип запроса.
		 */
		private final String requestType;

		/**
		 * ID ресурса с именем источника.
		 */
		private final int name;

		/**
		 * Парсер.
		 */
		private final ContainerImporter importer;

		public ImportSource(String requestType, int name,
				ContainerImporter importer) {
			super();
			this.requestType = requestType;
			this.name = name;
			this.importer = importer;
		}

		public String getRequestType() {
			return requestType;
		}

		public int getName() {
			return name;
		}

		public ContainerImporter getImporter() {
			return importer;
		}

	}

	/**
	 * Задача импорта данных.
	 * 
	 * @author alexander.ivanov
	 * 
	 */
	private class ImportTask extends AbstractTask<ImportProgress> {

		List<ImportSource> sources;

		public ImportTask() {
			super();
			sources = new ArrayList<ImportSource>();
			sources.add(new ImportSource("Uom", R.string.source_uom,
					new UomsImporter()));
			sources.add(new ImportSource("PriceType",
					R.string.source_price_type, new PriceTypesImporter()));
			sources.add(new ImportSource("MyCompany",
					R.string.source_my_company, new MyCompaniesImporter()));
			sources.add(new ImportSource("Warehouse",
					R.string.source_warehouse, new WarehousesImporter()));
			sources.add(new ImportSource("Project", R.string.source_project,
					new ProjectsImporter()));
			sources.add(new ImportSource("Contract", R.string.source_contract,
					new ContractsImporter()));
			sources.add(new ImportSource("Company", R.string.source_company,
					new CompaniesImporter()));
			sources.add(new ImportSource("GoodFolder",
					R.string.source_good_folder, new GoodFoldersImporter()));
			sources.add(new ImportSource("Good", R.string.source_good,
					new GoodsImporter()));
		}

		@Override
		protected void executeInBackground() {
			synchronized (lock) {
				CompanyTable.getInstance().clear();
				UomTable.getInstance().clear();
				GoodFolderTable.getInstance().clear();
				GoodTable.getInstance().clear();
				GoodBarcodeTable.getInstance().clear();
				MyCompanyTable.getInstance().clear();
				WarehouseTable.getInstance().clear();
				ProjectTable.getInstance().clear();
				ContractTable.getInstance().clear();
				PriceTypeTable.getInstance().clear();
				PriceTable.getInstance().clear();
				Editor editor = settings.edit();
				editor.putBoolean(getString(R.string.imported_key), false);
				editor.commit();
				for (int index = 0; index < sources.size(); index++)
					getData(index, sources.get(index));
				editor = settings.edit();
				editor.putBoolean(getString(R.string.imported_key), true);
				editor.commit();
			}
		}

		private void getData(int index, ImportSource importSource) {
			String type = importSource.getRequestType();
			ContainerImporter importer = importSource.getImporter();
			int maximum = -1;
			int start = 0;
			while (true) {
				if (isCancelled())
					throw new RuntimeException(new InterruptedException());
				publishProgress(new ImportProgress(importSource.getName(),
						index, sources.size(), start, maximum));
				String url = String.format(IMPORT_URL, type, start,
						ELEMENTS_IN_REQUEST);
				if (Debugger.ENABLED)
					System.out.println("Request: " + url);
				HttpResponse response = executeRequest(new HttpGet(url));
				if (Debugger.ENABLED)
					System.out.println("Response...");
				XmlPullParser parser;
				try {
					parser = XmlPullParserFactory.newInstance().newPullParser();
					Reader reader = new BufferedReader(new InputStreamReader(
							response.getEntity().getContent(), "UTF-8"));
					if (Debugger.ENABLED) {
						File dir = new File(
								Environment.getExternalStorageDirectory(),
								"bst");
						dir.mkdirs();
						File file = new File(dir, new Date().getTime() + ".xml");
						reader = new DebugReader(reader, file);
					}
					parser.setInput(reader);
				} catch (IllegalStateException e) {
					throw new RuntimeException(e);
				} catch (XmlPullParserException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (Debugger.ENABLED)
					System.out.println("Parse...");
				importer.resetCount();
				try {
					new DocumentImporter(importer).parse(parser);
				} catch (XmlPullParserException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (Debugger.ENABLED)
					System.out.println("Parsed: " + importer.getCount()
							+ ", total: " + importer.getTotal());
				if (importer.getTotal() != null)
					maximum = importer.getTotal();
				if (importer.getCount() < ELEMENTS_IN_REQUEST)
					break;
				start += ELEMENTS_IN_REQUEST;
			}
			if (Debugger.ENABLED)
				System.out.println(type + " done.");
		}

		@Override
		protected void onCancelled() {
			importTask = null;
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(RuntimeException result) {
			importTask = null;
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(ImportProgress... values) {
			super.onProgressUpdate(values);
			if (operationListener instanceof ImportOperationListener)
				((ImportOperationListener) operationListener)
						.onProgressUpdate(values[0]);
		}

	}

	/**
	 * Задача отправки документа.
	 * 
	 * @author alexander.ivanov
	 * 
	 */
	private class SendTask extends AbstractTask<Void> {

		@Override
		protected void executeInBackground() {
			HttpPut request = new HttpPut(INVENTORY_URL);
			BaseSerializer serializer;
			if (getDocumentType() == DocumentType.supply)
				serializer = new SupplySerializer();
			else if (getDocumentType() == DocumentType.inventory)
				serializer = new InventorySerializer();
			else if (getDocumentType() == DocumentType.demand)
				serializer = new DemandSerializer();
			else if (getDocumentType() == DocumentType.move)
				serializer = new MoveSerializer();
			else
				throw new UnsupportedOperationException();
			String body;
			try {
				body = serializer.getXml();
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalStateException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			StringEntity entity;
			try {
				entity = new StringEntity(body, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			entity.setContentType("application/xml");
			request.setEntity(entity);
			executeRequest(request);
			BST.getInstance().setDocumentType(null);
		}

		@Override
		protected void onCancelled() {
			sendTask = null;
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(RuntimeException result) {
			sendTask = null;
			super.onPostExecute(result);
		}

	}

}
