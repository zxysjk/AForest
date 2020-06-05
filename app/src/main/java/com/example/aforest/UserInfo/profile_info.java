package com.example.aforest.UserInfo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aforest.Dao.userDbHelper;
import com.example.aforest.Main.MainActivity;
import com.example.aforest.R;
import com.example.aforest.home;


public class profile_info extends AppCompatActivity {

	Button bt1, bt2, bt3,bt4;
	TextView tvName, tvSex, tvBirth, tvEmail, tvPhone, tvAddress, tvNote, tvHabit, tvStatus,tvManage;
	View.OnClickListener ocl1, ocl2, ocl3,ocl4;
	String uName, sex, birthday, email, phone, address, note, habit,status;
	String status_admin = "管理员";
	public static profile_info instance=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_info);
		setTitle("个人信息");

		//建立类实例工厂以实现其他activity关闭本activity
		instance=this;

		//绑定控件
		findViewId();
		//设置并接受姓名intent
		uName = this.getIntent().getStringExtra("uName");
		tvName.setText(uName);


		//实例化一个dbhelper,传入context，数据库名，版本号
		userDbHelper dbhelper = new userDbHelper(profile_info.this, "logintest", null, 2);
		dbhelper.createtable();//能够登录说明表已存在，不需要再建立，但也有可能数据库出现错误，一般情况能够正常运行，故此处注释
		Cursor cursor = dbhelper.select(uName);//首先根据用户名进行查询，判断该用户是否存在
		if (cursor.getCount() > 0)//如果用户存在
		{
			cursor.moveToFirst();//定位游标到查询到的第一条记录，因为用户名为主键，应该也只有一条数据
			status = textInit(cursor.getString(2), tvStatus);  //自定义函数判断是否为空
			sex = textInit(cursor.getString(3), tvSex);
			birthday = textInit(cursor.getString(4), tvBirth);
			email = textInit(cursor.getString(5), tvEmail);
			phone = textInit(cursor.getString(6), tvPhone);
			address = textInit(cursor.getString(7), tvAddress);
			note = textInit(cursor.getString(8), tvNote);
			habit = textInit(cursor.getString(9), tvHabit);
		}

		cursor.close();
		dbhelper.close();


		//进入修改信息界面，并传入用户名
		ocl1 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//传入用户名
				Intent intent = new Intent(profile_info.this, profile_edit.class);
				intent.putExtra("uName", uName);
				startActivity(intent);
				//profile_info.this.finish();
			}
		};
		bt1.setOnClickListener(ocl1);

		//返回键
		ocl2 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				profile_info.this.finish();
			}
		};
		bt2.setOnClickListener(ocl2);

		//如果是管理员，则显示用户管理提示
		if (status.equals(status_admin)) {
			tvManage.setVisibility(View.VISIBLE);
		}
		//用户管理
		String html = "您有管理员权限：点此管理用户";//超链接文本
		SpannableString ss = new SpannableString(html);//利用富文本类构建超链接
		ss.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent_userManage = new Intent(profile_info.this, authUserInfoManage.class);
				intent_userManage.putExtra("uName", uName);
				startActivity(intent_userManage);


			}
		}, 8, html.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvManage.setText(ss);
		tvManage.setMovementMethod(LinkMovementMethod.getInstance());
		tvManage.setHighlightColor(Color.parseColor("#7CFC00"));//设置高亮颜色，不过并未变化

		//修改密码按钮
		ocl3=new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent_pass = new Intent(profile_info.this, userPassModify.class);
				intent_pass.putExtra("uName", uName);
				startActivity(intent_pass);
			}
		};
		bt3.setOnClickListener(ocl3);


		//退出登录按钮
		ocl4=new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			profile_info.this.finish();
			home.instance.finish(); //同时关闭home界面，让用户无法返回
			}
		};
		bt4.setOnClickListener(ocl4);

	}

	//editText初始化函数，值传递，全局变量value不会变化
	//	public String textInit(String column, String value, TextView tv) {
	//		if (column == null || column.equals("")) {  //获取记录中第4个字段（性别）的值，判断是否为空，并设置为空时的默认值，，以下同理
	//			value = "未填写";
	//			tv.setText(value);
	//			return value;
	//		} else {
	//			value = column;
	//			tv.setText(value);
	//			return value;
	//		}
	//	}

	//editText初始化函数,址传递
	public String textInit(String column, TextView tv) {
		String value;
		if (column == null || column.equals("")) {  //获取记录中第n字段的值，判断是否为空，并设置为空时的默认值
			value = "未填写";
			tv.setText(value);
			return value;
		} else {
			value = column;
			tv.setText(value);
			return value;
		}
	}


	public void findViewId() {
		bt1 = findViewById(R.id.prof_edit);
		bt2 = findViewById(R.id.back1);
		bt3=findViewById(R.id.buttonPassModify);
		bt4=findViewById(R.id.buttonExit);
		tvName = findViewById(R.id.textView);
		tvSex = findViewById(R.id.textView10);
		tvBirth = findViewById(R.id.textView11);
		tvEmail = findViewById(R.id.textView12);
		tvPhone = findViewById(R.id.textView13);
		tvAddress = findViewById(R.id.textView14);
		tvNote = findViewById(R.id.textView15);
		tvHabit = findViewById(R.id.textHabit);
		tvStatus = findViewById(R.id.textStatus);
		tvManage=findViewById(R.id.textViewManage);
	}

}
