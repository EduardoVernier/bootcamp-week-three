package com.iws.futurefaces.topartists.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.iws.futurefaces.topartists.R;
import com.iws.futurefaces.topartists.adapters.ArtistAdapter;
import com.iws.futurefaces.topartists.adapters.ContactAdapter;
import com.iws.futurefaces.topartists.data.database.DatabaseHandler;
import com.iws.futurefaces.topartists.data.local.ContactProvider;
import com.iws.futurefaces.topartists.data.network.ArtistProvider;
import com.iws.futurefaces.topartists.fragments.ArtistListFragment;
import com.iws.futurefaces.topartists.fragments.ContactListFragment;
import com.iws.futurefaces.topartists.models.Artist;
import com.iws.futurefaces.topartists.models.Contact;

import java.util.ArrayList;

public class MatchingActivity extends AppCompatActivity
		implements ArtistAdapter.OnArtistFragmentInteractionListener,
		ContactAdapter.OnContactFragmentInteractionListener {

	private final int READ_CONTACT_PERMISSION = 1;
	private ArtistListFragment artistListFragment = null;
	private ContactListFragment contactListFragment = null;
	private SwipeRefreshLayout swipeContainer;
	private String username;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matching);
		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.activity_matching);
		swipeContainer.setOnRefreshListener(new DataChangeListener());
		username = getIntent().getStringExtra(getString(R.string.username));

		if (savedInstanceState == null) {

			if (artistListFragment == null) {
				artistListFragment = new ArtistListFragment();
				contactListFragment = new ContactListFragment();
			}

			Bundle args = new Bundle();
			args.putString(getString(R.string.username), username);
			artistListFragment.setArguments(args);
		}

		if (hasPermission()) {
			commitArtistFragment();
		}
		else {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 
					READ_CONTACT_PERMISSION);
		}
	}

	private void commitArtistFragment() {
		// Initialize database with username
		DatabaseHandler db = DatabaseHandler.getInstance(this);
		db.init(username, new DataChangeListener());

		getSupportFragmentManager().beginTransaction()
				.add(R.id.activity_matching, artistListFragment)
				.commitAllowingStateLoss();
	}

	@Override
	public void onArtistInteraction(Artist item) {

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.activity_matching, contactListFragment)
				.commit();
	}

	@Override
	public void onContactInteraction(Contact contact) {

	}

	private boolean hasPermission() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& ContextCompat.checkSelfPermission(this,
				Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[],
										   int[] grantResults) {
		switch (requestCode) {
			case READ_CONTACT_PERMISSION: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					commitArtistFragment();

				} else {
					Toast.makeText(this, "Permission denied to read your Contacts.", Toast.LENGTH_SHORT).show();
				}
				return;
			}
		}
	}

	public class DataChangeListener implements DatabaseHandler.DatabaseListener,
			SwipeRefreshLayout.OnRefreshListener {

		@Override
		public void updateArtists(ArrayList<Artist> artistArrayList) {
			artistListFragment.updateData(artistArrayList);
		}

		@Override
		public void updateContacts(ArrayList<Contact> contactArrayList) {
			contactListFragment.updateData(contactArrayList);
		}

		@Override
		public void onRefresh() {
			ArtistProvider.fetchArtists(username, MatchingActivity.this);
			ContactProvider.fetchContacts(MatchingActivity.this);

			swipeContainer.setRefreshing(false);
		}
	}

}
