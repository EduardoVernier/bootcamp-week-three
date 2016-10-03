package com.iws.futurefaces.topartists.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.iws.futurefaces.topartists.data.network.ArtistProvider;
import com.iws.futurefaces.topartists.models.Artist;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

	public interface DatabaseListener {
		public void onArtistsReady();
	}

	private DatabaseListener listener;

	private static DatabaseHandler instance = null;
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "database";
	private static final String TABLE_ARTISTS = "artists";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "artistName";
	private static final String KEY_PLAYCOUNT = "playCount";
	private static final String KEY_IMAGE_SMALL = "imageSmall";
	private static final String KEY_IMAGE_LARGE = "imageLarge";

	private Context context;

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
		ArtistProvider.fetchArtists(username, context);
		fetchContacts();
	}

	private void fetchContacts() {
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_BOOKMARKS_TABLE = "CREATE TABLE " + TABLE_ARTISTS + " ("
				+ KEY_ID + " INTEGER PRIMARY KEY, "
				+ KEY_NAME + " TEXT UNIQUE, "
				+ KEY_PLAYCOUNT + " INTEGER, "
				+ KEY_IMAGE_SMALL + " TEXT, "
				+ KEY_IMAGE_LARGE + " TEXT" + ")";
		db.execSQL(CREATE_BOOKMARKS_TABLE);
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
				values.put(KEY_NAME, artist.getName());
				values.put(KEY_PLAYCOUNT, artist.getPlayCount());
				values.put(KEY_IMAGE_SMALL, artist.getImageSmall());
				values.put(KEY_IMAGE_LARGE, artist.getImageLarge());

				db.insert(TABLE_ARTISTS, null, values);
			}

			db.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			listener.onArtistsReady();
		}
	}

	public ArrayList<Artist> getAllArtists() {

		ArrayList<Artist> artistArrayList = new ArrayList<Artist>();
		SQLiteDatabase db = this.getWritableDatabase();
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



}
