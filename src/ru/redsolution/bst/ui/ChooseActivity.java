package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.table.GoodTable;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

public class ChooseActivity extends ListActivity implements OnItemClickListener {

	public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose);
		final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, GoodTable.getInstance()
						.list(), new String[] { GoodTable.Fields.NAME,
						GoodTable.Fields.PRODUCT_CODE }, new int[] {
						android.R.id.text1, android.R.id.text2 });
		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			@Override
			public Cursor runQuery(CharSequence constraint) {
				Cursor cursor = GoodTable.getInstance().filterByText(
						constraint.toString());
				startManagingCursor(cursor);
				return cursor;
			}
		});
		setListAdapter(adapter);
		EditText filterText = (EditText) findViewById(R.id.search);
		filterText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				adapter.getFilter().filter(s);
			}

		});
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
