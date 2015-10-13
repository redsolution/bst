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
package ru.redsolution.bst.ui;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.Debugger;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * О программе.
 * 
 * @author alexander.ivanov
 * 
 */
public class AboutActivity extends Activity implements OnClickListener {

	private static final long HIDER_DELAY = 3000;

	public static final String ACTION_WELLCOME_SCREEN = "WELLCOME_SCREEN";

	private static final String REDSOLUTION_URL = "http://www.redsolution.ru/nashi-proekty/moj-sklad";

	private final Runnable hider = new Runnable() {

		@Override
		public void run() {
			finish();
		}

	};

	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		handler = new Handler();
		findViewById(R.id.root).setOnClickListener(this);
		findViewById(R.id.redsolution).setOnClickListener(this);
		findViewById(R.id.debug_enabled).setVisibility(
				Debugger.ENABLED ? View.VISIBLE : View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ACTION_WELLCOME_SCREEN.equals(getIntent().getAction()))
			handler.postDelayed(hider, HIDER_DELAY);
	}

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(hider);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.root:
			finish();
			break;
		case R.id.redsolution:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse(REDSOLUTION_URL)));
			break;
		default:
			break;
		}

	}

}
