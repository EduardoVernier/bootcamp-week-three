package com.iws.futurefaces.topartists.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.iws.futurefaces.topartists.data.local.ContactProvider;
import com.iws.futurefaces.topartists.data.network.ArtistProvider;
import com.iws.futurefaces.topartists.models.Artist;
import com.iws.futurefaces.topartists.models.Contact;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

	public interface DatabaseListener {
		public void syncArtists();
		public void syncContacts();
	}

	private static DatabaseHandler instance = null;
	private DatabaseListener listener;
	private Context context;

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "database";
	private static final String TABLE_ARTISTS = "artists";
	private static final String TABLE_CONTACTS = "contacts";

	private static final String KEY_ARTISTS_ID = "id";
	private static final String KEY_ARTISTS_NAME = "artistName";
	private static final String KEY_ARTISTS_PLAYCOUNT = "playCount";
	private static final String KEY_ARTISTS_IMAGE_SMALL = "imageSmall";
	private static final String KEY_ARTISTS_IMAGE_LARGE = "imageLarge";

	private static final String KEY_CONTACTS_ID = "id";
	private static final String KEY_CONTACTS_NAME = "contactName";
	private static final String KEY_CONTACTS_PHONE_NUMBER = "phoneNumber";


	private DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DatabaseHandler getInstance(Context context) {

		if (instance == null) {
			instance = new DatabaseHandler(context);
			instance.context = context;
			instance.listener = null;
		}
		return instance;
	}

	public void init(String username, DatabaseListener listener) {

		this.listener = listener;

		// Offline first (sync UI with current database state)
		listener.syncArtists();
		listener.syncContacts();

		// Then use fetch data from network/local storage
		ArtistProvider.fetchArtists(username, context);
		ContactProvider.fetchContacts(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_ARTISTS_TABLE = "CREATE TABLE " + TABLE_ARTISTS + " ("
				+ KEY_ARTISTS_ID + " INTEGER PRIMARY KEY, "
				+ KEY_ARTISTS_NAME + " TEXT UNIQUE, "
				+ KEY_ARTISTS_PLAYCOUNT + " INTEGER, "
				+ KEY_ARTISTS_IMAGE_SMALL + " TEXT, "
				+ KEY_ARTISTS_IMAGE_LARGE + " TEXT" + ")";

		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + " ("
				+ KEY_CONTACTS_ID + " INTEGER PRIMARY KEY, "
				+ KEY_CONTACTS_NAME + " TEXT, "
				+ KEY_CONTACTS_PHONE_NUMBER + " TEXT UNIQUE" + ")";

		db.execSQL(CREATE_ARTISTS_TABLE);
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTISTS);
		onCreate(db);
	}

	public void addArtists(ArrayList<Artist> artistArrayList) {

		AsyncAddArtist task = new AsyncAddArtist();
		task.execute(artistArrayList);
	}

	private class AsyncAddArtist extends AsyncTask<ArrayList<Artist>, Void, Void> {

		@Override
		protected Void doInBackground(ArrayList<Artist>... artistArrayList) {

			SQLiteDatabase db = DatabaseHandler.this.getWritableDatabase();

			for (Artist artist : artistArrayList[0]) {

				ContentValues values = new ContentValues();
				values.put(KEY_ARTISTS_NAME, artist.getName());
				values.put(KEY_ARTISTS_PLAYCOUNT, artist.getPlayCount());
				values.put(KEY_ARTISTS_IMAGE_SMALL, artist.getImageSmall());
				values.put(KEY_ARTISTS_IMAGE_LARGE, artist.getImageLarge());

				try {
					db.insertOrThrow(TABLE_ARTISTS, null, values);
				} catch (SQLException e) {
					; // Mute failed insertions due to the UNIQUE constraint
				}
			}
			db.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			listener.syncArtists();
		}
	}

	public void addContacts(ArrayList<Contact> contactArrayList) {

		AsyncAddContacts task = new AsyncAddContacts();
		task.execute(contactArrayList);
	}

	private class AsyncAddContacts extends AsyncTask<ArrayList<Contact>, Void, Void> {

		@Override
		protected Void doInBackground(ArrayList<Contact>... contactArrayList) {

			SQLiteDatabase db = DatabaseHandler.this.getWritableDatabase();

			for (Contact contact : contactArrayList[0]) {

				ContentValues values = new ContentValues();
				values.put(KEY_CONTACTS_NAME, contact.getName());
				values.put(KEY_CONTACTS_PHONE_NUMBER, contact.getPhoneNumber());

				try {
					db.insertOrThrow(TABLE_CONTACTS, null, values);
				} catch (SQLException e) {
					; // Mute failed insertions due to the UNIQUE constraint
				}
			}
			db.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			listener.syncContacts();
		}
	}

	public ArrayList<Artist> getAllArtists() {

		ArrayList<Artist> artistArrayList = new ArrayList<Artist>();
		SQLiteDatabase db = instance.getWritableDatabase();
		Cursor cursor = db.query(TABLE_ARTISTS, null, null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(0);
				String name = cursor.getString(1);
				int playCount = cursor.getInt(2);
				String imageSmall = cursor.getString(3);
				String imageLarge = cursor.getString(4);

				Artist artist = new Artist(id, name, playCount, imageSmall, imageLarge);
				artistArrayList.add(artist);

			} while (cursor.moveToNext());
		}

		db.close();
		return artistArrayList;
	}

	public ArrayList<Contact> getAllContacts() {

		ArrayList<Contact> contactArrayList = new ArrayList<Contact>();
		SQLiteDatabase db = instance.getWritableDatabase();
		Cursor cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(0);
				String name = cursor.getString(1);
				String phoneNumber = cursor.getString(2);

				Contact contact = new Contact(id, name, phoneNumber);
				contactArrayList.add(contact);

			} while (cursor.moveToNext());
		}

		db.close();
		return contactArrayList;
	}


}
