package com.example.aforest.MusicPlayer;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class musicService extends Service {
	private static final String TAG = "MyService";
	private MediaPlayer mediaPlayer;//音乐播放器实例，用于播放音乐
	boolean isfirst = true;//判断是否是第一次播放
	String[] list;//播放列表，接受intent传来的内容
	int index = 0;//当前播放位置，接受intent传来的内容

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	//第一次启动服务时执行，初始化mediaplayer
	public void onCreate() {
		//Toast.makeText(this, "开始播放", Toast.LENGTH_SHORT).show();

		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
		//定义一首歌播放完毕后的事件监听器，让播放器自动顺序播放下一首，到最后一首时循环至第一首
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				Log.d("tag", "播放完毕");
				if (list.length > 0) {
					if (index < (list.length - 1))
						index++;
					else
						index = 0;
					String path = list[index];
					try {
						AssetFileDescriptor fd = getAssets().openFd(path);//读取音乐文件
						mp.reset();//一定要reset否则再次设置资源就会出错
						mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());//设置mediaplayer的播放内容
						mp.prepare();//准备播放
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					mp.start();//开始播放
				}
			}
		});


		Runnable runnable = new Runnable() {
			public void run() {
				int cur = 0, tot = 2400;
				while (!Thread.currentThread().isInterrupted()) {
					try {
						while (mediaPlayer != null && mediaPlayer.isPlaying()) {

							cur = mediaPlayer.getCurrentPosition();//得到当前歌曲播放进度(秒)
							tot = mediaPlayer.getDuration();//最大秒数
							//广播传递消息
							Intent intent_pro = new Intent();
							//intent_pro.putExtra("progress", mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration());
							intent_pro.putExtra("total", tot);
							intent_pro.putExtra("cur", cur);
							intent_pro.setAction("location.reportsucc");
							sendBroadcast(intent_pro);

							try {
								Thread.sleep(500);// 每间隔0.5秒发送一次更新消息
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	//服务终止时执行的操作，停止播放器mediaplayer,释放资源
	public void onDestroy() {
		Log.v(TAG, "onDestroy");
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
	}

	//每次执行startService()方法是都会调用此方法，根据intent传来的op命令执行对应的操作
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v(TAG, "onStart");
		if (intent != null) {
			int op = intent.getIntExtra("op", -1);// 获取操作指令
			int index = intent.getIntExtra("index", 0);// 获取播放位置
			list = intent.getStringArrayExtra("list");// 获取播放列表
			String path = list[index];// 获取播放曲目

			switch (op) {
				case 1:// 如果是播放操作
					if (isfirst)// 如果是第一次播放则要初始化mediaplayer设置资源
					{
						try {
							AssetFileDescriptor fd = getAssets().openFd(path);
							mediaPlayer.setDataSource(fd.getFileDescriptor());
							mediaPlayer.prepare();
							isfirst = false;
						} catch (IOException ex) {

							ex.printStackTrace();
						}
					}
					play();// 如果不是第一次播放（暂停后再播放）则直接播放


					break;
				case 2:// 如果是暂停操作
					pause();
					break;
				case 3:// 如果是更换曲目操作
					change(path);
					break;
				case 4:// 如果是停止操作
					stop();
					break;
				case 5:// 如果是下一首
					//if (mediaPlayer != null && mediaPlayer.isPlaying())
						change(path);
					break;
				case 6:// 如果是上一首
					//if (mediaPlayer != null && mediaPlayer.isPlaying())
						change(path);
					break;
				case 7:
					if (isfirst)// 如果是第一次播放则要初始化mediaplayer设置资源
					{
						try {
							AssetFileDescriptor fd = getAssets().openFd(path);
							mediaPlayer.setDataSource(fd.getFileDescriptor());
							mediaPlayer.prepare();
							isfirst = false;
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
					mediaPlayer.start();

					int barMax = intent.getIntExtra("barMax", 24000);
					int barProgress = intent.getIntExtra("barProgress", 0);
					int musicMax = mediaPlayer.getDuration(); //得到该首歌曲最长秒数
					mediaPlayer.seekTo(musicMax * barProgress / barMax);//跳到该曲该秒

			}
		}
		return Service.START_STICKY;
	}


	//换歌
	private void change(String path) {
		// TODO Auto-generated method stub
		try {
			isfirst = false;
			AssetFileDescriptor fd = getAssets().openFd(path);
			mediaPlayer.reset();//一定要reset否则再次设置资源就会出错
			mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
			mediaPlayer.prepare();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// TODO Auto-generated method stub
		mediaPlayer.start();
	}

	//暂停
	private void pause() {
		// TODO Auto-generated method stub
		if (mediaPlayer != null && mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}

	//播放
	private void play() {
		// TODO Auto-generated method stub
		if (!mediaPlayer.isPlaying())
			mediaPlayer.start();
	}

	public void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.reset();
		}
	}
}
