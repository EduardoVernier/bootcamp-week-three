package com.iws.futurefaces.topartists.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iws.futurefaces.topartists.R;
import com.iws.futurefaces.topartists.fragments.ArtistListFragment;

public class ArtistListActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_list);

		if (savedInstanceState == null) {

			getSupportFragmentManager().beginTransaction()
					.add(R.id.activity_main, new ArtistListFragment())
					.commit();
		}
	}
}
