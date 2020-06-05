package com.example.aforest.UserInfo;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.aforest.Dao.userDbHelper;
import com.example.aforest.R;
import com.example.aforest.contact.contact_forResult2;
import com.example.aforest.home;


public class authUserInfoModify extends AppCompatActivity {

	Button bt1, bt2, bt3, bt4;
	String uName;
	View.OnClickListener ocl1, ocl2, ocl3, ocl4, ocl5;
	EditText etName, etBirth, etEmail, etPhone, etAddress, etNote, etHabit;
	RadioButton rb1, rb2;
	RadioGroup rg1;
	CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8;

	String name, sex, birthday, email, phone, address, note, habit, nameRe;
	String male = "男", female = "女";
	int myYear, myMonth, myDay;
	static final int DATE_DIALOG_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_edit);

		findViewId();  //绑定控件
		uName = this.getIntent().getStringExtra("uName");
		//获取用户名并设置editText
		name = this.getIntent().getStringExtra("username");
		etName.setText(name);

		backButtonInit();   //返回按钮
		checkBind();        //多选框初始化
		dateButtonInit();

		contactInit();  //联系人初始化

		//实例化一个dbhelper,传入context，数据库名，版本号
		final userDbHelper dbhelper = new userDbHelper(authUserInfoModify.this, "logintest", null, 2);
		dbhelper.createtable();//能够登录说明表已存在，不需要再建立，但也有可能数据库出现错误，一般情况能够正常运行，故此处注释
		final Cursor cursor = dbhelper.select(name);//首先根据用户名进行查询，判断该用户是否存在
		if (cursor.getCount() > 0)//如果用户存在
		{
			cursor.moveToFirst();//定位游标到查询到的第一条记录，因为用户名为主键，应该也只有一条数据
			//单选、多选框初始化
			checkInit(cursor);
			//editText初始化
			birthday = editTextInit(cursor.getString(4), etBirth);//获取记录中第5个字段（biryhday）的值，判断是否为空，并设置为空时的默认值，以下同理
			email = editTextInit(cursor.getString(5), etEmail);
			phone = editTextInit(cursor.getString(6), etPhone);
			address = editTextInit(cursor.getString(7), etAddress);
			note = editTextInit(cursor.getString(8), etNote);
			habit = editTextInit(cursor.getString(9), etHabit);
		}


		//保存按钮初始化
		ocl1 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//获取控件值
				getViewValue();
				//判断是否更改自己以及用户名修改，如果是则更新uName
				if (uName.equals(name)) {   //如果修改的是自己
					if (name.equals(nameRe)) {//尽管用户名没有变化，此处出于简洁用统一方法
						dbhelper.updateAll(uName, nameRe, sex, birthday, email, phone, address, note, habit);

						authUserInfoManage.instance.finish();
						//返回userManage界面，传入name参数，并关闭当前页面，实现动态显示效果
						Intent intent2 = new Intent(authUserInfoModify.this, authUserInfoManage.class);
						intent2.putExtra("uName", uName);
						startActivity(intent2);
						authUserInfoModify.this.finish();
						Toast.makeText(authUserInfoModify.this, "信息修改成功", Toast.LENGTH_SHORT).show();
					} else {
						if (nameRe.equals("")) {      //用户名不能为空
							Toast.makeText(authUserInfoModify.this, "禁止操作——用户名不能为空", Toast.LENGTH_SHORT).show();
						} else {
							Cursor cursor = dbhelper.select(nameRe);//首先根据更改后的用户名进行查询，判断该用户是否存在
							if (cursor.getCount() > 0) {    //如果用户存在
								Toast.makeText(authUserInfoModify.this, "用户已存在,请更换填写", Toast.LENGTH_SHORT).show();
							} else {
								dbhelper.updateAll(uName, nameRe, sex, birthday, email, phone, address, note, habit);
								uName = nameRe;     //修改后的用户名
								home.instance.finish();
								profile_info.instance.finish();
								//返回home界面，传入uName参数，并关闭当前页面，实现动态显示效果
								Intent intent = new Intent(authUserInfoModify.this, home.class);
								intent.putExtra("uName", uName);
								startActivity(intent);
								//返回profile_info界面，传入uName参数，并关闭当前页面，实现动态显示效果
								Intent intent1 = new Intent(authUserInfoModify.this, profile_info.class);
								intent1.putExtra("uName", uName);
								startActivity(intent1);
								//返回authUserInfoManage界面，传入uName参数，并关闭当前页面，实现动态显示效果
								Intent intent2 = new Intent(authUserInfoModify.this, authUserInfoManage.class);
								intent2.putExtra("uName", uName);
								startActivity(intent2);

								authUserInfoModify.this.finish();
								Toast.makeText(authUserInfoModify.this, "信息修改成功", Toast.LENGTH_SHORT).show();
							}
						}

					}
				} else {     //否则修改的为其他用户
					if (name.equals(nameRe)) {
						//尽管用户名没有变化，此处出于简洁用统一方法
						dbhelper.updateAll(name, nameRe, sex, birthday, email, phone, address, note, habit);
						authUserInfoManage.instance.finish();
						//返回userManage界面，传入name参数，并关闭当前页面，实现动态显示效果
						Intent intent2 = new Intent(authUserInfoModify.this, authUserInfoManage.class);
						intent2.putExtra("uName", uName);
						startActivity(intent2);
						authUserInfoModify.this.finish();
						Toast.makeText(authUserInfoModify.this, "信息修改成功", Toast.LENGTH_SHORT).show();
					} else {
						if (nameRe.equals("")) {      //用户名不能为空
							Toast.makeText(authUserInfoModify.this, "禁止操作——用户名不能为空", Toast.LENGTH_SHORT).show();
						} else {
							Cursor cursor = dbhelper.select(nameRe);//首先根据更改后的用户名进行查询，判断该用户是否存在
							if (cursor.getCount() > 0) {    //如果用户存在
								Toast.makeText(authUserInfoModify.this, "用户已存在,请更换填写", Toast.LENGTH_SHORT).show();
							} else {
								dbhelper.updateAll(name, nameRe, sex, birthday, email, phone, address, note, habit);
								Toast.makeText(authUserInfoModify.this, "修改完成，保存成功", Toast.LENGTH_SHORT).show();

								authUserInfoManage.instance.finish();
								//返回userManage界面，传入name参数，并关闭当前页面，实现动态显示效果
								Intent intent2 = new Intent(authUserInfoModify.this, authUserInfoManage.class);
								intent2.putExtra("uName", uName);
								startActivity(intent2);
								authUserInfoModify.this.finish();
								Toast.makeText(authUserInfoModify.this, "信息修改成功", Toast.LENGTH_SHORT).show();
							}
						}
					}
				}
			}
		};
		bt2.setOnClickListener(ocl1);


	}

	public void getViewValue() {

		if (rb1.isChecked())
			sex = male;
		else if (rb2.isChecked())
			sex = female;

		nameRe = etName.getText().toString();
		birthday = etBirth.getText().toString();
		email = etEmail.getText().toString();
		phone = etPhone.getText().toString();
		address = etAddress.getText().toString();
		note = etNote.getText().toString();
		habit = etHabit.getText().toString();
	}

	public void checkInit(Cursor cursor) {
		//判断性别单选框
		if (cursor.getString(3) == null) {
		} else {
			sex = cursor.getString(3);
			if (sex.equals(male))   //a==b指针地址必不相等，需用a.equals(b)判断相等
			{
				rb1.setChecked(true);
			} else if (sex.equals(female)) {
				rb2.setChecked(true);
			}
		}
		//判断爱好多选框
		if (cursor.getString(9) == null) {
		} else {
			String habits = cursor.getString(9);
			for (int cb = 0; cb <= habits.length() - 2; cb += 3) {
				if (habits.subSequence(cb, cb + 2).equals("美食")) {
					cb1.setChecked(true);
				}
				if (habits.subSequence(cb, cb + 2).equals("游戏")) {
					cb2.setChecked(true);
				}
				if (habits.subSequence(cb, cb + 2).equals("电影")) {
					cb3.setChecked(true);
				}
				if (habits.subSequence(cb, cb + 2).equals("动漫")) {
					cb4.setChecked(true);
				}
				if (habits.subSequence(cb, cb + 2).equals("音乐")) {
					cb5.setChecked(true);
				}
				if (habits.subSequence(cb, cb + 2).equals("运动")) {
					cb6.setChecked(true);
				}
				if (habits.subSequence(cb, cb + 2).equals("阅读")) {
					cb7.setChecked(true);
				}
				if (habits.subSequence(cb, cb + 2).equals("科技")) {
					cb8.setChecked(true);
				}
			}
		}
	}

	public void findViewId() {
		etName = findViewById(R.id.editText1);
		etBirth = findViewById(R.id.editText2);
		etEmail = findViewById(R.id.editText3);
		etPhone = findViewById(R.id.editText4);
		etAddress = findViewById(R.id.editText5);
		etNote = findViewById(R.id.editText6);
		etHabit = findViewById(R.id.editText7);

		rg1 = findViewById(R.id.radiogroup1);
		rb1 = findViewById(R.id.radioButton14);
		rb2 = findViewById(R.id.radioButton15);

		cb1 = findViewById(R.id.checkBox1);
		cb2 = findViewById(R.id.checkBox2);
		cb3 = findViewById(R.id.checkBox3);
		cb4 = findViewById(R.id.checkBox4);
		cb5 = findViewById(R.id.checkBox5);
		cb6 = findViewById(R.id.checkBox6);
		cb7 = findViewById(R.id.checkBox7);
		cb8 = findViewById(R.id.checkBox8);

		bt1 = this.findViewById(R.id.back2);
		bt2 = this.findViewById(R.id.save);
	}

	//editText初始化函数
	public String editTextInit(String column, EditText et) {
		String value;
		if (column == null || column.equals("")) {  //获取记录中第4个字段（性别）的值，判断是否为空，并设置为空时的默认值，，以下同理
			value = "";
			et.setText(value);
			et.setHint("未填写");
			return value;
		} else {
			value = column;
			et.setText(value);
			return value;
		}
	}

	//多选框初始化
	private void checkBind() {
		// TODO Auto-generated method stub
		ocl3 = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String habit = "";
				if (cb1.isChecked())
					habit += "," + cb1.getText().toString();
				if (cb2.isChecked())
					habit += "," + cb2.getText().toString();
				if (cb3.isChecked())
					habit += "," + cb3.getText().toString();
				if (cb4.isChecked())
					habit += "," + cb4.getText().toString();
				if (cb5.isChecked())
					habit += "," + cb5.getText().toString();
				if (cb6.isChecked())
					habit += "," + cb6.getText().toString();
				if (cb7.isChecked())
					habit += "," + cb7.getText().toString();
				if (cb8.isChecked())
					habit += "," + cb8.getText().toString();
				if (habit.length() == 0)
					habit = "";
				else
					habit = habit.substring(1);
				etHabit.setText(habit);
			}
		};
		cb1.setOnClickListener(ocl3);
		cb2.setOnClickListener(ocl3);
		cb3.setOnClickListener(ocl3);
		cb4.setOnClickListener(ocl3);
		cb5.setOnClickListener(ocl3);
		cb6.setOnClickListener(ocl3);
		cb7.setOnClickListener(ocl3);
		cb8.setOnClickListener(ocl3);
	}


	//日历按钮初始化
	private void dateButtonInit() {
		bt4 = findViewById(R.id.buttonCalendar);
		ocl5 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//定义一个设置文本框中显示日期的按钮
				showDialog(DATE_DIALOG_ID);
			}
		};
		bt4.setOnClickListener(ocl5);
	}

	//定义时间和日期弹出对话框
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, dls, myYear, myMonth, myDay);
		}
		return null;
	}

	//为弹出的日期对话框设置监听
	private DatePickerDialog.OnDateSetListener dls =
			new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year, int monthOfYear,
				                      int dayOfMonth) {
					myYear = year;
					myMonth = monthOfYear;
					myDay = dayOfMonth;
					etBirth.setText(myYear + "/" + (myMonth + 1) + "/" + myDay);
				}
			};

	//联系人
	private void contactInit() {
		// TODO Auto-generated method stub

		bt3 = findViewById(R.id.buttonContact);
		etPhone = findViewById(R.id.editText4);
		ocl4 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent_contact = new Intent(authUserInfoModify.this, contact_forResult2.class);
				startActivityForResult(intent_contact, 1234);
			}
		};
		bt3.setOnClickListener(ocl4);
	}

	//联系人响应函数，传回电话号码
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1234 && resultCode == 4321) {
			String phone = data.getStringExtra("phone");
			etPhone.setText(phone);
		}
	}

	//	//日历选择作用于editText
	//	private void calendarInit() {
	//		cv1 = findViewById(R.id.calendarView);
	//		etBirth = findViewById(R.id.editText2);
	//		cv1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
	//			@Override
	//			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
	//				//显示用户选择的日期
	//				String date = String.valueOf(year) + '/' + String.valueOf(month + 1) + '/' + String.valueOf(dayOfMonth);
	//				Toast.makeText(authUserInfoModify.this, year + "年" + (month + 1) + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT).show();
	//				etBirth.setText(date);
	//			}
	//		});
	//	}

	//返回键初始化
	private void backButtonInit() {

		bt1 = this.findViewById(R.id.back2);
		ocl1 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(authUserInfoModify.this);
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
						authUserInfoModify.this.finish();
						Toast.makeText(authUserInfoModify.this, "修改结果未保存", Toast.LENGTH_SHORT).show();
					}
				});
				//    设置一个NegativeButton
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(authUserInfoModify.this, "留在当前页面", Toast.LENGTH_SHORT).show();
					}
				});
				//    设置一个NeutralButton
				builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(authUserInfoModify.this, "留在当前页面", Toast.LENGTH_SHORT).show();
					}
				});
				//显示该对话框
				builder.show();
			}
		};
		bt1.setOnClickListener(ocl1);
	}


}
