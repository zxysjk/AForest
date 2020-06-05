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
import android.widget.TextView;
import android.widget.Toast;

import com.example.aforest.Dao.userDbHelper;
import com.example.aforest.R;
import com.example.aforest.contact.contact_forResult2;
import com.example.aforest.home;

import java.util.Calendar;


public class profile_edit extends AppCompatActivity {

	Button bt1, bt2, bt3, bt4, bt;
	View.OnClickListener ocl1, ocl2, ocl3, ocl4, ocl5;
	EditText etName, etBirth, etEmail, etPhone, etAddress, etNote, etHabit;
	RadioButton rb1, rb2;
	RadioGroup rg1;
	CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8;
	String uName, sex, birthday, email, phone, address, note, habit, nameRe;
	String male = "男", female = "女";
	int myYear, myMonth, myDay;

	static final int DATE_DIALOG_ID = 1;
	TextView ttv, dtv;
	//定义一个Calendar并获得系统的当前日期和时间
	final Calendar cal = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_edit);
		setTitle("信息修改");
		findViewId();  //绑定控件
		//获取用户名并设置editText
		uName = getIntent().getStringExtra("uName");
		etName.setText(uName);


		backButtonInit();   //返回按钮
		checkBind();        //多选框初始化
		dateButtonInit();   //日期按钮初始化
		contactInit();  //联系人初始化

		//实例化一个dbhelper,传入context，数据库名，版本号
		final userDbHelper dbhelper = new userDbHelper(profile_edit.this, "logintest", null, 2);
		dbhelper.createtable();//能够登录说明表已存在，不需要再建立，但也有可能数据库出现错误，一般情况能够正常运行，故此处注释
		final Cursor cursor = dbhelper.select(uName);//首先根据用户名进行查询，判断该用户是否存在
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
				if ((email.contains("@") && email.contains(".com")) || email.equals("")) {
					if ((phone.length() == 11 && phone.startsWith("1") )|| phone.equals("")) {
						if (uName.equals(nameRe)) {
							//尽管用户名没有变化，此处出于简洁,用统一update方法
							dbhelper.updateAll(uName, nameRe, sex, birthday, email, phone, address, note, habit);
							Toast.makeText(profile_edit.this, "修改完成，保存成功", Toast.LENGTH_SHORT).show();

							//返回profile界面，传入name参数，并关闭当前页面，实现动态显示效果
							profile_info.instance.finish();
							Intent intent = new Intent(profile_edit.this, profile_info.class);
							intent.putExtra("uName", uName);
							startActivity(intent);
							profile_edit.this.finish();
						} else {    //否则修改了用户名
							if (nameRe.equals("")) {      //用户名不能为空
								Toast.makeText(profile_edit.this, "禁止操作——用户名不能为空", Toast.LENGTH_SHORT).show();
							} else {
								Cursor cursor = dbhelper.select(nameRe);//首先根据更改后的用户名进行查询，判断该用户是否存在
								if (cursor.getCount() > 0) {    //如果用户存在
									Toast.makeText(profile_edit.this, "用户名已存在,请更换填写", Toast.LENGTH_SHORT).show();
								} else {
									dbhelper.updateAll(uName, nameRe, sex, birthday, email, phone, address, note, habit);
									Toast.makeText(profile_edit.this, "修改完成，保存成功", Toast.LENGTH_SHORT).show();
									cursor.close();
									dbhelper.close();
									home.instance.finish();
									Intent intent2 = new Intent(profile_edit.this, home.class);
									intent2.putExtra("uName", nameRe);
									startActivity(intent2);
									//返回profile界面，传入name参数，并关闭当前页面，实现动态显示效果
									Intent intent = new Intent(profile_edit.this, profile_info.class);
									intent.putExtra("uName", nameRe);
									startActivity(intent);

									profile_edit.this.finish();
								}
							}
						}

					} else {
						Toast.makeText(profile_edit.this, "电话格式错误", Toast.LENGTH_SHORT).show();
					}

				} else
					Toast.makeText(profile_edit.this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
			}
		};
		bt2.setOnClickListener(ocl1);
	}

	//返回键初始化
	private void backButtonInit() {
		bt1 = this.findViewById(R.id.back2);
		ocl1 = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(profile_edit.this);
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
						//						Intent intentback=new Intent(profile_edit.this,profile_info.class);
						//						intentback.putExtra("uName",uName);
						//						startActivity(intentback);
						profile_edit.this.finish();
						Toast.makeText(profile_edit.this, "修改结果未保存", Toast.LENGTH_SHORT).show();
					}
				});
				//    设置一个NegativeButton
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(profile_edit.this, "留在当前页面", Toast.LENGTH_SHORT).show();
					}
				});
				//    设置一个NeutralButton
				builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(profile_edit.this, "留在当前页面", Toast.LENGTH_SHORT).show();
					}
				});
				//显示该对话框
				builder.show();
			}
		};
		bt1.setOnClickListener(ocl1);
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

	public void getViewValue() {

		if (rb1.isChecked())
			sex = male;
		else if (rb2.isChecked())
			sex = female;
		else
			sex = "";

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
		bt4 = this.findViewById(R.id.buttonCalendar);
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
				Intent intent_contact = new Intent(profile_edit.this, contact_forResult2.class);
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
	//				Toast.makeText(profile_edit.this, year + "年" + (month + 1) + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT).show();
	//				etBirth.setText(date);
	//			}
	//		});
	//	}


}
