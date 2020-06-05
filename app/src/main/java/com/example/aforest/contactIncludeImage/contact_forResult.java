package com.example.aforest.contactIncludeImage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.aforest.R;

import java.util.ArrayList;
import java.util.List;

public class contact_forResult extends Activity{
	List<contactItem> ls;
	ImageAdapter sa;
	OnItemClickListener ls1;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView lv = new ListView(this);
		ls = getdata();
		sa = new ImageAdapter(this, ls);
		ls1 = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				contactItem item = ls.get(position);
				//String name = item.getName();
				String phone = item.getPhone();
				Intent intent = contact_forResult.this.getIntent();
				intent.putExtra("phone", phone);
				contact_forResult.this.setResult(4321, intent);
				contact_forResult.this.finish();
			}};
		lv.setOnItemClickListener(ls1);
		lv.setAdapter(sa);
		setContentView(lv);
	}

	private List<contactItem> getdata() {
		// TODO Auto-generated method stub
		List<contactItem> list = new ArrayList<contactItem>();
		//int i=R.drawable.pfo_alice;   可用i指代
		contactItem item = new contactItem("张宇", "男", "1345354354", R.drawable.pfo_alice);
		list.add(item);
		item = new contactItem("罗飞", "男", "1435545454", R.drawable.pfo_aquarium);
		list.add(item);
		item = new contactItem("柳青衣", "女", "1254545454", R.drawable.pfo_girl);
		list.add(item);
		item = new contactItem("李治", "男", "1445465656", R.drawable.pfo_xuan);
		list.add(item);
		return list;
	}
}
