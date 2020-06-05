package com.example.aforest;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class sensors extends AppCompatActivity implements SensorEventListener {
	private SensorManager mSensorManager;//传感器管理器
	private Sensor mAccelerometer, mOrientation, mLight, mMagnetic, mGravity, mGyroScope;//加速度传感器和方向传感器对象
	final String TAG = "duanhong";
	private TextView sensorName, xAxis, yAxis, zAxis, sensorNameO, xAxisO, yAxisO, zAxisO, sensorLight, sensorList, sensorLight0;
	private TextView sensorMag0, xMag, yMag, zMag, sensorGrav0, xgrav, ygrav, zgrav, sensorgyro, xgyro, ygyro, zgyro;
	private ImageView compass;
	private float currentDegree = 0;
	private String nameAcce, nameOrie, nameMag, nameGyro, nameGrav, nameLight;
	Button bt1, bt2;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensors);
		sensors.this.setTitle("设备传感器");

		//绑定两个按钮
		bt1 = findViewById(R.id.buttonPe);
		bt1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(sensors.this, pedometer.class));
			}
		});
		bt2 = findViewById(R.id.buttonCom);
		bt2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(sensors.this, compass.class));
			}
		});

		//高版本的android系统不支持在主线程中访问网络，因此需要在此更改其线程的安全策略，以免报错
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		sensorName = (TextView) findViewById(R.id.sensorname);//获取各个ui控件
		xAxis = (TextView) findViewById(R.id.xAxis);
		yAxis = (TextView) findViewById(R.id.yAxis);
		zAxis = (TextView) findViewById(R.id.zAxis);
		sensorNameO = (TextView) findViewById(R.id.sensornameO);
		xAxisO = (TextView) findViewById(R.id.xAxisO);
		yAxisO = (TextView) findViewById(R.id.yAxisO);
		zAxisO = (TextView) findViewById(R.id.zAxisO);
		sensorList = (TextView) findViewById(R.id.sensorlist);
		compass = (ImageView) findViewById(R.id.compass);
		sensorLight = findViewById(R.id.sensor_light);
		sensorLight0 = findViewById(R.id.sensor_light0);
		sensorGrav0 = findViewById(R.id.sensorGravity);
		xgrav = findViewById(R.id.xGrav);
		ygrav = findViewById(R.id.yGrav);
		zgrav = findViewById(R.id.zGrav);
		sensorgyro = findViewById(R.id.sensorGyroScope);
		xgyro = findViewById(R.id.xGyro);
		ygyro = findViewById(R.id.yGyro);
		zgyro = findViewById(R.id.zGyro);
		sensorMag0 = findViewById(R.id.sensorMag);
		xMag = findViewById(R.id.xMag);
		yMag = findViewById(R.id.yMag);
		zMag = findViewById(R.id.zMag);


		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);//获取一个SensorManager类的实例

		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//获得需要的传感器
		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);//获得需要的传感器
		mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);//获得需要的光传感器
		mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		mGyroScope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		nameAcce = mAccelerometer.getName();
		nameGrav = mGravity.getName();
		nameGyro = mGyroScope.getName();
		nameLight = mLight.getName();
		nameMag = mMagnetic.getName();
		nameOrie = mOrientation.getName();

		sensorName.setText("加速度传感器:" + nameAcce);
		sensorMag0.setText("磁场传感器:" + nameMag);
		sensorGrav0.setText("重力传感器:" + nameGrav);
		sensorgyro.setText("螺旋仪传感器:" + nameGyro);
		sensorNameO.setText("方向传感器:" + nameOrie);
		sensorLight0.setText("光传感器:" + nameLight);

		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);//获取传感器清单
		for (Sensor sensor : sensors) {
			//输出传感器的名称
			sensorList.append(sensor.getName() + "\n");
		}
	}

	protected void onResume() {//当activity进入resume状态是注册传感器监听器
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mGyroScope, SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_UI);
	}

	protected void onPause() {//当activity进入pause状态是注销传感器监听器
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {//当传感器数值发生改变时进入此方法，更新界面显示的结果
		switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER: {//加速度传感器的值显示

				xAxis.setText("Accelerometer X: " + event.values[0]);
				yAxis.setText("Accelerometer Y: " + event.values[1]);
				zAxis.setText("Accelerometer Z: " + event.values[2]);
				break;
			}
			case Sensor.TYPE_MAGNETIC_FIELD: {

				xMag.setText("X_lateral: " + event.values[0]);
				yMag.setText("Y_longitudinal: " + event.values[1]);
				zMag.setText("Z_vertical: " + event.values[2]);

			}
			case Sensor.TYPE_GRAVITY: {

				xgrav.setText("X_gravity: " + event.values[0]);
				ygrav.setText("Y_gravity: " + event.values[1]);
				zgrav.setText("Z_gravity: " + event.values[2]);

			}
			case Sensor.TYPE_GYROSCOPE: {

				xgyro.setText("X_axis angular velocity: " + event.values[0]);
				ygyro.setText("Y_axis angular velocity: " + event.values[1]);
				zgyro.setText("Z_axis angular velocity: " + event.values[2]);

			}
			case Sensor.TYPE_LIGHT: {

				sensorLight.setText("illumination intensity:" + event.values[0]);

			}
			case Sensor.TYPE_ORIENTATION: {//方向传感器的值显示，同时更新图片UI控件实现指南针效果

				xAxisO.setText("Orientation X: " + event.values[0]);
				yAxisO.setText("Orientation Y: " + event.values[1]);
				zAxisO.setText("Orientation Z: " + event.values[2]);
//				float degree = event.values[0]; //value[0]表示z轴的角度，即手机的水平方向，0为正北，90为正东，以此类推
//				//以指南针图像中心旋转
//				RotateAnimation ra;//旋转动画工具对象
//				if (Math.abs((Math.abs(degree) - Math.abs(currentDegree))) > 180) {//防止出现旋转较大弧的情况
//					if (Math.abs(currentDegree) < 180) {
//						ra = new RotateAnimation(360 - currentDegree, degree,
//								Animation.RELATIVE_TO_SELF, 0.5f,
//								Animation.RELATIVE_TO_SELF, 0.5f);
//						ra.setDuration(200);          //在200毫秒之内完成旋转动作
//						compass.startAnimation(ra);   //开始旋转图像
//					} else {
//						ra = new RotateAnimation(currentDegree, -degree - 360,
//								Animation.RELATIVE_TO_SELF, 0.5f,
//								Animation.RELATIVE_TO_SELF, 0.5f);
//						ra.setDuration(200);                //在200毫秒之内完成旋转动作
//						compass.startAnimation(ra);        //开始旋转图像
//					}
//				} else {
//					ra = new RotateAnimation(currentDegree, -degree,
//							Animation.RELATIVE_TO_SELF, 0.5f,
//							Animation.RELATIVE_TO_SELF, 0.5f);
//					ra.setDuration(200);                //在200毫秒之内完成旋转动作
//					compass.startAnimation(ra);        //开始旋转图像
//				}
//
//				//保存旋转后的度数，currentDegree是一个在类中定义的float类型变量
//				currentDegree = -degree;
				break;
			}
			default:
				break;
		}
	}
}