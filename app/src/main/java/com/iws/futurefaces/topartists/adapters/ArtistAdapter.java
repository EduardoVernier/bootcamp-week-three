package com.iws.futurefaces.topartists.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iws.futurefaces.topartists.R;
import com.iws.futurefaces.topartists.models.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

	private final List<Artist> artistList;
	private final OnListFragmentInteractionListener listener;

	public ArtistAdapter(ArrayList<Artist> artistList, OnListFragmentInteractionListener listener) {
		this.artistList = artistList;
		this.listener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_artist, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.item = artistList.get(position);
//		holder.idView.setText(holder.item.id);
//		holder.contentView.setText(holder.item.content);

		holder.view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				listener.onListFragmentInteraction(holder.item);
			}
		});
	}

	@Override
	public int getItemCount() {
		return artistList.size();
	}

	public interface OnListFragmentInteractionListener {
		void onListFragmentInteraction(Artist item);
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View view;
		public final TextView idView;
		public final TextView contentView;
		public Artist item;

		public ViewHolder(View view) {
			super(view);
			this.view = view;
			idView = (TextView) view.findViewById(R.id.id);
			contentView = (TextView) view.findViewById(R.id.content);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + contentView.getText() + "'";
		}
	}
}
