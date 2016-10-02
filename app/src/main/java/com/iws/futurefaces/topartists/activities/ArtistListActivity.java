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
		String username = getIntent().getStringExtra(getString(R.string.username));

		if (savedInstanceState == null) {
			ArtistListFragment artistListFragment = new ArtistListFragment();

			Bundle args = new Bundle();
			args.putString(getString(R.string.username), username);
			artistListFragment.setArguments(args);

			getSupportFragmentManager().beginTransaction()
					.add(R.id.activity_artist_list, artistListFragment)
					.commit();
		}
	}
}
