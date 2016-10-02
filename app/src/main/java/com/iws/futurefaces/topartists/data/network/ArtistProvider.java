package com.iws.futurefaces.topartists.data.network;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iws.futurefaces.topartists.R;
import com.iws.futurefaces.topartists.adapters.ArtistAdapter;
import com.iws.futurefaces.topartists.models.Artist;
import com.iws.futurefaces.topartists.utils.ArtistParser;

import org.json.JSONException;

import java.util.ArrayList;

public class ArtistProvider {

	private static ArtistProvider instance = null;
	private String username = null;
	private ArrayList<Artist> artistList = null;
	private ArtistAdapter adapter = null;
	private Context context = null;
	private String API_KEY = "63083974d27eb340a1152911f1bcb934";
	private RequestQueue queue;

	private ArtistProvider() {}

	static public ArtistProvider getInstance () {

		if (instance == null) {
			instance = new ArtistProvider();
		}
		return instance;
	}

	public void init (String username, ArrayList<Artist> artistList, ArtistAdapter adapter, Context context) {

		this.username = username;
		this.artistList = artistList;
		this.adapter = adapter;
		this.context = context;
		queue = Volley.newRequestQueue(context);
	}

	public void getArtists() {

		Resources res = context.getResources();
		String url = res.getString(R.string.artists_url, username, API_KEY);

		StringRequest artistsRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							ArtistParser.parseArtists(response);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(context, "Couldn't load artists.", Toast.LENGTH_SHORT).show();
				return;
			}
		});

		queue.add(artistsRequest);
	}
}
