package com.megaloworks.game.ZubonOrDie.activity;

import java.util.ArrayList;

import com.megaloworks.game.ZubonOrDie.R;
import com.megaloworks.game.ZubonOrDie.ZubonOrDie;
import com.megaloworks.game.ZubonOrDie.support.ZubonEntityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author tsukuyomi
 * 
 */
public final class ZubonOrDieQuestion extends Activity {

	private static final String TAG = ZubonOrDieQuestion.class.getName();

	public static final String PREFKEY_ZOD_RESULT = "PREFRENCE_KEY_ZUBON_OR_DIE_QUESTION_RESULT";

	public static final String KEY_GAME_CLEARED = "EXTRA_GAME_CLEARED";

	public static final String KEY_PASSED_QUESTION_NUMBER = "EXTRA_PASSED_QUESTION_NUMBER";

	public static final String KEY_GAME_STATE = "EXTRA_GAME_STATE";

	public static final String KEY_AVERAGE_TIME = "EXTRA_AVERAGE_TIME";

	public static final int GAME_STATE_SUCCESS = 0x100;

	public static final int GAME_STATE_PLAYING = 0x101;

	public static final int GAME_STATE_FAILURE = 0x102;

	public static final long DEFFAULT_REMAIN_TIME = 10l;

	class ZubonOnTouchAnimationListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			whenClickedVuAnimEnd();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

	}

	class ImageViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View vu) {
			Log.v(TAG, "Button clicked.");
			if (vu instanceof ImageView) {
				m_clickedVu = (ImageView) vu;
				vu.startAnimation(m_zubonOnTouchAnim);
			}
		}

	}

	private ZubonEntityManager m_zem;

	/**
	 * Question number
	 */
	private int m_qNum;

	/**
	 * question number passed by a player.
	 */
	private int m_passedQNum;

	private int m_gameState;

	private int m_numZubons;

	private int m_numPantss;

	private int m_zubonsIndex;

	private int m_pantssIndex;

	private ArrayList<Long> m_elapsedTimes;

	private long m_prevElapsedTime;

	/**
	 * correct answer is corresponding to a imageView.<br>
	 * so, the correctVuId has the imagebutton's id being connected to correct
	 * image.
	 */
	private int m_correctVuId;

	/**
	 * Top ImageView
	 */
	private ImageView m_topImgVu;

	/**
	 * Bottom ImageView
	 */
	private ImageView m_btmImgVu;

	private ImageView m_clickedVu;

	private Animation m_zubonOnTouchAnim;

	private TextView m_resultTxtVu;

	private Chronometer m_elapsedTimeMeter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Question is started.");
		setContentView(R.layout.question);

		m_zem = ZubonEntityManager.getInstance(this);
		m_numZubons = m_zem.getNumZubons();
		m_numPantss = m_zem.getNumPantss();

		// initialize
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			m_qNum = extras.getInt(ZubonOrDie.EXTRA_QUESTION_NUMBER);
		}
		m_passedQNum = 0;
		m_gameState = GAME_STATE_PLAYING;
		m_topImgVu = (ImageView) findViewById(R.id.ImageViewTop);
		m_btmImgVu = (ImageView) findViewById(R.id.ImageViewBottom);
		m_topImgVu.setOnClickListener(new ImageViewOnClickListener());
		m_btmImgVu.setOnClickListener(new ImageViewOnClickListener());
		m_zubonOnTouchAnim = AnimationUtils.loadAnimation(this,
				R.anim.zubon_ontouch_anim);
		m_zubonOnTouchAnim
				.setAnimationListener(new ZubonOnTouchAnimationListener());
		m_resultTxtVu = (TextView) findViewById(R.id.TextViewResult);
		m_resultTxtVu.setText(R.string.TextViewResultVacant);
		m_prevElapsedTime = SystemClock.elapsedRealtime();
		m_elapsedTimeMeter = (Chronometer) findViewById(R.id.ElapsedTimeMeter);
		m_elapsedTimes = new ArrayList<Long>();
		updateCurrentIds();
		updateViews();
		updateElapsedTimeMeter();
	}

	private void updateCurrentIds() {
		int prevZubonsIndex = m_zubonsIndex;
		int prevPantssIndex = m_pantssIndex;
		int nextZubonsIndex = -1;
		int nextPantssIndex = -1;

		do {
			nextPantssIndex = (int) (Math.random() * 1000) % m_numPantss;
		} while (nextPantssIndex == prevPantssIndex);

		do {
			nextZubonsIndex = (int) (Math.random() * 1000) % m_numZubons;
		} while (nextZubonsIndex == prevZubonsIndex);

		m_zubonsIndex = nextZubonsIndex;
		m_pantssIndex = nextPantssIndex;

		Log.v(TAG, "pants index: " + m_pantssIndex);
		Log.v(TAG, "zubon index: " + m_zubonsIndex);
	}

	private void updateViews() {
		if (((int) (Math.random() * 10) % 2) == 0) {
			setImagesOnViews(m_zubonsIndex, m_pantssIndex, true);
		} else {
			setImagesOnViews(m_pantssIndex, m_zubonsIndex, false);
		}
	}

	private void updateElapsedTimeMeter() {
		m_elapsedTimeMeter.stop();
		m_elapsedTimeMeter.setBase(SystemClock.elapsedRealtime());
		m_elapsedTimeMeter.start();
	}

	private void whenOneQuestionEnd() {
		long currentElapsedTime = SystemClock.elapsedRealtime();
		m_elapsedTimes.add(currentElapsedTime - m_prevElapsedTime);

		m_prevElapsedTime = currentElapsedTime;
	}

	/**
	 * @param id1
	 *            topVu's zubon id
	 * @param id2
	 *            btmVu's zubon id
	 * @param isId1IsZubon
	 *            If id1 is a kind of zubon, give true as argument.
	 */
	private void setImagesOnViews(int id1, int id2, boolean isId1IsZubon) {

		int topVuImgResId = -1;
		int bottomVuImgResId = -1;

		if (isId1IsZubon) {
			topVuImgResId = m_zem.getZEntity(id1).imgId;
			bottomVuImgResId = m_zem.getPEntity(id2).imgId;
			m_correctVuId = m_topImgVu.getId();
		} else {
			topVuImgResId = m_zem.getPEntity(id1).imgId;
			bottomVuImgResId = m_zem.getZEntity(id2).imgId;
			m_correctVuId = m_btmImgVu.getId();
		}

		m_topImgVu.setImageResource(topVuImgResId);
		m_btmImgVu.setImageResource(bottomVuImgResId);
	}

	private void whenClickedVuAnimEnd() {
		updateElapsedTimeMeter();
		whenOneQuestionEnd();
		ImageView iv = (ImageView) m_clickedVu;
		int vuId = iv.getId();
		if (vuId == m_correctVuId) {
			m_resultTxtVu.setText(R.string.TextViewResultCorrect);
			m_passedQNum += 1;
			m_zem.getZEntity(m_zubonsIndex).isAnsweredCorrectily = true;
			if (m_passedQNum == m_qNum) {
				m_gameState = GAME_STATE_SUCCESS;
				whenGameEnd();
			}
			updateCurrentIds();
			updateViews();
		} else {
			m_resultTxtVu.setText(R.string.TextViewResultIncorrect);
			m_gameState = GAME_STATE_FAILURE;
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ZubonOrDieQuestion.this);
			builder.setTitle(R.string.FailureDialogTitle);
			builder.setMessage(R.string.FailureDialogMessage);
			builder.setPositiveButton(R.string.FailureDialogCloseButton,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							whenGameEnd();
						}
					});
			builder.create();
			builder.show();
		}
	}

	private void whenGameEnd() {
		// calculate a average response time.
		Log.v(TAG, "End gaming.");
		long averageTime = 0;
		if (m_elapsedTimes.size() > 0) {
			for (long time : m_elapsedTimes) {
				averageTime += time;
			}
			averageTime /= m_elapsedTimes.size();
		}

		SharedPreferences pref = getSharedPreferences(PREFKEY_ZOD_RESULT,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(KEY_GAME_CLEARED, (m_qNum == m_passedQNum));
		editor.putInt(KEY_GAME_STATE, m_gameState);
		editor.putInt(KEY_PASSED_QUESTION_NUMBER, m_passedQNum);
		editor.putLong(KEY_AVERAGE_TIME, averageTime);
		editor.commit();

		m_zem.saveCorrectnesses();

		setResult(RESULT_OK);
		finish();
	}
}
