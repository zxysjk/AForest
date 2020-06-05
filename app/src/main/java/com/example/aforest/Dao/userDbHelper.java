package com.example.aforest.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @writer zxysjk
 * @time 2019-05-10.
 */
//创建一个DbHelper扩展SQLiteOpenHelper，实现SQLite数据库相关操作

/*增删改查本全为protected方法，此处为不同包调用改为public，实为不规范操作*/
public class userDbHelper extends SQLiteOpenHelper {
	Context context;//上下文变量，在构造函数中传入
	SQLiteDatabase db;//sqlite数据库对象，完成各类数据库操作
	String table_name = "user";//用于存放用户信息的表名
	final String Tag = "sqlop";//写入日志时用的标签
	String db_name;//数据库文件名，在构造函数中传入

	public userDbHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);//调用父类方法，根据传入的相关参数初始化数据库
		// TODO Auto-generated constructor stub
		this.context = context;
		this.db_name = name;
		//根据传入的数据库名打开或者创建一个数据库，返回sqlite对象
		//数据库文件默认存放在应用根目录的databases文件夹下，根据db_name命名，注意如果db_name没有后缀名，不会自动加.db
		db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
		//createtable();//创建用户表
	}

	public void createtable() {
		// TODO Auto-generated method stub
		try {
			//创建用户表的SQL语句
			String sql = "Create table if not exists " + table_name +
					"(username Varchar Primary Key not null, pwd Varchar not null,status Varchar," +
					"sex Varchar,birthday Varchar,email Varchar,phone Varchar," +
					"address Varchar,note Varchar,habit Varchar,pwdPb Varchar);";
			db.execSQL(sql);//执行SQL语句
			Log.v(Tag, "Create Table...");
		} catch (Exception e) {
			Log.v(Tag, e.toString());
		}
	}

	//插入新的用户，注册用户时使用
	public void insert(String UId, String password) {
		try {
			String status_default = "用户";
			db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);//首先根据数据库名打开数据库，返回sqlite对象
			//String sql = "Insert into " + table_name + " values('" + username +"','" + pwd +"','" + phone +"');";//构建插入用户的SQL
			String sql = "Insert into " + table_name +
					" (username,pwd,status) values('" + UId + "','" + password + "','" + status_default + "');";//构建插入用户的SQL
			db.execSQL(sql);//执行SQL
			Log.v(Tag, "Insert User...");
		} catch (Exception e) {
			Log.v(Tag, e.toString());
		}
	}

	//插入新的管理员
	public void insertAdmin(String UId, String password) {
		try {
			String status_default = "管理员";
			db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);//首先根据数据库名打开数据库，返回sqlite对象
			//String sql = "Insert into " + table_name + " values('" + username +"','" + pwd +"','" + phone +"');";//构建插入用户的SQL
			String sql = "Insert into " + table_name +
					" (username,pwd,status) values('" + UId + "','" + password + "','" + status_default + "');";//构建插入用户的SQL
			db.execSQL(sql);//执行SQL
			Log.v(Tag, "Insert User...");
		} catch (Exception e) {
			Log.v(Tag, e.toString());
		}
	}

	//删除记录，删除用户时使用
	public void delete(String username) {
		try {
			db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
			String sql = "delete from " + table_name +
					" where username='" + username + "';";
			db.execSQL(sql);
			Log.v(Tag, "Delete User...");
		} catch (Exception e) {
			Log.v(Tag, e.toString());
		}
	}

	//更新记录，修改用户资料时使用
	public void update(String username, String sex, String birthday, String email, String phone, String address, String note, String habit) {
		try {
			db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);

			String sql = "update " + table_name +
					" set sex='" + sex + "',birthday='" + birthday + "',email='" + email +
					"',phone='" + phone + "',address='" + address + "',note='" + note +
					"',habit='" + habit + "' where username='" + username + "';";
			db.execSQL(sql);
			Log.v(Tag, "Update UserInfo...");
		} catch (Exception e) {
			Log.v(Tag, e.toString());
		}
	}

	//更新记录，修改密码时使用
	public void updatePassAuth(String username, String password, String status,String pwdPb) {
		try {
			db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);

			String sql = "update " + table_name +
					" set pwd='" + password + "',status='" + status + "',pwdPb='"+pwdPb+"' where username='" + username + "';";
			db.execSQL(sql);
			Log.v(Tag, "Update UserPassword...");
		} catch (Exception e) {
			Log.v(Tag, e.toString());
		}
	}

	//更新记录，修改用户资料时使用
	public void updateAll(String username,String usernameRe, String sex, String birthday, String email, String phone, String address, String note, String habit) {
		try {
			db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);

			String sql = "update " + table_name +
					" set username='" + usernameRe + "',sex='" + sex +"',birthday='" + birthday + "',email='" + email +
					"',phone='" + phone + "',address='" + address + "',note='" + note +
					"',habit='" + habit + "' where username='" + username + "';";
			db.execSQL(sql);
			Log.v(Tag, "Update User...");
		} catch (Exception e) {
			Log.v(Tag, e.toString());
		}
	}

	//查询某一条记录，登录显示用户信息时使用
	public Cursor select(String username) {
		try {
			db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
			String sql = "username='" + username + "'";
			Cursor cr = db.query(table_name, null, sql, null, null, null, null);//通过query方法进行查询，返回一个游标
			Log.v(Tag, "Select User...");
			return cr;
		} catch (Exception e) {
			Log.v(Tag, e.toString());
			return null;
		}
	}

	//查询所有记录，管理员登录后使用
	public Cursor selectall() {
		try {
			db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
			Cursor cr = db.query(table_name, null, null, null, null, null, null);
			Log.v(Tag, "Select All...");
			return cr;
		} catch (Exception e) {
			Log.v(Tag, e.toString());
			return null;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
