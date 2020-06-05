package com.example.aforest;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.aforest.R;

import java.util.ArrayList;

public class pedometer extends AppCompatActivity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	final String TAG = "duanhong";
	private TextView pedometerStatus, stepcount, debug;
	private static final float GRAVITY = 9.80665f;
	private static final float GRAVITY_RANGE = 0.5f;
	Button btn1;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedometer);
		setTitle("计步器");
		//高版本的android系统不支持在主线程中访问网络，因此需要在此更改其线程的安全策略，以免报错
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		stepcount = (TextView)findViewById(R.id.stepcount);
		debug = (TextView)findViewById(R.id.debug);
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		BindButton();
	}

	private void BindButton() {
		// TODO Auto-generated method stub
		btn1 = (Button) this.findViewById(R.id.clear);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stepcount.setText("0");
			}
		});
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()){
			case Sensor.TYPE_ACCELEROMETER:{
				Log.v(TAG, "values[0]-->" + event.values[0] + ", values[1]-->" + event.values[1] + ", values[2]-->" + event.values[2]);
				//debug.setText("values[0]-->" + event.values[0] + "\nvalues[1]-->" + event.values[1] + "\nvalues[2]-->" + event.values[2]);
				debug.setText("x-->" + event.values[0] + "\ny-->" + event.values[1] + "\nz-->" + event.values[2]);
				if(justFinishedOneStep(event.values[2])){
					stepcount.setText((Integer.parseInt(stepcount.getText().toString()) + 1) + "");
				}
				break;
			}
			default:
				break;
		}
	}

	private ArrayList<Float> dataOfOneStep = new ArrayList<Float>();//存储一步的过程中传感器传回值的数组便于分析
	/**
	 * 判断是否完成了一步行走的动作
	 * @param newData 传感器新传回的数值（values[2]）
	 * @return 是否完成一步
	 */
	private boolean justFinishedOneStep(float newData){
		boolean finishedOneStep = false;
		dataOfOneStep.add(newData);//将新数据加入到用于存储数据的列表中
		dataOfOneStep = eliminateRedundancies(dataOfOneStep);//消除冗余数据
		finishedOneStep = analysisStepData(dataOfOneStep);//分析是否完成了一步动作
		if(finishedOneStep){//若分析结果为完成了一步动作，则清空数组，并返回真
			dataOfOneStep.clear();
			return true;
		}else{//若分析结果为尚未完成一步动作，则返回假
			if(dataOfOneStep.size() >= 100){//防止占资源过大
				dataOfOneStep.clear();
			}
			return false;
		}
	}

	/**
	 * 分析数据子程序
	 * @param stepData 待分析的数据
	 * @return 分析结果
	 */
	private boolean analysisStepData(ArrayList<Float> stepData){
		boolean answerOfAnalysis = false;
		boolean dataHasBiggerValue = false;
		boolean dataHasSmallerValue = false;
		for(int i=1; i<stepData.size()-1; i++){
			if(stepData.get(i).floatValue() > GRAVITY + GRAVITY_RANGE){//是否存在一个极大值
				if((stepData.get(i).floatValue() > stepData.get(i+1).floatValue()) && (stepData.get(i).floatValue() > stepData.get(i-1).floatValue())){
					dataHasBiggerValue = true;
				}
			}
			if(stepData.get(i).floatValue() < GRAVITY - GRAVITY_RANGE){//是否存在一个极小值
				if((stepData.get(i).floatValue() < stepData.get(i+1).floatValue()) && (stepData.get(i).floatValue() < stepData.get(i-1).floatValue())){
					dataHasSmallerValue = true;
				}
			}
		}
		answerOfAnalysis = dataHasBiggerValue && dataHasSmallerValue;
		return answerOfAnalysis;
	}

	/**
	 * 消除ArrayList中的冗余数据，节省空间，降低干扰
	 * @param rawData 原始数据
	 * @return 处理后的数据
	 */
	private ArrayList<Float> eliminateRedundancies(ArrayList<Float> rawData){
		for(int i=0; i<rawData.size()-1 ;i++){
			//判断是否有连续多个值维持在重力加速度附近范围内，如果是，则表示手机可能处于静止状态，此时把这几个连续值删除以免rawData过长
			if((rawData.get(i) < GRAVITY + GRAVITY_RANGE) && (rawData.get(i) > GRAVITY - GRAVITY_RANGE)
					&& (rawData.get(i+1) < GRAVITY + GRAVITY_RANGE) && (rawData.get(i+1) > GRAVITY - GRAVITY_RANGE)){
				rawData.remove(i);
			}else{
				break;
			}
		}
		return rawData;
	}

}