package com.megaloworks.game.ZubonOrDie.model;

public final class ZubonEntity {

	public String resStr;

	public int imgId;

	public String txtId;

	public boolean isZubon;

	public boolean isAnsweredCorrectily;

	public ZubonEntity(int imgId, String txtId, boolean isZubon,
			boolean isAnsweredCorrectly) {
		this.imgId = imgId;
		this.txtId = txtId;
		this.isZubon = isZubon;
		this.isAnsweredCorrectily = isAnsweredCorrectly;
	}

	public ZubonEntity() {
	}

}
