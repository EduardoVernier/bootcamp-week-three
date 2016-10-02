package com.iws.futurefaces.topartists.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iws.futurefaces.topartists.R;
import com.iws.futurefaces.topartists.adapters.ArtistAdapter;
import com.iws.futurefaces.topartists.data.network.ArtistProvider;
import com.iws.futurefaces.topartists.models.Artist;

import java.util.ArrayList;

public class ArtistListFragment extends Fragment
		implements ArtistAdapter.OnListFragmentInteractionListener{

	private ArtistAdapter adapter;
	private ArrayList<Artist> artistList;
	private ArtistProvider artistProvider;
	private String username;

	public ArtistListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		username = getArguments().getString(getString(R.string.username));
		artistList = new ArrayList<Artist>();
		adapter = new ArtistAdapter(artistList, this);

		artistProvider = ArtistProvider.getInstance();
		artistProvider.init(username, artistList, adapter, getContext());
		artistProvider.getArtists();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_artist_list,
				container, false);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(adapter);
		return recyclerView;
	}

	@Override
	public void onListFragmentInteraction(Artist item) {

	}
}
