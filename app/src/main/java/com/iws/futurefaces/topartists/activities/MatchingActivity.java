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
import com.iws.futurefaces.topartists.data.database.DatabaseHandler;
import com.iws.futurefaces.topartists.fragments.ArtistListFragment;
import com.iws.futurefaces.topartists.fragments.ContactListFragment;
import com.iws.futurefaces.topartists.models.Artist;
import com.iws.futurefaces.topartists.models.Contact;

import java.util.ArrayList;

public class MatchingActivity extends AppCompatActivity {

	private static final int READ_CONTACT_PERMISSION = 1;
	private ArtistListFragment artistListFragment = null;
	private ContactListFragment contactListFragment = null;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matching);
		username = getIntent().getStringExtra(getString(R.string.username));

		if (savedInstanceState == null) {
			// Instantiate both fragments
			if (artistListFragment == null) {
				artistListFragment = new ArtistListFragment();
				contactListFragment = new ContactListFragment();
			}

			// Send username to artists fragment
			Bundle args = new Bundle();
			args.putString(getString(R.string.username), username);
			artistListFragment.setArguments(args);
		}

		// Check for Contact List permission
		if (hasPermission()) {
			initDatabase();
			commitArtistFragment();
		} else {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
					READ_CONTACT_PERMISSION);
		}
	}

	private void initDatabase() {
		// Init database and request data
		DatabaseHandler db = DatabaseHandler.getInstance(this);
		db.init(username, new DatabaseChangeListener());
	}

	private void commitArtistFragment() {

		getSupportFragmentManager().beginTransaction()
				.add(R.id.activity_matching, this.artistListFragment)
				.commitAllowingStateLoss();
	}

	public void onArtistInteraction(Artist artist) {

		Bundle args = new Bundle();
		args.putString(getString(R.string.current_artist), artist.getName());
		contactListFragment.setArguments(args);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.activity_matching, contactListFragment)
				.commit();
	}

	public void onContactInteraction(Contact contact, String currentArtist) {
		// TODO: Add relationship to a third table
		Toast.makeText(this, contact.getName() + " - " + currentArtist, Toast.LENGTH_SHORT).show();
	}

	private boolean hasPermission() {
		// Marshmallow and Nougat require user to grant permission
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
				== PackageManager.PERMISSION_GRANTED;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[],
										   int[] grantResults) {
		// Check if permission was granted
		switch (requestCode) {
			case READ_CONTACT_PERMISSION:
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					initDatabase();
					commitArtistFragment();
				} else {
					Toast.makeText(this, "Permission denied to read your Contacts.",
							Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	// Sync adapters with database
	public class DatabaseChangeListener implements DatabaseHandler.DatabaseListener {

		@Override
		public void syncArtists() {

			ArrayList<Artist> updatedArtistList =
					DatabaseHandler.getInstance(MatchingActivity.this).getAllArtists();
			if (updatedArtistList != null) {
				artistListFragment.updateData(updatedArtistList);
			}
		}

		@Override
		public void syncContacts() {

			ArrayList<Contact> updatedContactList =
					DatabaseHandler.getInstance(MatchingActivity.this).getAllContacts();
			contactListFragment.updateData(updatedContactList);

		}
	}
}
