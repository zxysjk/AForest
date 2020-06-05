package com.example.aforest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.aforest.MusicPlayer.musicPlayer;
import com.example.aforest.UserInfo.profile_info;
import com.example.aforest.contact.contact;

import java.text.SimpleDateFormat;
import java.util.Date;

public class home extends AppCompatActivity {

	public static home instance = null;
	View.OnClickListener ocl1, ocl2, ocl3, ocl4, ocl5, ocl6, ocl7,ocl8,ocl9;
	TextView t1, t2;
	ImageButton ib1, ib2, ib3, ib4, ib5, ib6, ib7,ib8,ib9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setTitle("应用功能");
		//建立类实例工厂以实现其他activity关闭本activity
		instance = this;
		//获取昵称
		final String uName = this.getIntent().getStringExtra("uName");

		//显示当前时间
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		t1 = findViewById(R.id.text_time);
		t1.setText(simpleDateFormat.format(date));

		//个人信息按钮
		ib1 = this.findViewById(R.id.imageButton1);
		ocl1 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(home.this, profile_info.class);
				intent.putExtra("uName", uName);
				startActivity(intent);
				//home.this.finish();
			}
		};
		ib1.setOnClickListener(ocl1);

		//评分
		ib2 = this.findViewById(R.id.imageButton4);
		ocl2 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(home.this, userRate.class);
				intent2.putExtra("uName", uName);
				startActivity(intent2);
			}
		};
		ib2.setOnClickListener(ocl2);

		//联系人按钮
		ib3 = this.findViewById(R.id.imageButton3);
		ocl3 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent3 = new Intent(home.this, contact.class);
				startActivity(intent3);
			}
		};
		ib3.setOnClickListener(ocl3);

		//音乐播放器
		ib4 = this.findViewById(R.id.imageButton2);
		ocl4 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent4 = new Intent(home.this, musicPlayer.class);
				intent4.putExtra("uName", uName);
				startActivity(intent4);
			}
		};
		ib4.setOnClickListener(ocl4);

		//天气
		ib5 = this.findViewById(R.id.imageButton5);
		ocl5 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(home.this, weather.class));
			}
		};
		ib5.setOnClickListener(ocl5);

		//传感器
		ib6 = this.findViewById(R.id.imageButton6);
		ocl6 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(home.this, sensors.class));
			}
		};
		ib6.setOnClickListener(ocl6);

		//设备
		ib7 = this.findViewById(R.id.imageButton7);
		ocl7 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(home.this, device.class));
			}
		};
		ib7.setOnClickListener(ocl7);

		//计算器
		ib8= this.findViewById(R.id.imageButton8);
		ocl8=new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				startActivity(new Intent(home.this, calculator.class));
			}
		};
		ib8.setOnClickListener(ocl8);

		//计算器
		ib9= this.findViewById(R.id.imageButton9);
		ocl9=new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				startActivity(new Intent(home.this, bdmap.class));
			}
		};
		ib9.setOnClickListener(ocl9);

	}
}
