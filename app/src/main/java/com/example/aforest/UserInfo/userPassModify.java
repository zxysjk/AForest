package com.example.aforest.UserInfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aforest.Dao.userDbHelper;
import com.example.aforest.R;

public class userPassModify extends AppCompatActivity {

	Button bt1, bt2;

	View.OnClickListener ocl1;
	EditText etPassword, etPwdPb;
	TextView tvName, tv1;
	RadioButton rb1, rb2;
	RadioGroup rg1;
	String uName, password, status, pwdPb;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_pass_auth_manage);

		setTitle("用户密码修改");
		findViewId();  //绑定控件
		rg1.setVisibility(View.INVISIBLE);
		tv1.setVisibility(View.INVISIBLE);

		uName = this.getIntent().getStringExtra("uName");
		tvName.setText(uName);

		//实例化一个dbhelper,传入context，数据库名，版本号
		final userDbHelper dbhelper = new userDbHelper(userPassModify.this, "logintest", null, 2);
		dbhelper.createtable();//能够登录说明表已存在，不需要再建立，但也有可能数据库出现错误，一般情况能够正常运行，故此处注释
		final Cursor cursor = dbhelper.select(uName);//首先根据用户名进行查询，判断该用户是否存在
		if (cursor.getCount() > 0)//如果用户存在
		{
			cursor.moveToFirst();//定位游标到查询到的第一条记录，因为用户名为主键，应该也只有一条数据

			//获取密码值，由于密码不能为空，但还是判断一下
			if (cursor.getString(1) == null || cursor.getString(1).equals("")) {  //获取记录中第4个字段（性别）的值，判断是否为空，并设置为空时的默认值，，以下同理
				password = "123456";
			} else {
				password = cursor.getString(1);
			}
			etPassword.setText(password);

			if (cursor.getString(10) == null || cursor.getString(10).equals("")) {  //获取记录中第4个字段（性别）的值，判断是否为空，并设置为空时的默认值，，以下同理
				pwdPb = "";
			} else {
				pwdPb = cursor.getString(10);
			}
			etPwdPb.setText(pwdPb);

			if (cursor.getString(2) == null) {//应该不为空，但避免为空报错
				status = "用户";
			} else {
				status = cursor.getString(2);
			}

		}

		//返回按钮
		backButtonInit();
		//保存按钮初始化
		ocl1 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//获取控件值
				password = etPassword.getText().toString();
				pwdPb = etPwdPb.getText().toString();

				if (pwdPb.equals(""))
					Toast.makeText(userPassModify.this, "未填写密保码", Toast.LENGTH_SHORT).show();
				//判断密码是否为空
				if (password.equals("")) {
					Toast.makeText(userPassModify.this, "密码不能为空", Toast.LENGTH_SHORT).show();
				} else {
					dbhelper.updatePassAuth(uName, password, status, pwdPb);
					Toast.makeText(userPassModify.this, "密码修改成功，请记好您的密码", Toast.LENGTH_LONG).show();
					dbhelper.close();
					userPassModify.this.finish();
				}
			}
		};
		bt2.setOnClickListener(ocl1);
	}


	public void findViewId() {

		tvName = findViewById(R.id.textView23);
		etPassword = findViewById(R.id.editTextPwd2);
		etPwdPb = findViewById(R.id.editTextPwdPb);

		tv1 = findViewById(R.id.textView2);
		rg1 = findViewById(R.id.radiogroup1);
		rb1 = findViewById(R.id.radioButton14);
		rb2 = findViewById(R.id.radioButton15);

		bt1 = this.findViewById(R.id.back2);
		bt2 = this.findViewById(R.id.save);
	}

	//返回键初始化
	private void backButtonInit() {

		bt1 = this.findViewById(R.id.back2);
		ocl1 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(userPassModify.this);
				//    设置Title的图标
				builder.setIcon(R.drawable.tree);
				//    设置Title的内容
				builder.setTitle("提示");
				//    设置Content来显示一个信息
				builder.setMessage("修改未保存，您确定要退出吗？");
				//    设置一个PositiveButton
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						userPassModify.this.finish();
						Toast.makeText(userPassModify.this, "修改结果未保存", Toast.LENGTH_SHORT).show();
					}
				});
				//    设置一个NegativeButton
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(userPassModify.this, "留在当前页面", Toast.LENGTH_SHORT).show();
					}
				});
				//    设置一个NeutralButton
				builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(userPassModify.this, "留在当前页面", Toast.LENGTH_SHORT).show();
					}
				});
				//显示该对话框
				builder.show();
			}
		};
		bt1.setOnClickListener(ocl1);
	}


}
