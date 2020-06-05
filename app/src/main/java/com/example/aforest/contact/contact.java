package com.example.aforest.contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.aforest.Dao.contactDbHelper;
import com.example.aforest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class contact extends AppCompatActivity {

	int sposition;

	ArrayList<Map<String, Object>> l;
	contactItem2 con;
	EditText etName, etPhone, etSex;
	String name, phone, sex;
	SimpleAdapter sa;
	contactDbHelper dbhelper;
	Button bt1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		this.setTitle("联系人");
		dbhelper = new contactDbHelper(this, "contact", null, 2);//实例化一个dbhelper,传入context，数据库名，版本号

		final ListView lv = findViewById(R.id.listViewContact);
		sa = new SimpleAdapter(this, getData(), R.layout.contact_item, new String[]{"username", "phone", "sex"},
				new int[]{R.id.tvname, R.id.tvphone, R.id.tvsex});

		lv.setAdapter(sa);//绑定数据到listview中显示
		registerForContextMenu(lv);//为listview注册上下文菜单

//		bt1=findViewById(R.id.buttonback4);
//		bt1.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				contact.this.finish();
//			}
//		});
	}

	private List<Map<String, Object>> getData() {
		Cursor cursor = dbhelper.selectall();//利用selectall方法获取user表中的所有记录，即全部用户的信息
		if (cursor.getCount() == 0) { //初始化添加几条数据
			dbhelper.insert("唐青枫", "13453543545", "男");
			dbhelper.insert("苏尘", "14355454545", "男");
			dbhelper.insert("柳如仪", "12545454545", "女");
			dbhelper.insert("沈浪", "14454656445", "男");

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

	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("菜单列表");// 为弹出的上下文菜单设置title
		getMenuInflater().inflate(R.menu.contact_manage_menu, menu);
	}


	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		sposition = menuInfo.position;
		switch (item.getItemId()) {
			case R.id.delete:
				AlertDialog.Builder dialogDelete = new AlertDialog.Builder(contact.this);
				//    设置Title的图标
				dialogDelete.setIcon(R.drawable.tree);
				//    设置Title的内容
				dialogDelete.setTitle("提示");

				Map<String, Object> map1 = l.get(sposition);

				name = map1.get("username").toString();
				phone = map1.get("phone").toString();
				String string = "确定要删除用户\n" + map1.get("username") + "," + map1.get("phone") + "吗？";
				dialogDelete.setMessage(string);
				//    设置一个PositiveButton
				dialogDelete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (l.remove(sposition) != null) {// 这行代码必须有
							dbhelper.delete(name, phone);
							System.out.println("success");
						} else {
							System.out.println("failed");
						}
						sa.notifyDataSetChanged();
						Toast.makeText(contact.this, "成功删除" + name, Toast.LENGTH_SHORT).show();
					}
				});
				//    设置一个NegativeButton
				dialogDelete.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(contact.this, "留在当前页面", Toast.LENGTH_SHORT).show();
					}
				});
				//显示该对话框
				dialogDelete.show();

				break;
			case R.id.modify:

				final Map<String, Object> map2 = l.get(sposition);
				name = map2.get("username").toString();
				LayoutInflater li = LayoutInflater.from(this);
				final View edit = li.inflate(R.layout.contact_info, null);
				AlertDialog.Builder dialogModify = new AlertDialog.Builder(contact.this);
				etName = (EditText) edit.findViewById(R.id.editName);
				etPhone = (EditText) edit.findViewById(R.id.editPhone);
				etSex = (EditText) edit.findViewById(R.id.editSex);
				etName.setText(map2.get("username").toString());
				etPhone.setText(map2.get("phone").toString());
				etSex.setText(map2.get("sex").toString());
				final String txtName = etName.getText().toString();

				dialogModify.setIcon(R.drawable.appicon).setTitle("联系人信息").setView(edit);
				dialogModify.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String txtnameRe = etName.getText().toString();
						String txtPhone = etPhone.getText().toString();
						String txtSex = etSex.getText().toString();
						if (txtnameRe.length() > 0) {
							map2.clear();
							map2.put("username", txtName);
							map2.put("phone", txtPhone);
							map2.put("sex", txtSex);
							if (l.set(sposition, map2) != null) {
								dbhelper.update(txtName, txtnameRe, txtSex, txtPhone);
								System.out.println("success");
							} else {
								System.out.println("failed");
							}
							sa.notifyDataSetChanged();
							Toast.makeText(getBaseContext(), "联系人已修改", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getBaseContext(), "请填写姓名", Toast.LENGTH_SHORT).show();
						}
					}
				});
				dialogModify.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(contact.this, "留在当前页面", Toast.LENGTH_SHORT).show();
					}
				});
				dialogModify.show();

				break;

			case R.id.add:

				LayoutInflater li2 = LayoutInflater.from(this);
				final View edit2 = li2.inflate(R.layout.contact_info, null);
				AlertDialog.Builder dialogAdd = new AlertDialog.Builder(contact.this);
				etName = (EditText) edit2.findViewById(R.id.editName);
				etPhone = (EditText) edit2.findViewById(R.id.editPhone);
				etSex = (EditText) edit2.findViewById(R.id.editSex);

				dialogAdd.setIcon(R.drawable.appicon).setTitle("联系人信息").setView(edit2);
				dialogAdd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String txtName = etName.getText().toString();
						String txtPhone = etPhone.getText().toString();
						String txtSex = etSex.getText().toString();
						if (txtName.length() > 0) {
							Cursor cursor = dbhelper.select(txtName, txtPhone);
							if (cursor.getCount() == 0) {//如果用户不重复

								Map<String, Object> map3 = new HashMap<>();
								map3.put("username", txtName);
								map3.put("phone", txtPhone);
								map3.put("sex", txtSex);
								if (l.add(map3)) {  //成功添加
									dbhelper.insert(txtName, txtPhone, txtSex);
									System.out.println("success");
								} else {
									System.out.println("failed");
								}
								sa.notifyDataSetChanged();
								Toast.makeText(getBaseContext(), "联系人添加成功", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getBaseContext(), "联系人已存在，请修改内容", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getBaseContext(), "请填写姓名", Toast.LENGTH_SHORT).show();
						}
					}
				});
				dialogAdd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(contact.this, "留在当前页面", Toast.LENGTH_SHORT).show();
					}
				});
				dialogAdd.show();
				break;
		}
		return true;
	}

}
