package com.megaloworks.game.ZubonOrDie.support;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.megaloworks.game.ZubonOrDie.R;
import com.megaloworks.game.ZubonOrDie.model.ZubonEntity;

/**
 * Manage class for ZubonEntities <br>
 * using Singleton pattern.
 * 
 * @author trude
 * 
 */
public final class ZubonEntityManager {

	private static final String TAG = ZubonEntityManager.class.getName();

	private static final String PREFKEY_ZOD_CORRECTNESSES = "PREFRENCE_KEY_ZUBON_OR_DIE_CORRECTNESSES";

	private static final String ZUBON_ENTITY_PREFIX = "zubon";

	private static final String PANTS_ENTITY_PREFIX = "pants";

	private ZubonEntity[] m_zubons;

	private ZubonEntity[] m_pantss;

	private SharedPreferences _pref;

	private SharedPreferences.Editor m_editor;

	private static ZubonEntityManager m_zem;

	private ZubonEntityManager(Context context) {

		Log.v(TAG, "Initilaze ZubonEntityManager.");

		_pref = context.getSharedPreferences(PREFKEY_ZOD_CORRECTNESSES,
				Context.MODE_PRIVATE);
		m_editor = _pref.edit();

		Field[] drawables = R.drawable.class.getFields();
		Field[] strings = R.string.class.getFields();

		ArrayList<ZubonEntity> zubons = new ArrayList<ZubonEntity>();
		ArrayList<ZubonEntity> pantss = new ArrayList<ZubonEntity>();

		// initialize imgIds
		for (int i = 0; i < drawables.length; i++) {
			if (drawables[i].getName().startsWith(ZUBON_ENTITY_PREFIX)) {
				ZubonEntity ze = new ZubonEntity();
				ze.resStr = drawables[i].getName();
				ze.isAnsweredCorrectily = _pref.getBoolean(ze.resStr, false);
				ze.isZubon = true;
				try {
					ze.imgId = drawables[i].getInt(null);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				zubons.add(ze);
			} else if (drawables[i].getName().startsWith(PANTS_ENTITY_PREFIX)) {
				ZubonEntity ze = new ZubonEntity();
				ze.resStr = drawables[i].getName();
				ze.isAnsweredCorrectily = _pref.getBoolean(ze.resStr, false);
				ze.isZubon = false;
				try {
					ze.imgId = drawables[i].getInt(null);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				pantss.add(ze);
			}
		}

		// initialize txtIds
		// for (int i = 0; i < strings.length; i++) {
		// if (strings[i].getName().startsWith(ZUBON_ENTITY_PREFIX)) {
		// for (ZubonEntity ze : zubons) {
		// if (ze.resStr.equals(strings[i].getName())) {
		// try {
		// ze.txtId = strings[i].getInt(null);
		// } catch (IllegalArgumentException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// } else if (strings[i].getName().startsWith(PANTS_ENTITY_PREFIX)) {
		// for (ZubonEntity ze : pantss) {
		// if (ze.resStr.equals(strings[i].getName())) {
		// try {
		// ze.txtId = strings[i].getInt(null);
		// } catch (IllegalArgumentException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// }
		// }

		// convert arraylist to array.
		m_zubons = zubons.toArray(new ZubonEntity[0]);
		m_pantss = pantss.toArray(new ZubonEntity[0]);
	}

	public static ZubonEntityManager getInstance(Context context) {
		if (m_zem == null) {
			m_zem = new ZubonEntityManager(context);
		}
		return m_zem;
	}

	public int getNumZubons() {
		return m_zubons.length;
	}

	public int getNumPantss() {
		return m_pantss.length;
	}

	public ZubonEntity getZEntity(int index) {
		if (index >= m_zubons.length) {
			return null;
		}
		return m_zubons[index];
	}

	public ZubonEntity getPEntity(int index) {
		if (index >= m_pantss.length) {
			return null;
		}
		return m_pantss[index];
	}

	public void saveCorrectnesses() {
		for (ZubonEntity ze : m_zubons) {
			m_editor.putBoolean(ze.resStr, ze.isAnsweredCorrectily);
		}
		m_editor.commit();
	}

}
