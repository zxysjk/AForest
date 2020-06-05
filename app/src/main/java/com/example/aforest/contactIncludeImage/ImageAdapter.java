package com.example.aforest.contactIncludeImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aforest.R;

import java.util.List;

public class ImageAdapter extends BaseAdapter{
	List<contactItem> ls;
	Context context;
	ImageAdapter(Context context, List<contactItem> ls)
	{
		this.context = context;
		this.ls = ls;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ls.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		if (convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.contact_item, null);
			TextView name = view.findViewById(R.id.tvname);
			TextView sex = view.findViewById(R.id.tvsex);
			TextView phone = view.findViewById(R.id.tvphone);
			ImageView iv = view.findViewById(R.id.imageView1);
			contactItem item = (contactItem) getItem(position);
			name.setText(item.getName());
			sex.setText(item.getSex());
			phone.setText(item.getPhone());
			iv.setImageResource(item.getImage());
		}
		else
			view = convertView;
		return view;
	}

}