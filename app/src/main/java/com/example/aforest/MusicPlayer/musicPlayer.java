package com.example.aforest.MusicPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aforest.R;

import java.io.IOException;
import java.util.ArrayList;

public class musicPlayer extends AppCompatActivity implements OnClickListener {


	ImageButton ibtn1, ibtn2, ibtn3, ibtn4, ibtn5;
	ListView lv;
	int cposition;//记录当前播放的曲目位置
	private static final String TAG = "PlayMusic";
	String[] musics, files;
	int playstatu = -1;//记录当前是否正在播放

	private MyReceiver receiver = null;
	TextView tv1;

	int barProgress = 0, barMax = 24000;
	public SeekBar seekBar;

	private Handler handler;// 处理改变进度条事件
	int UPDATE = 0x101;
	private boolean autoChange, manulChange;// 判断是进度条是自动改变还是手动改变
	private boolean isPause;// 判断是从暂停中恢复还是重新播放

	protected void onCreate(Bundle savedInstanceState) {

		playstatu = 0;
		cposition = 0;

		musicPlayer.this.setTitle("音乐播放器");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_player);


		//		此处也可用handler动态显示
		//		handler = new Handler() {
		//			@Override
		//			public void handleMessage(Message msg) {
		//				super.handleMessage(msg);
		//
		//				if (msg.what == UPDATE) {
		//					//更新UI
		//					int cur = msg.arg1;
		//					int total = msg.arg2;
		//					int max = seekBar.getMax();
		//					int curSecond = cur / 1000 % 60;
		//					int curMin = cur / 1000 / 60;
		//					int totalSecond = total / 1000 % 60;
		//					int totalMin = total / 1000 / 60;
		//
		//
		//					try {
		//						tv1.setText(curMin + ":" + curSecond + "/" + totalMin + ":" + totalSecond);
		//
		//						//为整数
		//						seekBar.setProgress(cur * max / total);
		//
		//
		//					} catch (Exception e) {
		//						e.printStackTrace();
		//					}
		//				} else {
		//					//seekBar.setProgress(0);
		//				}
		//			}
		//		};
		//
		//
		//		// 新建一个线程，过n秒钟后向handler发送一个消息，实现延迟显示登陆界面效果
		//		Runnable runnable = new Runnable() {
		//			public void run() {
		//				while (receiver != null) {
		//					try {
		//						int position, mMax, sMax;
		//
		//						Message m = handler.obtainMessage();//获取一个Message
		//						m.arg1 = receiver.getCur();
		//						m.arg2 = receiver.getTotal();
		//						m.what = UPDATE;
		//						handler.sendMessage(m);
		//						//  handler.sendEmptyMessage(UPDATE);
		//						Thread.sleep(750);
		//					} catch (Exception e) {
		//						e.printStackTrace();
		//					}
		//				}
		//			}
		//		};
		//		Thread thread = new Thread(runnable);
		//		thread.start();
		tv1 = findViewById(R.id.tvMus);
		seekBar = findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {//用于监听SeekBar进度值的改变
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {//用于监听SeekBar开始拖动
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {//用于监听SeekBar停止拖动  SeekBar停止拖动后的事件

				Intent intent_bar = new Intent(musicPlayer.this, musicService.class);
				playstatu = 1;
				int op = 7;
				barProgress = seekBar.getProgress();
				barMax = seekBar.getMax();
				intent_bar.putExtra("barProgress", barProgress);
				intent_bar.putExtra("barMax", barMax);
				intent_bar.putExtra("op", op);
				//传递列表
				intent_bar.putExtra("index", cposition);
				intent_bar.putExtra("list", musics);

				startService(intent_bar);
				setTitle(musics[cposition]);
			}
		});

