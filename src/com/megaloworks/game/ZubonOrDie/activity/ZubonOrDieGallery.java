package com.megaloworks.game.ZubonOrDie.activity;

import java.util.ArrayList;

import com.megaloworks.game.ZubonOrDie.R;
import com.megaloworks.game.ZubonOrDie.model.ZubonEntity;
import com.megaloworks.game.ZubonOrDie.support.ZubonEntityManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Gallery Class: <br>
 * Show images and texts of Zubons which answered correctly in
 * ZubonOrDieQuestion.
 * 
 * @author trude
 * 
 */
public final class ZubonOrDieGallery extends Activity {

	private static final String TAG = ZubonOrDieGallery.class.getName();

	private ZubonEntity[] m_zubons;

	private ZubonEntity[] m_pantss;

	private ZubonEntityManager m_zem;

	private ImageView m_zubonImgVu;

	private TextView m_titleTxtVu;

	private TextView m_descTxtVu;

	private ImageButton m_backBtn;

	private ImageButton m_nextBtn;

	private int m_leastIndex;

	private int m_mostIndex;

	private int m_currentZubonIndex;

	class BackButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			backZubon();
		}

	}

	class NextButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			nextZubon();
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);

		m_leastIndex = Integer.MIN_VALUE;
		m_mostIndex = Integer.MAX_VALUE;
		// make the list of correctly answered zubonEntities.
		m_zem = ZubonEntityManager.getInstance(this);
		ArrayList<ZubonEntity> zubons = new ArrayList<ZubonEntity>();
		for (int i = 0; i < m_zem.getNumZubons(); i++) {
			ZubonEntity zubon = m_zem.getZEntity(i);
			if (zubon.isAnsweredCorrectily) {
				if (m_leastIndex == Integer.MIN_VALUE) {
					m_leastIndex = i;
				}
				m_mostIndex = i;
				zubons.add(zubon);
			}
		}
		m_zubons = zubons.toArray(new ZubonEntity[0]);

		Log.v(TAG, "_mostIndex: " + m_mostIndex);
		Log.v(TAG, "_leastIndex: " + m_leastIndex);
		Log.v(TAG, "_zubons length: " + m_zubons.length);

		if (m_zubons.length > 0) {
			// obtain each view
			m_zubonImgVu = (ImageView) findViewById(R.id.GalleryZubonImageView);
			m_titleTxtVu = (TextView) findViewById(R.id.GalleryImageTitle);
			m_descTxtVu = (TextView) findViewById(R.id.GalleryImageDescription);
			m_backBtn = (ImageButton) findViewById(R.id.GalleryBackImageButton);
			m_nextBtn = (ImageButton) findViewById(R.id.GalleryNextImageButton);

			// set listener on button
			m_backBtn.setOnClickListener(new BackButtonOnClickListener());
			m_nextBtn.setOnClickListener(new NextButtonOnClickListener());
		}

		m_currentZubonIndex = 0;
	}

	public void nextZubon() {
		if (m_currentZubonIndex >= m_zubons.length - 1) {
			m_currentZubonIndex = 0;
		} else {
			m_currentZubonIndex++;
		}
		m_titleTxtVu.setText(m_zubons[m_currentZubonIndex].resStr);
		m_zubonImgVu.setImageResource(m_zubons[m_currentZubonIndex].imgId);
		// _descTxtVu.setText(_zubons[_currentZubonIndex].txtId);
	}

	public void backZubon() {
		if (m_currentZubonIndex < 1) {
			m_currentZubonIndex = m_zubons.length - 1;
		} else {
			m_currentZubonIndex--;
		}
		Log.v(TAG, "_currentZubonIndex: " + m_currentZubonIndex);
		m_titleTxtVu.setText(m_zubons[m_currentZubonIndex].resStr);
		m_zubonImgVu.setImageResource(m_zubons[m_currentZubonIndex].imgId);
		// _descTxtVu.setText(_zubons[_currentZubonIndex].txtId);
	}

}