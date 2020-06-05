package com.example.aforest.Dao;

/**
 * @writer zxysjk
 * @time 2019-05-18.
 */
public class user {
	private String uId;
	private String uPassword;
	private String sex;
	private String birthday;
	private String email;
	private String phone;
	private String address;
	private String note;
	private String habit;

	public user(String uId, String uPassword, String sex, String birthday, String email, String phone, String address, String note, String habit) {
		this.uId = uId;
		this.uPassword = uPassword;
		this.sex = sex;
		this.birthday = birthday;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.note = note;
		this.habit = habit;
	}
	//默认初始密码123456
	public user(String uId, String sex, String birthday, String email, String phone, String address, String note, String habit) {
		this.uId = uId;
		this.uPassword = "123456";
		this.sex = sex;
		this.birthday = birthday;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.note = note;
		this.habit = habit;
	}


	public void setuId(String uId) {
		this.uId = uId;
	}

	public void setuPassword(String uPassword) {
		this.uPassword = uPassword;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setHabit(String habit) {
		this.habit = habit;
	}

	public String getuId() {
		return uId;
	}

	public String getuPassword() {
		return uPassword;
	}

	public String getSex() {
		return sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public String getNote() {
		return note;
	}

	public String getHabit() {
		return habit;
	}
}
