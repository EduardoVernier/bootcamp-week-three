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
import com.iws.futurefaces.topartists.data.database.DatabaseHandler;
import com.iws.futurefaces.topartists.models.Artist;
import com.iws.futurefaces.topartists.utils.ArtistParser;
import org.json.JSONException;
import java.util.ArrayList;

public class ArtistProvider {

	private static ArtistProvider instance = null;
	private static String API_KEY = "63083974d27eb340a1152911f1bcb934";

	private ArtistProvider() {}

	static public void fetchArtists(String username, final Context context) {

		RequestQueue queue = Volley.newRequestQueue(context);

		Resources res = context.getResources();
		String url = res.getString(R.string.artists_url, username, API_KEY);

		StringRequest artistsRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							ArrayList<Artist> artistArrayList = ArtistParser.parseArtists(response);
							DatabaseHandler.getInstance(context).addArtists(artistArrayList);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(context, "Couldn't load artists from network.", Toast.LENGTH_SHORT).show();
				return;
			}
		});

		queue.add(artistsRequest);
	}
}