		//绑定控件
		findview();
		bindbutton();
		//获取音乐文件列表
		getfileFromAssets();
		//通过arrayAdapter绑定至listView中
		getmusiclist();
		//设置listView的点击事件，根据点击位置更换曲目
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(musicPlayer.this, musicService.class);
				ibtn1.setImageResource(android.R.drawable.ic_media_pause);
				playstatu = 1;
				Log.d(TAG, "onClick: playing music");
				int op = 3;
				intent.putExtra("op", op);
				cposition = position;
				setTitle(musics[cposition]);
				intent.putExtra("index", cposition);
				intent.putExtra("list", musics);
				startService(intent);
			}
		});


		//注册广播接收器
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("location.reportsucc");
		musicPlayer.this.registerReceiver(receiver, filter);


	}

	private void getmusiclist() {
		// TODO Auto-generated method stub
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, musics));
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.music_item, musics));

	}

	public String[] getfileFromAssets() {
		AssetManager assetManager = getAssets();
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].endsWith(".mp3")) {
				list.add(files[i]);
			}
		}
		musics = list.toArray(new String[0]);
		return musics;
	}

	private void bindbutton() {
		// TODO Auto-generated method stub
		//因为musicplayer除了继承了Activity之外还实现了onClickListener，所以可以直接传入this
		ibtn1.setOnClickListener(this);
		ibtn2.setOnClickListener(this);
		ibtn3.setOnClickListener(this);
		ibtn4.setOnClickListener(this);
		ibtn5.setOnClickListener(this);
	}

	private void findview() {
		// TODO Auto-generated method stub
		ibtn1 = this.findViewById(R.id.imageButton1);
		ibtn2 = this.findViewById(R.id.imageButton2);
		ibtn3 = this.findViewById(R.id.imageButton3);
		ibtn4 = this.findViewById(R.id.imageButton4);
		ibtn5 = this.findViewById(R.id.imageButton5);
		lv = this.findViewById(R.id.listView1);

	}

	//在类内部实现onClickListener的方法
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//op表示操作指令，初始化
		int op = -1;
		//创建连接Service的intent
		Intent intent = new Intent(this, musicService.class);
		switch (v.getId()) {
			//根据点击的按钮不同传入不同的操作指令到intent中
			//播放和暂停操作
			case R.id.imageButton1:
				if (playstatu == 0) {
					Toast.makeText(this, "继续播放", Toast.LENGTH_SHORT).show();
					ibtn1.setImageResource(android.R.drawable.ic_media_pause);
					playstatu = 1;
					Log.d(TAG, "onClick: playing music");
					op = 1;
				} else {
					Toast.makeText(this, "暂停播放", Toast.LENGTH_SHORT).show();
					ibtn1.setImageResource(android.R.drawable.ic_media_play);
					playstatu = 0;
					Log.d(TAG, "onClick: pausing music");
					op = 2;
				}
				break;
			//退出（不停止服务）操作
			case R.id.imageButton2:
				Toast.makeText(this, "返回主界面", Toast.LENGTH_SHORT).show();
				//seekBar.setProgress(0);
				Log.d(TAG, "onClick: close");
				this.finish();
				break;
			//退出同时停止服务
			case R.id.imageButton3:

				Log.d(TAG, "onClick: exit");
				Toast.makeText(this, "退出播放", Toast.LENGTH_SHORT).show();
				op = 4;
				playstatu = 0;
				seekBar.setProgress(0);
				ibtn1.setImageResource(android.R.drawable.ic_media_play);
				stopService(intent);
				this.finish();
				break;
			//上一首
			case R.id.imageButton4:
				ibtn1.setImageResource(android.R.drawable.ic_media_pause);
				playstatu = 1;
				//if (playstatu == 1) { 无需判断当前状态
				Log.d(TAG, "onClick: previous");    //发送日志
				Toast.makeText(this, "上一首", Toast.LENGTH_SHORT).show();
				//更新索引
				if (cposition == 0)
					cposition = musics.length - 1;//如果现在是第一首则跳转到最后一首
				else
					cposition--;
				setTitle(musics[cposition]);
				op = 5;
				break;
			//下一首
			case R.id.imageButton5:

				ibtn1.setImageResource(android.R.drawable.ic_media_pause);
				playstatu = 1;
				Log.d(TAG, "onClick: next");
				Toast.makeText(this, "下一首", Toast.LENGTH_SHORT).show();
				//更新索引
				if (cposition == (musics.length - 1))
					cposition = 0;//如果现在是最后一首则跳转到第一首
				else
					cposition++;
				op = 6;
				//}
				break;
			//此处应该不能设置seekbar
			//			case R.id.seekBar: {
			//				barProgress = seekBar.getProgress() / seekBar.getMax();
			//				intent.putExtra("barProgress", barProgress);
			//				autoChange = true;
			//				manulChange = false;
			//				op = 7;
			//			}
		}
		//把操作命令写入intent
		intent.putExtra("op", op);
		setTitle(musics[cposition]);
		//把音乐列表和当前播放位置写入intent
		intent.putExtra("index", cposition);
		intent.putExtra("list", musics);
		//启动服务（第一次），执行操作（第n次）
		startService(intent);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}


	public class MyReceiver extends BroadcastReceiver {
		int total, cur;

		@Override
		public void onReceive(Context context, Intent intent) {
			String intentAction = intent.getAction();
			if (intentAction.equals("location.reportsucc")) {

				Bundle bundle = intent.getExtras();
				total = bundle.getInt("total");
				cur = bundle.getInt("cur");

				int curSecond = cur / 1000 % 60;
				int curMin = cur / 1000 / 60;
				int totalSecond = total / 1000 % 60;
				int totalMin = total / 1000 / 60;
				int max = seekBar.getMax();

				tv1.setText(String.format("%02d", curMin) + ":" + String.format("%02d", curSecond) +
						"/" + String.format("%02d", totalMin) + ":" + String.format("%02d", totalSecond));
				seekBar.setProgress(cur * max / total);
				//判断是否正在播放
				ibtn1.setImageResource(android.R.drawable.ic_media_pause);
				playstatu = 1;
				//				Runnable runnable = new Runnable() {
				//					public void run() {
				//						while (!Thread.currentThread().isInterrupted()) {
				//							Message m = handler.obtainMessage();//获取一个Message
				//							m.arg1 = cur;
				//							m.arg2 = total;
				//							m.what = UPDATE;
				//							handler.sendMessage(m);
				//						}
				//						try {
				//							Thread.sleep(1000);// 每间隔1秒发送一次更新消息
				//						} catch (InterruptedException e) {
				//							e.printStackTrace();
				//						}
				//					}
				//				};
				//				Thread thread = new Thread(runnable);
				//				thread.start();
			}

		}

		public int getCur() {
			return cur;
		}

		public int getTotal() {
			return total;
		}
	}
}

