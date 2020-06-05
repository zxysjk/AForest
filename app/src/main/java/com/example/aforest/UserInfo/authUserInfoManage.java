package com.example.aforest.UserInfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aforest.Dao.userDbHelper;
import com.example.aforest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class authUserInfoManage extends AppCompatActivity {

	String uName, name;
	Button bt1;
	SimpleAdapter sa;
	ArrayList<Map<String, Object>> l;
	int sposition;
	userDbHelper dbhelper;
	TextView tv;
	public static authUserInfoManage instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_manage);
		//建立类实例工厂以实现其他activity关闭本activity
		instance = this;
		uName = this.getIntent().getStringExtra("uName");
		tv = findViewById(R.id.textViewAdmin);
		tv.setText("欢迎管理员：" + uName);

		dbhelper = new userDbHelper(this, "logintest", null, 2);//实例化一个dbhelper,传入context，数据库名，版本号
		dbhelper.createtable();//调用createtable方法创建user表

		ListView lv = this.findViewById(R.id.listView);
		////初始化一个simpleAdapter,分别用两个textview显示数据库中现有的用户名和密码，数据通过getdata方法获取
		sa = new SimpleAdapter(this, getData(), R.layout.user_item, new String[]{"username", "status"},
				new int[]{R.id.textView1, R.id.textView2});
		lv.setAdapter(sa);//绑定数据到listview中显示
		registerForContextMenu(lv);//为listview注册上下文菜单

		//返回按钮
		bt1 = findViewById(R.id.buttonback4);
		bt1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent intent_back=new Intent(authUserInfoManage.this,profile_info.class);
				//intent_back.putExtra("uName",uName);
				//startActivity(intent_back);
				authUserInfoManage.this.finish();
			}
		});

	}

	//获取当前所有用户的信息，返回一个list
	private List<Map<String, Object>> getData() {
		Cursor cursor = dbhelper.selectall();//利用selectall方法获取user表中的所有记录，即全部用户的信息
		l = new ArrayList<Map<String, Object>>();//创建一个list，每个元素为一个map

		if (cursor.moveToFirst())//定位游标到第一条数据
		{
			for (int i = 0; i < cursor.getCount(); i++)//遍历每一条记录
			{
				Map<String, Object> m;//用一个map来存放每个用户的信息
				m = new HashMap<String, Object>();
				m.put("username", cursor.getString(0));
				m.put("status", cursor.getString(2));
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
		getMenuInflater().inflate(R.menu.user_manage_menu, menu);
	}


	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		sposition = menuInfo.position;
		switch (item.getItemId()) {
			case R.id.delete:
				AlertDialog.Builder builder = new AlertDialog.Builder(authUserInfoManage.this);
				//    设置Title的图标
				builder.setIcon(R.drawable.tree);
				//    设置Title的内容
				builder.setTitle("提示");

				Map<String, Object> map1 = l.get(sposition);

				name = map1.get("username").toString();
				if (name.equals(uName)) {     //判断是否删除自己
					Toast.makeText(authUserInfoManage.this, "危险操作——您不能删除自己", Toast.LENGTH_SHORT).show();
				} else {
					String string = "确定要删除用户\n" + map1.get("username") + "," + map1.get("status") + "吗？";
					builder.setMessage(string);
					//    设置一个PositiveButton
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (l.remove(sposition) != null) {// 这行代码必须有
								dbhelper.delete(name);
								System.out.println("success");
							} else {
								System.out.println("failed");
							}
							sa.notifyDataSetChanged();
							Toast.makeText(authUserInfoManage.this, "成功删除" + name, Toast.LENGTH_SHORT).show();
						}
					});
					//    设置一个NegativeButton
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(authUserInfoManage.this, "留在当前页面", Toast.LENGTH_SHORT).show();
						}
					});
					//显示该对话框
					builder.show();
				}
				break;
			case R.id.modify:
				Map<String, Object> map2 = l.get(sposition);
				name = map2.get("username").toString();
				Intent intent_modify = new Intent(authUserInfoManage.this, authUserInfoModify.class);
				intent_modify.putExtra("username", name);   //发送选中用户name
				intent_modify.putExtra("uName", uName);     //发送当前管理员用户uName
				startActivity(intent_modify);
				//authUserInfoManage.this.finish();
				sa.notifyDataSetChanged();
				break;

			case R.id.passAuthMana:
				Map<String, Object> map3 = l.get(sposition);
				name = map3.get("username").toString();
				Intent intent_passauthmodify = new Intent(authUserInfoManage.this, authUserPassStatusModify.class);
				intent_passauthmodify.putExtra("username", name);   //发送选中用户name
				intent_passauthmodify.putExtra("uName", uName); //发送当前用户name
				startActivity(intent_passauthmodify);
				//authUserInfoManage.this.finish();
				sa.notifyDataSetChanged();
				break;
		}
		return true;
	}


}
