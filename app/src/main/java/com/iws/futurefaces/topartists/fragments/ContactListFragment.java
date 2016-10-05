package com.iws.futurefaces.topartists.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iws.futurefaces.topartists.R;
import com.iws.futurefaces.topartists.adapters.ContactAdapter;
import com.iws.futurefaces.topartists.models.Contact;

import java.util.ArrayList;

public class ContactListFragment extends Fragment {

	private ArrayList<Contact> contactList;
	private ContactAdapter contactAdapter;

	public ContactListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (contactList == null) {
			contactList = new ArrayList<Contact>();
			contactAdapter = new ContactAdapter(contactList,
					(ContactAdapter.OnContactFragmentInteractionListener) getActivity());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_contact_list, container, false);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(contactAdapter);
		return recyclerView;
	}

	public void updateData(ArrayList<Contact> updatedContactList) {

		if (contactList == null) {
			contactList = new ArrayList<Contact>();
			contactAdapter = new ContactAdapter(contactList,
					(ContactAdapter.OnContactFragmentInteractionListener) getActivity());
		}

		contactList.clear();
		contactList.addAll(updatedContactList);
		contactAdapter.notifyDataSetChanged();
	}
}
