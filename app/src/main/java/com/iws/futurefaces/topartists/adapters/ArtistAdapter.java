package com.iws.futurefaces.topartists.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.iws.futurefaces.topartists.R;
import com.iws.futurefaces.topartists.models.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

	private List<Artist> artistList;
	private OnArtistFragmentInteractionListener listener;
	private Context context;

	public ArtistAdapter(ArrayList<Artist> artistList, OnArtistFragmentInteractionListener listener) {
		this.artistList = artistList;
		this.listener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		context = parent.getContext();
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_artist, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.item = artistList.get(position);
		holder.name.setText(holder.item.getName());
		holder.playCount.setText(String.valueOf(holder.item.getPlayCount()));

		String thumbnailUrl = holder.item.getImageSmall();

		Glide.with(context).load(thumbnailUrl).asBitmap().centerCrop()
				.into(new BitmapImageViewTarget(holder.thumbnail) {
			@Override
			protected void setResource(Bitmap resource) {
				RoundedBitmapDrawable circularBitmapDrawable =
						RoundedBitmapDrawableFactory.create(context.getResources(), resource);
				circularBitmapDrawable.setCircular(true);
				holder.thumbnail.setImageDrawable(circularBitmapDrawable);
			}
		});

		holder.view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				listener.onArtistInteraction(holder.item);
			}
		});
	}

	@Override
	public int getItemCount() {
		return artistList.size();
	}

	public interface OnArtistFragmentInteractionListener {
		void onArtistInteraction(Artist item);
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View view;
		public final TextView name;
		public final TextView playCount;
		public final ImageView thumbnail;
		public Artist item;

		public ViewHolder(View view) {
			super(view);
			this.view = view;
			
			thumbnail = (ImageView) view.findViewById(R.id.artist_thumbnail);
			name = (TextView) view.findViewById(R.id.artist_name);
			playCount = (TextView) view.findViewById(R.id.artist_play_count);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + playCount.getText() + "'";
		}
	}
}
