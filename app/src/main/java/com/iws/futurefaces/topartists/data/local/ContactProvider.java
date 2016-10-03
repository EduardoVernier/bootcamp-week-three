package com.iws.futurefaces.topartists.data.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.iws.futurefaces.topartists.data.database.DatabaseHandler;
import com.iws.futurefaces.topartists.models.Contact;

import java.util.ArrayList;

public class ContactProvider {

	private static ContactProvider instance = null;

	private ContactProvider() {}

	public static void fetchContacts(Context context) {

		ArrayList<Contact> contactArrayList = new ArrayList<Contact>();

		Cursor phones = context.getContentResolver()
				.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);

		while (phones.moveToNext())
		{
			String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			contactArrayList.add(new Contact(name, phoneNumber));
		}
		phones.close();

		DatabaseHandler.getInstance(context).addContacts(contactArrayList);
	}

}
