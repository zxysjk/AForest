package com.example.aforest.contactIncludeImage;

public class contactItem {
	private String name;
	private String sex;
	private String phone;
	private int image;

	contactItem(String name, String sex, String phone, int image) {
		this.name = name;
		this.sex = sex;
		this.phone = phone;
		this.image = image;
	}

	public String getName() {
		return this.name;
	}

	public String getSex() {
		return this.sex;
	}

	public String getPhone() {
		return this.phone;
	}

	public int getImage() {
		return this.image;
	}
}
