package com.example.aforest;

import android.app.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;


public class weather extends AppCompatActivity implements OnClickListener {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		setTitle("天气");
		//webView=(WebView)findViewById(R.id.webView1);
		webView=this.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl("http://www.weather.com.cn");

		Button bj=(Button)findViewById(R.id.bj);
		bj.setOnClickListener(this);

		Button sh=(Button)findViewById(R.id.sh);
		sh.setOnClickListener(this);

		Button heb=(Button)findViewById(R.id.sz);
		heb.setOnClickListener(this);

		Button cc=(Button)findViewById(R.id.wh);
		cc.setOnClickListener(this);

		Button sy=(Button)findViewById(R.id.xy);
		sy.setOnClickListener(this);

		Button gz=(Button)findViewById(R.id.gz);
		gz.setOnClickListener(this);
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
			case R.id.bj:
				openUrl("101010100");
				break;

			case R.id.sh:
				openUrl("101020100");
				break;

			case R.id.sz:
				openUrl("101280601");
				break;

			case R.id.wh:
				openUrl("101200101");
				break;

			case R.id.xy:
				openUrl("101200201");
				break;

			case R.id.gz:
				openUrl("101280101");
				break;
		}
	}

	private void openUrl(String id){
		webView.loadUrl("http://m.weather.com.cn/mweather/"+id+".shtml");
	}

}