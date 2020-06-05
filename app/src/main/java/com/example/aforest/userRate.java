package com.example.aforest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

public class userRate extends AppCompatActivity {

	RatingBar.OnRatingBarChangeListener orcl1;
	private int count = 0;  //评分个数
	private float avg = 0;  //平均分
	private float sum = 0;  //总评分
	private RatingBar rb1, rb2;
	private TextView tv1, tv2;
	Button btn;
	View.OnClickListener ocl1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("应用评价");
		setContentView(R.layout.activity_user_rate);
		findViewId();   //绑定控件
		DecimalFormat decimalFormat = new DecimalFormat(".00"); //设置format格式
		
		
		//取出字符串
		String rateScore = readfile("rateScore");
		if(rateScore.equals("")) {}
		else{
			//用'/'分割开两个数值
			String[] rates = rateScore.split("/");
			String sums = rates[0];
			String counts = rates[1];
			count = Integer.parseInt(counts);
			sum = Float.parseFloat(sums);
		}

		if (count == 0) {
			rb2.setRating(0);
			tv2.setText("0个评分");
		} else {
			avg = sum / count;
			rb2.setRating(avg);
			tv2.setText(count + "个评分");
			tv1.setText(decimalFormat.format(avg));
		}

		//点击操作
		orcl1 = new RatingBar.OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				count += 1;
				sum += rating;
				avg = sum / count;
				rb2.setRating(avg);
				DecimalFormat decimalFormat = new DecimalFormat(".00");//设置format格式
				tv2.setText(count + "个评分");
				tv1.setText(decimalFormat.format(avg));

				//转换成字符存储至文件，中间用“/”分隔开
				String rateScore = String.valueOf(sum) + "/" + String.valueOf(count);
				savefile("rateScore", rateScore);
				Log.v("saveRate", "Save rateScore.");
				Toast.makeText(getApplicationContext(), "评价成功", Toast.LENGTH_SHORT).show();
			}
		};
		rb1.setOnRatingBarChangeListener(orcl1);

		//返回键
		ocl1 = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userRate.this.finish();
			}
		};
		btn.setOnClickListener(ocl1);
	}

	public void findViewId() {
		rb1 = this.findViewById(R.id.ratingBar1);
		rb2 = this.findViewById(R.id.ratingBar2);
		tv1 = this.findViewById(R.id.textRating);
		tv2 = this.findViewById(R.id.textRatingNumber);
		btn = this.findViewById(R.id.back3);
	}

	protected void savefile(String filename, String filecontent) {
		// TODO Auto-generated method stub
		try {
			//创建一个文件输出流用于保存，这里我们使用私有模式,创建出来的文件只能被本应用访问,还会覆盖原文件
			//如果不指定文件路径，则会保存在默认应用路径中去(data\data\应用程序包名\files,可以用通过DDMS查看)
			FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(filecontent.getBytes());//将String字符串以字节流的形式写入到输出流中
			fos.flush();
			fos.close();//关闭输出流
			//Toast.makeText(getApplicationContext(), "数据写入成功", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			//Toast.makeText(getApplicationContext(), "数据写入失败", Toast.LENGTH_SHORT).show();
		}
	}

	protected String readfile(String filename) {//读取文件，返回string类型的读取结果
		// TODO Auto-generated method stub
		String result;
		try {
			FileInputStream fis = this.openFileInput(filename);//根据文件名从应用程序默认路径打开一个文件输入流
			int lenght = fis.available();//获取文件流内容能够读取的最大字节数
			byte[] buffer = new byte[lenght];//创建一个比特数组变量接受读取内容
			fis.read(buffer);//读取比特流
			result = new String(buffer, "UTF-8");//将读取到的比特流数据转化为string
			fis.close();
		} catch (Exception e) {
			result="";//如果取不出文件则为空，以初始化评分
		}
		return result;
	}
}

