package com.example.aforest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.aforest.R;

public class bdmap extends AppCompatActivity {
	MapView mMapView;
	BaiduMap mBaiduMap;
	RadioGroup rg;
	RadioButton rb1,rb2;
	LocationClient mLocationClient;
	boolean isFirstLoc = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("地图");
		SDKInitializer.initialize(getApplicationContext());//初始化地图
		setContentView(R.layout.activity_bdmap);
		mMapView = (MapView) findViewById(R.id.bmapView);//获取地图UI控件
		mBaiduMap = mMapView.getMap();//获取百度地图实例
		rg = this.findViewById(R.id.radioGroup1);
		rb1 = this.findViewById(R.id.radio0);
		rb2 = this.findViewById(R.id.radio1);
		rb1.setChecked(true);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId)
				{
					case R.id.radio0:
						mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//设置地图类型为普通
						mBaiduMap.setTrafficEnabled(true);//打开实时路况图层
						break;
					case R.id.radio1:
						mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);//设置地图类型为卫星影像
						break;
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
		mMapView.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
		mMapView.onPause();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
		mLocationClient.stop();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.location)
		{
			doLocation();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void doLocation() {
		// TODO Auto-generated method stub
		mBaiduMap.setMyLocationEnabled(true);
		//定位初始化
		mLocationClient = new LocationClient(this);
		isFirstLoc = true;
		//通过LocationClientOption设置LocationClient相关参数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 2000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
		// .getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);
		option.setOpenGps(true); // 打开gps

		// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

		//设置locationClientOption
		mLocationClient.setLocOption(option);

		//注册LocationListener监听器
		MyLocationListener myLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myLocationListener);
		//开启地图定位图层
		mLocationClient.start();
	}

	public class MyLocationListener extends BDAbstractLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			//mapView 销毁后不在处理新接收的位置
			if (location == null || mMapView == null){
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(location.getDirection()).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

				if (location.getLocType() == BDLocation.TypeGpsLocation) {
					// GPS定位结果
					Toast.makeText(bdmap.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
					// 网络定位结果
					Toast.makeText(bdmap.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

				} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
					// 离线定位结果
					Toast.makeText(bdmap.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

				} else if (location.getLocType() == BDLocation.TypeServerError) {
					Toast.makeText(bdmap.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
					Toast.makeText(bdmap.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
				} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
					Toast.makeText(bdmap.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
				}
				mLocationClient.stop();
			}
		}
	}
}