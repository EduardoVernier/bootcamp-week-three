package com.iws.futurefaces.topartists.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iws.futurefaces.topartists.R;
import com.iws.futurefaces.topartists.models.Contact;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

	private ArrayList<Contact> contactList;
	private OnContactFragmentInteractionListener listener;

	public ContactAdapter(ArrayList<Contact> contactList) {
		this.contactList = contactList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_contact, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.item = contactList.get(position);
		holder.idView.setText(String.valueOf(holder.item.getId()));
		holder.contentView.setText(holder.item.getName());

		holder.view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onContactInteraction(holder.item);
			}
		});
	}

	@Override
	public int getItemCount() {
		return contactList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View view;
		public final TextView idView;
		public final TextView contentView;
		public Contact item;

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

	public interface OnContactFragmentInteractionListener {
		void onContactInteraction(Contact contact);
	}
}
