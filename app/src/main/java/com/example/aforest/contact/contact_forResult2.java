package com.example.aforest.contact;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.aforest.Dao.contactDbHelper;
import com.example.aforest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class contact_forResult2 extends AppCompatActivity {

	OnItemClickListener ls1;
	contactDbHelper dbhelper;
	ArrayList<Map<String, Object>> l;
	Button bt1;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		setTitle("联系人列表");
		dbhelper = new contactDbHelper(this, "contact", null, 2);//实例化一个dbhelper,传入context，数据库名，版本号
		//dbhelper.createtable();//调用createtable方法创建user表

		final ListView lv = findViewById(R.id.listViewContact);

		SimpleAdapter sa = new SimpleAdapter(this, getData(), R.layout.contact_item, new String[]{"username", "phone","sex"},
				new int[]{R.id.tvname, R.id.tvphone,R.id.tvsex});

		lv.setAdapter(sa);//绑定数据到listview中显示
		//registerForContextMenu(lv);//为listview注册上下文菜单

		ls1 = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				Map<String, Object> map1 = l.get(position);
				//String name = map1.get("username").toString();
				String phone = map1.get("phone").toString();
				Intent intent = contact_forResult2.this.getIntent();
				intent.putExtra("phone", phone);
				contact_forResult2.this.setResult(4321, intent);
				contact_forResult2.this.finish();
			}
		};
		lv.setOnItemClickListener(ls1);

//		bt1=findViewById(R.id.buttonback4);
//		bt1.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				contact_forResult2.this.finish();
//			}
//		});

	}

//	private List<contactItem2> getdata() {
//		// TODO Auto-generated method stub
//		List<contactItem2> list = new ArrayList<contactItem2>();
//		//int i=R.drawable.pfo_alice;   可用i指代
//		contactItem2 item = new contactItem2("张宇", "男", "1345354354");
//		list.add(item);
//		item = new contactItem2("罗飞", "男", "1435545454");
//		list.add(item);
//		item = new contactItem2("柳青衣", "女", "1254545454");
//		list.add(item);
//		item = new contactItem2("李治", "男", "1445465656");
//		list.add(item);
//		return list;
//	}

	private List<Map<String, Object>> getData() {
		Cursor cursor = dbhelper.selectall();//利用selectall方法获取user表中的所有记录，即全部用户的信息
		if(cursor.getCount() == 0){ //初始化添加几条数据
			dbhelper.insert("唐青枫", "13453543545", "男");
			dbhelper.insert("凌晓", "14355454545", "男");
			dbhelper.insert("柳青衣", "12545454545", "女");
			dbhelper.insert("陈楚", "14454656445", "男");
		}
		l = new ArrayList<Map<String, Object>>();//创建一个list，每个元素为一个map

		if (cursor.moveToFirst())//定位游标到第一条数据
		{
			for (int i = 0; i < cursor.getCount(); i++)//遍历每一条记录
			{
				Map<String, Object> m;//用一个map来存放每个用户的信息
				m = new HashMap<String, Object>();
				m.put("username", cursor.getString(0));
				m.put("phone", cursor.getString(1));
				m.put("sex", cursor.getString(2));
				l.add(m);//将用户信息加入到list中
				cursor.moveToNext();//定位到下一条记录
			}
			return l;
		} else {
			return new ArrayList<Map<String, Object>>();    //如果没有数据返回空，不过必定不为空
		}
	}
}
