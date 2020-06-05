package com.example.aforest.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.aforest.R;

public class serviceTerms extends AppCompatActivity {

	Button btback;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_terms);
		setTitle("软件服务协议");
		btback=findViewById(R.id.buttonback3);
		btback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				serviceTerms.this.finish();
			}
		});
	}
}
