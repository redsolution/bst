package ru.redsolution.bst.data.serializer;

import java.io.IOException;
import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.GoodTable;
import ru.redsolution.bst.data.table.NewGoodBarcodeTable;
import ru.redsolution.bst.data.table.SelectedGoodTable;
import android.content.ContentValues;
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
		Cursor cursor = NewGoodBarcodeTable.getInstance().list();
		try {
			if (cursor.moveToFirst()) {
				serializer.text(BST.getInstance().getString(
						R.string.new_barcode_description));
				do {
					ContentValues values = NewGoodBarcodeTable.getInstance()
							.getValues(cursor);
					String id = values
							.getAsString(NewGoodBarcodeTable.Fields._ID);
					ContentValues good;
					try {
							good = GoodTable.getInstance().getById(id);
					} catch (BaseDatabaseException e) {
						continue;
					}
					serializer.text(values
							.getAsString(NewGoodBarcodeTable.Fields.VALUE));
					serializer.text(" ");
					serializer.text(values
							.getAsString(NewGoodBarcodeTable.Fields.TYPE));
					serializer.text(" ");
					String value = good
							.getAsString(BaseGoodTable.Fields.PRODUCT_CODE);
					if ("".equals(value))
						value = good.getAsString(BaseGoodTable.Fields.NAME);
					serializer.text(value);
					serializer.text("\n");
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		serializer.endTag("", DESCRIPTION_TAG);
		cursor = SelectedGoodTable.getInstance().listCustom(false);
		try {
			if (cursor.moveToFirst()) {
				do {
					serializer.startTag("", getItemName());
					ContentValues values = SelectedGoodTable.getInstance()
							.getValues(cursor);
					renderItemAttrs(serializer, values);
					renderItemBody(serializer, values);
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
	protected void renderItemAttrs(XmlSerializer serializer,
			ContentValues values) throws IllegalArgumentException,
			IllegalStateException, IOException {
		String good = values.getAsString(SelectedGoodTable.Fields._ID);
		int quantity = values.getAsInteger(SelectedGoodTable.Fields.QUANTITY);
		serializer.attribute("", "quantity", String.valueOf(quantity));
		serializer.attribute("", "goodId", good);
	}

	/**
	 * Задаёт контент для элемента.
	 * 
	 * @param serializer
	 * @param cursor
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected void renderItemBody(XmlSerializer serializer, ContentValues values)
			throws IllegalArgumentException, IllegalStateException, IOException {
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
