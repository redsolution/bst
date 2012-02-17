package ru.redsolution.bst.data.serializer;

import java.io.IOException;
import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.GoodTable;
import ru.redsolution.bst.data.table.SelectedGoodTable;
import ru.redsolution.bst.data.table.SelectedProductCodeForBarcodeTable;
import ru.redsolution.bst.data.table.SelectedProductIdForBarcodeTable;
import android.database.Cursor;
import android.util.Xml;

/**
 * Базовый класс для сериализации данных для отправки.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class BaseSerializer {

	private static final String DESCRIPTION_TAG = "description";

	/**
	 * @return XML для отправки.
	 */
	public String getXml() throws IllegalArgumentException,
			IllegalStateException, IOException {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer);
		serializer.startDocument("UTF-8", true);
		serializer.startTag("", getContainerName());
		renderContainerAttrs(serializer);
		renderContainerBody(serializer);
		serializer.endTag("", getContainerName());
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * @return Имя контейнера.
	 */
	protected abstract String getContainerName();

	/**
	 * Задаёт атрибуты для контейнера.
	 * 
	 * @param serializer
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected abstract void renderContainerAttrs(XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException;

	/**
	 * Задаёт контент для контейнера.
	 * 
	 * @param serializer
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected void renderContainerBody(XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", DESCRIPTION_TAG);
		Cursor cursor = SelectedProductCodeForBarcodeTable.getInstance().list();
		boolean hasNewBarcode = false;
		try {
			if (cursor.moveToFirst()) {
				hasNewBarcode = true;
				serializer.text(BST.getInstance().getString(
						R.string.new_barcode_description));
				serializer.text(BST.getInstance().getString(
						R.string.new_product_code_for_barcode));
				do {
					serializer
							.text(cursor.getString(cursor
									.getColumnIndex(SelectedProductCodeForBarcodeTable.Fields.VALUE)));
					serializer.text(" ");
					serializer
							.text(cursor.getString(cursor
									.getColumnIndex(SelectedProductCodeForBarcodeTable.Fields.TYPE)));
					serializer.text(" ");
					serializer
							.text(cursor.getString(cursor
									.getColumnIndex(SelectedProductCodeForBarcodeTable.Fields._ID)));
					serializer.text("\n");
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}

		cursor = SelectedProductIdForBarcodeTable.getInstance().list();
		try {
			if (cursor.moveToFirst()) {
				if (!hasNewBarcode)
					serializer.text(BST.getInstance().getString(
							R.string.new_barcode_description));
				serializer.text(BST.getInstance().getString(
						R.string.new_product_name_for_barcode));
				do {
					String id = cursor
							.getString(cursor
									.getColumnIndex(SelectedProductCodeForBarcodeTable.Fields._ID));
					String name;
					try {
						name = GoodTable.getInstance().getName(id);
					} catch (BaseDatabaseException e) {
						continue;
					}
					serializer
							.text(cursor.getString(cursor
									.getColumnIndex(SelectedProductCodeForBarcodeTable.Fields.VALUE)));
					serializer.text(" ");
					serializer
							.text(cursor.getString(cursor
									.getColumnIndex(SelectedProductCodeForBarcodeTable.Fields.TYPE)));
					serializer.text(" ");
					serializer.text(name);
					serializer.text("\n");
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}

		serializer.endTag("", DESCRIPTION_TAG);
		cursor = getCursor();
		try {
			if (cursor.moveToFirst()) {
				do {
					serializer.startTag("", getItemName());
					renderItemAttrs(serializer, cursor);
					renderItemBody(serializer, cursor);
					serializer.endTag("", getItemName());
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
	}

	/**
	 * @return Имя элемента.
	 */
	protected abstract String getItemName();

	/**
	 * Задаёт атрибуты для элемента.
	 * 
	 * @param serializer
	 * @param cursor
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected abstract void renderItemAttrs(XmlSerializer serializer,
			Cursor cursor) throws IllegalArgumentException,
			IllegalStateException, IOException;

	/**
	 * Задаёт контент для элемента.
	 * 
	 * @param serializer
	 * @param cursor
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected void renderItemBody(XmlSerializer serializer, Cursor cursor)
			throws IllegalArgumentException, IllegalStateException, IOException {

	}

	/**
	 * @return Курсор с объектами.
	 */
	protected Cursor getCursor() {
		return SelectedGoodTable.getInstance().list();
	}

	/**
	 * Записывает тег с текстовым значением.
	 * 
	 * @param serializer
	 * @param tag
	 * @param text
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected void renderTag(XmlSerializer serializer, String tag, String text)
			throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", tag);
		serializer.text(text);
		serializer.endTag("", tag);
	}

}
