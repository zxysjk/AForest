package com.example.aforest;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class compass extends AppCompatActivity implements SensorEventListener {
	private SensorManager mSensorManager;//传感器管理器
	private Sensor mAccelerometer, mOrientation;//加速度传感器和方向传感器对象
	final String TAG = "duanhong";
	private TextView sensorName,xAxis,yAxis,zAxis,sensorNameO,xAxisO,yAxisO,zAxisO,sensorList;
	private ImageView compass;
	private float currentDegree = 0;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);
		setTitle("指南针");
		compass = (ImageView)findViewById(R.id.compass);
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);//获取一个SensorManager类的实例

		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);//获得需要的传感器

	}

	protected void onResume() {//当activity进入resume状态是注册传感器监听器
		super.onResume();
		mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_UI);
	}

	protected void onPause() {//当activity进入pause状态是注销传感器监听器
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {//当传感器数值发生改变时进入此方法，更新界面显示的结果
		if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
//				sensorNameO.setText("传感器：" + event.sensor.getName());
//				xAxisO.setText("Orientation X: " + event.values[0]);
//				yAxisO.setText("Orientation Y: " + event.values[1]);
//				zAxisO.setText("Orientation Z: " + event.values[2]);
				float degree = event.values[0]; //value[0]表示z轴的角度，即手机的水平方向，0为正北，90为正东，以此类推
				//以指南针图像中心旋转
				RotateAnimation ra;//旋转动画工具对象
				if(Math.abs((Math.abs(degree) - Math.abs(currentDegree))) > 180){//防止出现旋转较大弧的情况
					if(Math.abs(currentDegree) < 180){
						ra = new RotateAnimation(360 - currentDegree, degree,
								Animation.RELATIVE_TO_SELF, 0.5f,
								Animation.RELATIVE_TO_SELF, 0.5f);
						ra.setDuration(100);	      //在200毫秒之内完成旋转动作
						compass.startAnimation(ra);   //开始旋转图像
					}
					else{
						ra = new RotateAnimation(currentDegree, -degree - 360,
								Animation.RELATIVE_TO_SELF, 0.5f,
								Animation.RELATIVE_TO_SELF, 0.5f);
						ra.setDuration(100);	            //在200毫秒之内完成旋转动作
						compass.startAnimation(ra); 	    //开始旋转图像
					}
				}
				else{
					ra = new RotateAnimation(currentDegree, -degree,
							Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					ra.setDuration(100);	            //在200毫秒之内完成旋转动作
					compass.startAnimation(ra); 	    //开始旋转图像
				}

				//保存旋转后的度数，currentDegree是一个在类中定义的float类型变量
				currentDegree = -degree;
		}
	}
}