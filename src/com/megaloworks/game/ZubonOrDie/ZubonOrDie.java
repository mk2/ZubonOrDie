package com.megaloworks.game.ZubonOrDie;

import com.megaloworks.game.ZubonOrDie.R;
import com.megaloworks.game.ZubonOrDie.activity.ZubonOrDieGallery;
import com.megaloworks.game.ZubonOrDie.activity.ZubonOrDieQuestion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public final class ZubonOrDie extends Activity {

	public static final String TAG = ZubonOrDie.class.getName();

	public static final String EXTRA_QUESTION_NUMBER = "EXTRA_QUESTION_NUMBER";

	private static final String PREFKEY_ZUBON_POWER = "PREFRENCE_KEY_ZUBON_POWER";

	private static final String KEY_ZUBON_POWER = "KEY_ZUBON_POWER";

	private SharedPreferences m_pref;

	private SharedPreferences.Editor m_editor;

	private TextView m_zubonPowTxtVu;

	private long m_zubonPow;

	/**
	 * Listener class for Q*Buttons
	 * 
	 * @author trude
	 * 
	 */
	class QButtonOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v instanceof Button) {
				int id = v.getId();
				int qNum = 0;
				switch (id) {
				case R.id.Q3Button:
					qNum = 3;
					break;
				case R.id.Q5Button:
					qNum = 5;
					break;
				case R.id.Q10Button:
					qNum = 10;
					break;
				case R.id.QInfiniteButton:
					qNum = -1;
				default:
					break;
				}
				Intent intent = new Intent(ZubonOrDie.this,
						ZubonOrDieQuestion.class);
				intent.putExtra(EXTRA_QUESTION_NUMBER, qNum);
				startActivityForResult(intent, 0);
			}
		}

	}

	/**
	 * Listener class for GoGalleryButton
	 * 
	 * @author trude
	 * 
	 */
	class GoGalleryButtonOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ZubonOrDie.this, ZubonOrDieGallery.class);
			startActivity(intent);
		}

	}

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		m_pref = getSharedPreferences(PREFKEY_ZUBON_POWER, MODE_PRIVATE);
		m_editor = m_pref.edit();

		m_zubonPowTxtVu = (TextView) findViewById(R.id.TextViewZubonPower);
		m_zubonPow = m_pref.getLong(KEY_ZUBON_POWER, 0);
		m_zubonPowTxtVu.setText("" + m_zubonPow);

		Button q3Btn = (Button) findViewById(R.id.Q3Button);
		Button q5Btn = (Button) findViewById(R.id.Q5Button);
		Button q10Btn = (Button) findViewById(R.id.Q10Button);
		Button qinfBtn = (Button) findViewById(R.id.QInfiniteButton);
		q3Btn.setOnClickListener(new QButtonOnClickListener());
		q5Btn.setOnClickListener(new QButtonOnClickListener());
		q10Btn.setOnClickListener(new QButtonOnClickListener());
		qinfBtn.setOnClickListener(new QButtonOnClickListener());

		Button goGalleryBtn = (Button) findViewById(R.id.GoGalleryButton);
		goGalleryBtn.setOnClickListener(new GoGalleryButtonOnClickListener());
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			SharedPreferences pref = getSharedPreferences(
					ZubonOrDieQuestion.PREFKEY_ZOD_RESULT, MODE_PRIVATE);
			long averageTime = pref.getLong(
					ZubonOrDieQuestion.KEY_AVERAGE_TIME, 1);
			Log.v(TAG, "AverageTime: " + averageTime);
			boolean isGameCleared = pref.getBoolean(
					ZubonOrDieQuestion.KEY_GAME_CLEARED, false);
			int passedQNum = pref.getInt(
					ZubonOrDieQuestion.KEY_PASSED_QUESTION_NUMBER, 1);

			Log.v(TAG, "isGameCleard: " + isGameCleared);
			Log.v(TAG, "passwdQNum: " + passedQNum);

			if (isGameCleared && passedQNum > 0) {
				double zubonPower = (passedQNum * 1000000) / (averageTime);
				Log.v(TAG, "zubonPower: " + zubonPower);
				m_zubonPowTxtVu.setText("" + zubonPower);
			}
		}
	}
}