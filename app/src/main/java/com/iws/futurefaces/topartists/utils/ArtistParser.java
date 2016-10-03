package com.iws.futurefaces.topartists.utils;

import com.iws.futurefaces.topartists.models.Artist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ArtistParser {

	private ArtistParser() {}

	static public ArrayList<Artist> parseArtists(String response) throws JSONException {

		ArrayList<Artist> returnList =  new ArrayList<Artist>();

		JSONArray artists = new JSONObject(response).getJSONObject("topartists").getJSONArray("artist");

		for (int i = 0; i < artists.length(); i++) {

			JSONObject artist = artists.getJSONObject(i);
			String name = artist.getString("name");
			int playCount = artist.getInt("playcount");
			String imageSmall = artist.getJSONArray("image").getJSONObject(2).getString("#text");
			String imageLarge = artist.getJSONArray("image").getJSONObject(4).getString("#text");

			returnList.add(new Artist(name, playCount, imageSmall, imageLarge));
		}
		return returnList;
	}
}
