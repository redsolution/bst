package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.table.GoodTable;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;

public class ChooseActivity extends ListActivity implements OnItemClickListener {

	public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose);
		setListAdapter(new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, GoodTable.getInstance()
						.list(), new String[] { GoodTable.Fields.NAME,
						GoodTable.Fields.PRODUCT_CODE }, new int[] {
						android.R.id.text1, android.R.id.text2 }));
		getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		Cursor cursor = (Cursor) parent.getItemAtPosition(position);
		String productId = cursor.getString(cursor
				.getColumnIndex(GoodTable.Fields._ID));
		intent.putExtra(EXTRA_PRODUCT_ID, productId);
		setResult(RESULT_OK, intent);
		finish();
	}

}
