package com.iws.futurefaces.topartists.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.iws.futurefaces.topartists.R;
import com.iws.futurefaces.topartists.adapters.ArtistAdapter;
import com.iws.futurefaces.topartists.adapters.ContactAdapter;
import com.iws.futurefaces.topartists.data.database.DatabaseHandler;
import com.iws.futurefaces.topartists.fragments.ArtistListFragment;
import com.iws.futurefaces.topartists.fragments.ContactListFragment;
import com.iws.futurefaces.topartists.models.Artist;
import com.iws.futurefaces.topartists.models.Contact;

import java.util.ArrayList;

public class MatchingActivity extends AppCompatActivity
		implements ArtistAdapter.OnArtistFragmentInteractionListener,
		ContactAdapter.OnContactFragmentInteractionListener {

	private ArtistListFragment artistListFragment = null;
	private ContactListFragment contactListFragment = null;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matching);
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
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
		}
	}

	private void commitArtistFragment() {

		DatabaseHandler db = DatabaseHandler.getInstance(this);
		db.init(username, new MatchingActivity.DatabaseChangeListener());

		getSupportFragmentManager().beginTransaction()
				.add(R.id.activity_matching, this.artistListFragment)
				.commitAllowingStateLoss();
	}


	@Override
	public void onArtistInteraction(Artist item) {

		if (contactListFragment == null) {
			contactListFragment = new ContactListFragment();
		}

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.activity_matching, contactListFragment)
				.commit();
	}

	@Override
	public void onContactInteraction(Contact contact) {

	}

	private boolean hasPermission() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
				== PackageManager.PERMISSION_GRANTED;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[],
										   int[] grantResults) {
		switch (requestCode) {
			case 1: {
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

	public class DatabaseChangeListener implements DatabaseHandler.DatabaseListener {

		@Override
		public void onDataFetched() {

			ArrayList<Artist> updatedArtistList =
					DatabaseHandler.getInstance(MatchingActivity.this).getAllArtists();
			artistListFragment.updateData(updatedArtistList);


			ArrayList<Contact> updatedContactList =
					DatabaseHandler.getInstance(MatchingActivity.this).getAllContacts();
			contactListFragment.updateData(updatedContactList);

		}
	}

}
