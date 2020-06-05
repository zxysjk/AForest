package com.example.aforest.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aforest.R;

import java.util.List;

public class ImageAdapter2 extends BaseAdapter{
	List<contactItem2> ls;
	Context context;
	ImageAdapter2(Context context, List<contactItem2> ls)
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
			contactItem2 item = (contactItem2) getItem(position);
			name.setText(item.getName());
			sex.setText(item.getSex());
			phone.setText(item.getPhone());

		}
		else
			view = convertView;
		return view;
	}

}