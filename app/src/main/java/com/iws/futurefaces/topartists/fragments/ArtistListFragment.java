package com.iws.futurefaces.topartists.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iws.futurefaces.topartists.R;
import com.iws.futurefaces.topartists.activities.MatchingActivity;
import com.iws.futurefaces.topartists.adapters.ArtistAdapter;
import com.iws.futurefaces.topartists.models.Artist;

import java.util.ArrayList;

public class ArtistListFragment extends Fragment implements
		ArtistAdapter.OnArtistFragmentInteractionListener{

	private ArtistAdapter artistAdapter;
	private ArrayList<Artist> artistList;

	public ArtistListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Check if list and adapter were not yet initialized due to asynchronous data fetching
		if (artistAdapter == null && artistList == null) {
			artistList = new ArrayList<Artist>();
			artistAdapter = new ArtistAdapter(artistList, this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_artist_list,
				container, false);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(artistAdapter);
		return recyclerView;
	}

	public void updateData(ArrayList<Artist> updatedArtistList) {

		// onCreate method might might not have been called yet
		if (artistList == null) {
			artistList = new ArrayList<Artist>();
			artistAdapter = new ArtistAdapter(artistList, this);
		}

		// Update adapter
		artistList.clear();
		artistList.addAll(updatedArtistList);
		artistAdapter.notifyDataSetChanged();
	}

	@Override
	public void onArtistInteraction(Artist item) {
		// Alert activity that fragment must be replaced
		((MatchingActivity)getActivity()).onArtistInteraction(item);
	}

	public interface OnArtistFragmentInteractionListener {
		void onArtistInteraction(Artist item);
	}
}
