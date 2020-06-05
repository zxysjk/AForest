package com.example.aforest.Main;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aforest.Dao.userDbHelper;
import com.example.aforest.R;

public class userRegister extends AppCompatActivity {

	Button btRegister;
	EditText etUId, etPassword, etPasswordConfirm;
	TextView tvLogin, tvForget, tvIssue;
	String uName, pwd, pwd2;
	userDbHelper dbhelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_register);
		this.setTitle("用户注册");  //设置标题
		findViewId();   //绑定控件

		//使用条款超链接
		String html = "登录即代表阅读并同意服务条款";//超链接文本
		SpannableString ss = new SpannableString(html);//利用富文本类构建超链接
		ss.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent_issue = new Intent(userRegister.this, serviceTerms.class);//点击超链接后进入使用条款界面
				startActivity(intent_issue);

			}
		}, 10, html.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvIssue.setText(ss);
		tvIssue.setMovementMethod(LinkMovementMethod.getInstance());
		tvIssue.setHighlightColor(Color.parseColor("#7CFC00"));//设置高亮颜色，不过并未变化

		//忘记密码超链接
		String forgetpwd = "忘记密码";//超链接文本
		SpannableString forget = new SpannableString(forgetpwd);//利用富文本类构建超链接
		forget.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent_regis = new Intent(userRegister.this, forget_password.class);//点击超链接后进入用户注册界面
				startActivity(intent_regis);

			}
		}, 0, forget.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvForget.setText(forget);
		tvForget.setMovementMethod(LinkMovementMethod.getInstance());
		tvForget.setHighlightColor(Color.parseColor("#FFFFFF"));//设置高亮颜色，不过并未变化

		//用户登录超链接
		String Login = "用户登录";//超链接文本
		SpannableString login = new SpannableString(Login);//利用富文本类构建超链接
		login.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				//直接退出即可返回登录界面
				userRegister.this.finish();

			}
		}, 0, Login.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvLogin.setText(login);
		tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
		tvLogin.setHighlightColor(Color.parseColor("#FFFFFF"));//设置高亮颜色，不过并未变化

		View.OnClickListener ls = new View.OnClickListener() {//定义注册按钮的点击事件，完成用户注册操作
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//获取用户注册时各项输入内容
				uName = etUId.getText().toString();
				pwd = etPassword.getText().toString();
				pwd2 = etPasswordConfirm.getText().toString();
				//数据库插入数据
				dbhelper = new userDbHelper(userRegister.this, "logintest", null, 2);//实例化一个dbhelper,传入context，数据库名，版本号
				dbhelper.createtable();//调用createtable方法创建user表
				if (uName.length() * pwd.length() * pwd2.length() != 0)//判断必填项是否都填写了
				{
					if (uName.equals("admin")) {
						Toast.makeText(getBaseContext(), "管理员专属用户名，无法被注册", Toast.LENGTH_SHORT).show();
					} else {
						if (pwd.equals(pwd2))//判断两次填写的密码是否一致
						{
							Cursor cursor = dbhelper.select(uName);//首先根据用户名进行查询，判断是否已经存在同名用户
							if (cursor.getCount() == 0)//如果没有找到记录则可以注册
							{
								dbhelper.insert(uName, pwd);//通过insert方法在user表中插入一条记录，写入用户名、密码到对应字段
								Toast.makeText(getBaseContext(), "注册成功，返回登录界面", Toast.LENGTH_SHORT).show();
								userRegister.this.finish();
								Intent intent = new Intent(userRegister.this, MainActivity.class);//注册完成后进入注册成功界面
								startActivity(intent);
								userRegister.this.finish();
							} else//如果游标中有记录，则表示该用户以及被注册过了
							{
								Toast.makeText(getBaseContext(), "该用户名已被注册", Toast.LENGTH_SHORT).show();
							}
						} else//如果两次密码不相同
						{
							Toast.makeText(getBaseContext(), "两次密码设置内容不一致", Toast.LENGTH_SHORT).show();
						}
					}
				} else//如果必填项不完整
				{
					Toast.makeText(getBaseContext(), "请填写必要的信息（用户名，密码，确认密码）", Toast.LENGTH_SHORT).show();
				}
			}
		};
		btRegister.setOnClickListener(ls);
	}

	public void findViewId() {
		btRegister = findViewById(R.id.buttonRegister);
		tvLogin = findViewById(R.id.textViewLogin);
		tvIssue = findViewById(R.id.textViewIssue);
		tvForget = findViewById(R.id.textViewForget);
		etUId = findViewById(R.id.editTextUId);
		etPassword = findViewById(R.id.editTextPassword);
		etPasswordConfirm = findViewById(R.id.editTextPasswordConform);
	}
}

