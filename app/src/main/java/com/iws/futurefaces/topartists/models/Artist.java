package com.iws.futurefaces.topartists.models;

public class Artist {
	private int id = -1;
	private String name;
	private String playCount;
	private String imageSmall;
	private String imageLarge;

	public Artist(String name, String playCount, String imageSmall, String imageLarge) {
		this.name = name;
		this.playCount = playCount;
		this.imageSmall = imageSmall;
		this.imageLarge = imageLarge;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
