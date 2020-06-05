package com.example.aforest.Main;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aforest.Dao.userDbHelper;
import com.example.aforest.R;
import com.example.aforest.home;


public class MainActivity extends AppCompatActivity {

	Button btLogin;
	TextView tvForget, tvRegister, tvIssue;
	ProgressBar pb;
	TextView tvProg, tvProgHint;
	EditText etUId, etPassword;
	View.OnClickListener oclLogin;

	private Handler handler;

	//public static MainActivity instance=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewId();   //绑定控件
		//显示欢迎界面
		// 初始化handler
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				findViewId();
				if (msg.what != 0x123) { // handler接收到相关的消息后
					setContentView(R.layout.welcome);
					pb = findViewById(R.id.progressBarWel);
					tvProg = findViewById(R.id.textViewProg);
					tvProgHint = findViewById(R.id.textViewProgHint);
					pb.setProgress(msg.what);//与UI进行交互
					tvProg.setText(msg.what + "%");
					tvProgHint.setText("系统初始化...");
					if (msg.what == 100) {
						pb.setProgress(msg.what);//与UI进行交互
						tvProg.setText(msg.what + "%");
						tvProgHint.setText("系统初始化完成");
					}
				}
				if (msg.what == 0x123) {

					setContentView(R.layout.activity_main);// 显示真正的应用界面
					findViewId();
					//使用条款超链接
					String html = "登录即代表阅读并同意服务条款";//超链接文本
					SpannableString ss = new SpannableString(html);//利用富文本类构建超链接
					ss.setSpan(new ClickableSpan() {
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							//setContentView(R.layout.registerlayout);
							Intent intent_issue = new Intent(MainActivity.this, serviceTerms.class);//点击超链接后进入用户注册界面
							startActivity(intent_issue);

						}
					}, 10, html.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					tvIssue.setText(ss);
					tvIssue.setMovementMethod(LinkMovementMethod.getInstance());
					tvIssue.setHighlightColor(Color.parseColor("#7CFC00"));//设置高亮颜色，不过并未变化

					//用户注册超链接
					String register = "用户注册";//超链接文本
					SpannableString regis = new SpannableString(register);//利用富文本类构建超链接
					regis.setSpan(new ClickableSpan() {
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							//setContentView(R.layout.registerlayout);
							Intent intent_regis = new Intent(MainActivity.this, userRegister.class);//点击超链接后进入用户注册界面
							startActivity(intent_regis);

						}
					}, 0, register.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					tvRegister.setText(regis);
					tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
					tvRegister.setHighlightColor(Color.parseColor("#FFFFFF"));//设置高亮颜色，不过并未变化

					//忘记密码超链接
					String forgetpwd = "忘记密码";//超链接文本
					SpannableString forget = new SpannableString(forgetpwd);//利用富文本类构建超链接
					forget.setSpan(new ClickableSpan() {
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							//setContentView(R.layout.registerlayout);
							Intent intent_regis = new Intent(MainActivity.this, forget_password.class);//点击超链接后进入用户注册界面
							startActivity(intent_regis);

						}
					}, 0, forget.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					tvForget.setText(forget);
					tvForget.setMovementMethod(LinkMovementMethod.getInstance());
					tvForget.setHighlightColor(Color.parseColor("#FFFFFF"));//设置高亮颜色，不过并未变化

					//createtable方法创建user表
					oclLogin = new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//获取昵称
							String uName = etUId.getText().toString();
							String uPassword = etPassword.getText().toString();

							//实例化一个dbhelper,传入context，数据库名，版本号
							userDbHelper dbhelper = new userDbHelper(MainActivity.this, "logintest", null, 2);
							dbhelper.createtable();//调用.length()!=0)//判断登录名和密码是否都已填写
							if (uName.length() * uPassword.length() != 0) {
								Cursor cursor = dbhelper.select(uName);//首先根据用户名进行查询，判断该用户是否存在
								if (cursor.getCount() > 0)//如果用户存在
								{
									cursor.moveToFirst();//定位游标到查询到的第一条记录
									String userpwd = cursor.getString(1);//获取记录中第二个字段（密码）的值
									if (userpwd.equals(uPassword))//判断用户输入的密码是否和数据库中查询到的密码一致
									{
										String status = cursor.getString(2);
										if (status.equals("管理员"))//判断是否是管理员用户，如果是则打开管理用户界面
										{
											Toast.makeText(getBaseContext(), "管理员登录成功", Toast.LENGTH_SHORT).show();
											Intent intent = new Intent(MainActivity.this, home.class);//打开管理用户界面
											intent.putExtra("uName", uName);//传递用户名信息
											startActivity(intent);
											//MainActivity.this.finish();
										} else if (status.equals("用户"))//如果是一般用户，则打开用户信息界面
										{
											Toast.makeText(getBaseContext(), "用户登录成功", Toast.LENGTH_SHORT).show();
											Intent intent = new Intent(MainActivity.this, home.class);//打开用户信息界面
											intent.putExtra("uName", uName);//传递用户名信息
											startActivity(intent);
											//MainActivity.this.finish();
										}
									} else//密码不正确时
										Toast.makeText(getBaseContext(), "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
								} else//找不到用户信息时
								{
									Toast.makeText(getBaseContext(), "该用户不存在", Toast.LENGTH_SHORT).show();
								}
							} else {//填入信息不全时
								Toast.makeText(getBaseContext(), "请填写必要的登录信息（用户名，密码）", Toast.LENGTH_SHORT).show();
							}
						}
					};
					btLogin.setOnClickListener(oclLogin);
				}
			}
		};
		//初始登录方式
		// 初始化handler
		//				handler = new Handler()
		//				{
		//					@Override
		//					public void handleMessage(Message msg)
		//					{
		//						if(msg.what == 0x123 ) // handler接收到相关的消息后
		//						{
		//							setContentView(R.layout.activity_main);// 显示真正的应用界面，主界面借鉴QQ登录界面
		//							findViewId();   //绑定控件
		//
		//							//使用条款超链接
		//							String html = "登录即代表阅读并同意服务条款";//超链接文本
		//							SpannableString ss=new SpannableString(html);//利用富文本类构建超链接
		//							ss.setSpan(new ClickableSpan() {
		//								@Override
		//								public void onClick(View v) {
		//									// TODO 自动生成的方法存根
		//									//setContentView(R.layout.registerlayout);
		//									Intent intent_issue=new Intent(MainActivity.this,serviceTerms.class);//点击超链接后进入用户注册界面
		//									startActivity(intent_issue);
		//
		//								}
		//							}, 10, html.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//							tvIssue.setText(ss);
		//							tvIssue.setMovementMethod(LinkMovementMethod.getInstance());
		//							tvIssue.setHighlightColor(Color.parseColor("#7CFC00"));//设置高亮颜色，不过并未变化
		//
		//							//用户注册超链接
		//							String register= "用户注册";//超链接文本
		//							SpannableString regis=new SpannableString(register);//利用富文本类构建超链接
		//							regis.setSpan(new ClickableSpan() {
		//								@Override
		//								public void onClick(View v) {
		//									// TODO 自动生成的方法存根
		//									//setContentView(R.layout.registerlayout);
		//									Intent intent_regis=new Intent(MainActivity.this,userRegister.class);//点击超链接后进入用户注册界面
		//									startActivity(intent_regis);
		//
		//								}
		//							}, 0, register.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		//							tvRegister.setText(regis);
		//							tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
		//							tvIssue.setHighlightColor(Color.parseColor("#FFFFFF"));//设置高亮颜色，不过并未变化
		//
		//							//登录按钮
		//							oclLogin = new View.OnClickListener() {
		//
		//								@Override
		//								public void onClick(View v) {
		//									// TODO Auto-generated method stub
		//
		//									//获取昵称
		//									String uName=etUId.getText().toString();
		//									String uPassword=etPassword.getText().toString();
		//									//进入Intent界面
		//									Intent intent1 = new Intent(MainActivity.this,home.class);
		//									intent1.putExtra("uName",uName);
		//									startActivity(intent1);
		//
		//								}
		//							};
		//							btLogin.setOnClickListener(oclLogin);
		//						}
		//					}
		//				};

		// 新建一个线程，过n秒钟后向handler发送一个消息，实现延迟显示登陆界面效果
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					for (int i = 0; i < 101; i++) {
						Thread.sleep(50);   //50ms每1%进度
						handler.sendEmptyMessage(i);//等待0.1秒传回消息
					}
					Thread.sleep(750);
					handler.sendEmptyMessage(0x123);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}


			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	//绑定控件
	public void findViewId() {
		pb = findViewById(R.id.progressBarWel);
		tvProg = findViewById(R.id.textViewProg);
		tvProgHint = findViewById(R.id.textViewProgHint);
		btLogin = findViewById(R.id.buttonRegister);
		tvForget = findViewById(R.id.textViewForget);
		tvIssue = findViewById(R.id.textViewIssue);
		tvRegister = findViewById(R.id.textViewRegister);
		etUId = findViewById(R.id.editTextUId);
		etPassword = findViewById(R.id.editTextPassword);
	}
}


