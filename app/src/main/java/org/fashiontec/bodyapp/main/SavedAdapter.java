/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapp.main;

import java.util.List;
import org.fashiontec.bodyapp.models.MeasurementListModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter which populates the saved measurements list.
 */
public class SavedAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	List<MeasurementListModel> list;

	public SavedAdapter(Context context, List<MeasurementListModel> list) {
		this.context = context;
		this.list = list;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.saved_tab, null);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtName = (TextView) convertView.findViewById(R.id.svd_name);
		holder.txtDate = (TextView) convertView.findViewById(R.id.svd_date);
		holder.txtsync = (TextView) convertView.findViewById(R.id.svd_sync);
		holder.img = (ImageView) convertView.findViewById(R.id.svd_image);

		holder.txtName.setText(list.get(position).getPersonName());
		holder.txtDate.setText(list.get(position).getPersonEmail());
		holder.txtsync.setText(list.get(position).getCreated());

		return convertView;
	}

	static class ViewHolder {
		TextView txtName;
		TextView txtDate;
		TextView txtsync;
		ImageView img;
	}

}
