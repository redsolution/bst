/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quietlycoding.android.picker;

import java.math.BigDecimal;

import ru.redsolution.bst.R;
import android.content.Context;
import android.os.Handler;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This class has been pulled from the Android platform source code, its an
 * internal widget that hasn't been made public so its included in the project
 * in this fashion for use with the preferences screen; I have made a few slight
 * modifications to the code here, I simply put a MAX and MIN default in the
 * code but these values can still be set publically by calling code.
 * 
 * @author Google
 */
public class NumberPicker extends LinearLayout implements OnClickListener,
		OnFocusChangeListener, OnLongClickListener {

	public interface OnChangedListener {
		void onChanged(NumberPicker picker, BigDecimal oldVal, BigDecimal newVal);
	}

	private final Handler mHandler;
	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			if (mIncrement) {
				changeCurrent(mCurrent.add(BigDecimal.ONE));
				mHandler.postDelayed(this, mSpeed);
			} else if (mDecrement) {
				changeCurrent(mCurrent.add(BigDecimal.ONE.negate()));
				mHandler.postDelayed(this, mSpeed);
			}
		}
	};

	private final EditText mText;

	protected BigDecimal mCurrent;
	protected BigDecimal mPrevious;
	private OnChangedListener mListener;
	private long mSpeed = 300;

	private boolean mIncrement;
	private boolean mDecrement;

	public NumberPicker(Context context) {
		this(context, null);
	}

	public NumberPicker(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NumberPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		setOrientation(VERTICAL);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.number_picker, this, true);
		mHandler = new Handler();
		mIncrementButton = (NumberPickerButton) findViewById(R.id.increment);
		mIncrementButton.setOnClickListener(this);
		mIncrementButton.setOnLongClickListener(this);
		mIncrementButton.setNumberPicker(this);
		mDecrementButton = (NumberPickerButton) findViewById(R.id.decrement);
		mDecrementButton.setOnClickListener(this);
		mDecrementButton.setOnLongClickListener(this);
		mDecrementButton.setNumberPicker(this);

		mText = (EditText) findViewById(R.id.timepicker_input);
		mText.setOnFocusChangeListener(this);
		DigitsKeyListener keyListener = DigitsKeyListener.getInstance(false,
				true);
		mText.setFilters(new InputFilter[] { keyListener });
		mText.setRawInputType(keyListener.getInputType());

		if (!isEnabled()) {
			setEnabled(false);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		mIncrementButton.setEnabled(enabled);
		mDecrementButton.setEnabled(enabled);
		mText.setEnabled(enabled);
	}

	public void setOnChangeListener(OnChangedListener listener) {
		mListener = listener;
	}

	public void setCurrent(BigDecimal current) {
		mCurrent = current;
		updateView();
	}

	/**
	 * The speed (in milliseconds) at which the numbers will scroll when the the
	 * +/- buttons are longpressed. Default is 300ms.
	 */
	public void setSpeed(long speed) {
		mSpeed = speed;
	}

	@Override
	public void onClick(View v) {
		validateInput(mText);
		if (!mText.hasFocus())
			mText.requestFocus();

		// now perform the increment/decrement
		if (R.id.increment == v.getId()) {
			changeCurrent(mCurrent.add(BigDecimal.ONE));
		} else if (R.id.decrement == v.getId()) {
			changeCurrent(mCurrent.add(BigDecimal.ONE.negate()));
		}
	}

	protected void changeCurrent(BigDecimal current) {

		mPrevious = mCurrent;
		mCurrent = current;

		notifyChange();
		updateView();
	}

	protected void notifyChange() {
		if (mListener != null) {
			mListener.onChanged(this, mPrevious, mCurrent);
		}
	}

	protected void updateView() {

		mText.setText(String.valueOf(mCurrent));
		mText.setSelection(mText.getText().length());
	}

	private void validateCurrentView(CharSequence str) {
		BigDecimal val = new BigDecimal(str.toString());
		if (!mCurrent.equals(val)) {
			mPrevious = mCurrent;
			mCurrent = val;
			notifyChange();
		}
		updateView();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		/*
		 * When focus is lost check that the text field has valid values.
		 */
		if (!hasFocus) {
			validateInput(v);
		}
	}

	private void validateInput(View v) {
		String str = String.valueOf(((TextView) v).getText());
		if ("".equals(str)) {

			// Restore to the old value as we don't allow empty values
			updateView();
		} else {

			// Check the new value and ensure it's in range
			validateCurrentView(str);
		}
	}

	/**
	 * We start the long click here but rely on the {@link NumberPickerButton}
	 * to inform us when the long click has ended.
	 */
	@Override
	public boolean onLongClick(View v) {

		/*
		 * The text view may still have focus so clear it's focus which will
		 * trigger the on focus changed and any typed values to be pulled.
		 */
		mText.clearFocus();

		if (R.id.increment == v.getId()) {
			mIncrement = true;
			mHandler.post(mRunnable);
		} else if (R.id.decrement == v.getId()) {
			mDecrement = true;
			mHandler.post(mRunnable);
		}
		return true;
	}

	public void cancelIncrement() {
		mIncrement = false;
	}

	public void cancelDecrement() {
		mDecrement = false;
	}

	private final NumberPickerButton mIncrementButton;
	private final NumberPickerButton mDecrementButton;

	/**
	 * @return the current value.
	 */
	public BigDecimal getCurrent() {
		validateInput(mText);
		return mCurrent;
	}
}
