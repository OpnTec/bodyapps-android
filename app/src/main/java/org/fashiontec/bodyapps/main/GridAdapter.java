/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.main;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *Adapter which handles the icon grid in Measurement activity.
 */
public class GridAdapter extends BaseAdapter {
	private Context mContext;
	private static LayoutInflater inflater=null;
	public String[] result={"0/0","0/1","0/3","0/1","0/4","0/0","0/2","0/0","0/0","0/0","0/2","0/3"};

	// Keep all Images in array
	public Integer[] mThumbIds = { R.drawable.head, R.drawable.neck,
			R.drawable.shoulders, R.drawable.chest, R.drawable.arm,
			R.drawable.hand, R.drawable.hip_waist, R.drawable.leg,
			R.drawable.foot, R.drawable.trunk, R.drawable.heights,
			R.drawable.pics };

	// Constructor
	public GridAdapter(Context c) {
		mContext = c;
		inflater = ( LayoutInflater )c.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		return mThumbIds[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//get the display metrics for the device
		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(metrics);
		//get the pixel density for the device
		float density = mContext.getResources().getDisplayMetrics().density;
		//get the device width in pixels
		float width = metrics.widthPixels;
		//loads image views as thumbnails to grid
//		ImageView imageView = new ImageView(mContext);
//		imageView.setImageResource(mThumbIds[position]);
//		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//sets the each thumbnail size according to the screen size
		float num;
		if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			num = ((width / 2) - 50 * density) / 3;
		} else {
			num = (width - 50 * density) / 3;
		}

		// System.out.println(num+" "+width+" "+(width/150)+" "+density);
//		imageView.setLayoutParams(new GridView.LayoutParams((int) (num),
//				(int) (num)));
//		return imageView;
		 Holder holder=new Holder();
	        View rowView;
	 
	             rowView = inflater.inflate(R.layout.grid_cell, null);
	             if(rowView==null){
	            	 System.out.println("null");
	             }
	             holder.tv=(TextView) rowView.findViewById(R.id.textView1);
	             holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
	             holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
	 
	         holder.tv.setText(result[position]);
	         holder.img.setImageResource(mThumbIds[position]);
	        rowView.setLayoutParams(new GridView.LayoutParams((int)(num),(int)(num)));
	       return rowView;

	}
	public class Holder
    {
        TextView tv;
        ImageView img;
    }

}
