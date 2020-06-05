package com.example.aforest.Main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.aforest.Dao.userDbHelper;
import com.example.aforest.R;

import java.util.Random;

public class forget_password extends AppCompatActivity {

	ImageButton bt1;
	Button bt2, bt3;
	String uName, pwdPb;
	EditText et1, et2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);

		et1 = findViewById(R.id.editTextName);
		et2 = findViewById(R.id.editTextPwdPb);


		final userDbHelper dbhelper = new userDbHelper(forget_password.this, "logintest", null, 2);
		dbhelper.createtable();//能够登录说明表已存在，不需要再建立，但也有可能数据库出现错误，一般情况能够正常运行，故此处注释

		bt1 = findViewById(R.id.buttonAdmin);
		bt1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//实例化一个dbhelper,传入context，数据库名，版本号
				Cursor cursor = dbhelper.select("admin");//首先根据用户名进行查询，判断该用户是否存在
				if (cursor.getCount() > 0)//如果用户存在
				{
					Toast.makeText(getApplicationContext(), "管理员已存在", Toast.LENGTH_SHORT).show();
				} else {
					dbhelper.insertAdmin("admin", "123456");
					dbhelper.close();
					Toast.makeText(getApplicationContext(), "管理员(admin,123456)创建成功", Toast.LENGTH_SHORT).show();
				}
			}
		});

		bt2 = findViewById(R.id.buttonPwdPb);
		bt2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uName = et1.getText().toString();
				pwdPb = et2.getText().toString();
				//实例化一个dbhelper,传入context，数据库名，版本号
				final Cursor cursor = dbhelper.select(uName);//首先根据用户名进行查询，判断该用户是否存在
				if (cursor.getCount() > 0)//如果用户存在
				{
					cursor.moveToFirst();//定位游标到查询到的第一条记录，因为用户名为主键，应该也只有一条数据

					//获取密码值，由于密码不能为空，但还是判断一下
					if (cursor.getString(10) == null || cursor.getString(1).equals("")) {  //获取记录中第4个字段（性别）的值，判断是否为空，并设置为空时的默认值，，以下同理
						Toast.makeText(getApplicationContext(), "用户未设置密保码，请联系管理员找回", Toast.LENGTH_SHORT).show();
					} else if (cursor.getString(10).equals(pwdPb)) {
						int flag = new Random().nextInt(999999);
						if (flag < 100000)
							flag += 100000;
						final String ran = String.valueOf(flag);//随机生成6位密码
						dbhelper.updatePassAuth(uName, ran, "用户", pwdPb);
						Toast.makeText(getApplicationContext(), "生成的密码为" + ran, Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), "密保码错误", Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(getApplicationContext(), "用户不存在", Toast.LENGTH_SHORT).show();
				}
			}
		});

		bt3 = findViewById(R.id.buttonback5);
		bt3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				forget_password.this.finish();
			}
		});

	}
}
