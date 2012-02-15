package ru.redsolution.bst.data.serializer;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import ru.redsolution.bst.data.table.BaseDatabaseException;
import ru.redsolution.bst.data.table.GoodTable;
import ru.redsolution.bst.data.table.SelectedTable;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * Класс для сериализации данных и учета стоимости ТМЦ в документе.
 * 
 * @author alexander.ivanov
 * 
 */
public abstract class PricedSerializer extends BaseSerializer {

	private static final String PRICE_TAG = "price";
	private static final String BASE_PRICE_TAG = "basePrice";

	/**
	 * @return Имя поля со значением цены в таблице товаров.
	 */
	protected abstract String getPriceFieldName();

	@Override
	protected void renderItemBody(XmlSerializer serializer, Cursor cursor)
			throws IllegalArgumentException, IllegalStateException, IOException {
		super.renderItemBody(serializer, cursor);
		String good = cursor.getString(cursor
				.getColumnIndex(SelectedTable.Fields._ID));
		ContentValues values;
		try {
			values = GoodTable.getInstance().getById(good);
		} catch (BaseDatabaseException e) {
			return;
		}
		String price = values.getAsString(getPriceFieldName());
		if ("".equals(price))
			return;
		serializer.startTag("", BASE_PRICE_TAG);
		serializer.attribute("", "sumInCurrency", price);
		serializer.attribute("", "sum", price);
		serializer.endTag("", BASE_PRICE_TAG);
		serializer.startTag("", PRICE_TAG);
		serializer.attribute("", "sumInCurrency", price);
		serializer.attribute("", "sum", price);
		serializer.endTag("", PRICE_TAG);
	}

}
