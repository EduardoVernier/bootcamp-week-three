package com.iws.futurefaces.topartists.models;

public class Artist {
	private int id = -1;
	private String name;
	private int playCount;
	private String imageSmall;
	private String imageLarge;

	public Artist(String name, int playCount, String imageSmall, String imageLarge) {

		this.name = name;
		this.playCount = playCount;
		this.imageSmall = imageSmall;
		this.imageLarge = imageLarge;
	}

	public Artist(int id, String name, int playCount, String imageSmall, String imageLarge) {

		this.id = id;
		this.name = name;
		this.playCount = playCount;
		this.imageSmall = imageSmall;
		this.imageLarge = imageLarge;
	}

	public String getName() {
		return name;
	}

	public int getPlayCount() {
		return playCount;
	}

	public String getImageSmall() {
		return imageSmall;
	}

	public String getImageLarge() {
		return imageLarge;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
