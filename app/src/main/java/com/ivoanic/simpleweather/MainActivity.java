package com.ivoanic.simpleweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ivoanic.simpleweather.data.model.Weather;
import com.ivoanic.simpleweather.data.remote.WeatherAPI;
import com.thbs.skycons.library.CloudFogView;
import com.thbs.skycons.library.CloudHvRainView;
import com.thbs.skycons.library.CloudMoonView;
import com.thbs.skycons.library.CloudRainView;
import com.thbs.skycons.library.CloudSnowView;
import com.thbs.skycons.library.CloudSunView;
import com.thbs.skycons.library.CloudThunderView;
import com.thbs.skycons.library.CloudView;
import com.thbs.skycons.library.MoonView;
import com.thbs.skycons.library.SunView;
import com.thbs.skycons.library.WindView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    //https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22osijek%22)%20and%20u%3D'c'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys
    private String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22osijek%22)%20and%20u%3D'c'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    private String env="store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    private String format="json";
    private String defCity= "Osijek";

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.button)
    Button refreshBtn;
    @BindView(R.id.temp)
    TextView tempView;
    @BindView(R.id.location)
    TextView cityView;
    @BindView(R.id.conditions)
    TextView condView;
    @BindView(R.id.lastupdated)
    TextView lastupdateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @OnClick(R.id.button)
    public void refresh() {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
//        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please wait...");
//        progressDoalog.setTitle("ProgressDialog bar example");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
       url = getGrad();
        WeatherAPI.Factory.getInstance().getWeather(url).enqueue(new Callback<Weather>() {
                                                                  @Override
                                                                  public void onResponse(Call<Weather> call, Response<Weather> response) {
                                                                      if(response.body().getQuery().getResults() != null){
                                                                          progressDoalog.dismiss();
//                                                                      Query query = response.body().getQuery();
                                                                      String temperatura = response.body().getQuery().getResults().getChannel().getItem().getCondition().getTemp() + "ºC";
                                                                      tempView.setText(temperatura);
                                                                      cityView.setText(response.body().getQuery().getResults().getChannel().getTitle());
                                                                      lastupdateView.setText(response.body().getQuery().getResults().getChannel().getLastBuildDate());
                                                                      condView.setText(response.body().getQuery().getResults().getChannel().getItem().getCondition().getText());
                                                                      String weatherID = response.body().getQuery().getResults().getChannel().getItem().getCondition().getCode();
                                                                      weatherImgUpdate(weatherID);
                                                                  }
                                                                        else {
                                                                          progressDoalog.dismiss();
                                                                          Toast.makeText(MainActivity.this, "Please update location to a valid one!", Toast.LENGTH_SHORT).show();
                                                                      }

                                                                  }

                                                                  @Override
                                                                  public void onFailure(Call<Weather> call, Throwable t) {
                                                                      Log.e("Failed", t.getMessage());
                                                                  }
                                                              }

        );
    }
        //Ova fja setta view za weather
    private void weatherImgUpdate(String weatherID) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        switch(weatherID) {
            case "1" :
            case "2" :
            case "3" :
            case "32" :
            case "34" :
            case "36" :
                linear.removeAllViews();
                SunView mSun = new SunView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mSun.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mSun);
                break;
            case "22"  :
            case "23"  :
            case "24"  :
                linear.removeAllViews();
                WindView mWind = new WindView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mWind.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mWind);
                break;
            case "25" :
            case "26" :
            case "44" :
                linear.removeAllViews();
                CloudView mCloudy = new CloudView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mCloudy.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mCloudy);
            case "19" :
            case "20" :
            case "21" :
                linear.removeAllViews();
                CloudFogView mFog = new CloudFogView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mFog.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mFog);
            case "27" :
            case "29" :
                linear.removeAllViews();
                CloudMoonView mCloudyNight = new CloudMoonView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mCloudyNight.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mCloudyNight);
            case "28" :
            case "30" :
                linear.removeAllViews();
                CloudSunView mCloudySun = new CloudSunView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mCloudySun.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mCloudySun);
            case "31" :
            case "33" :
                linear.removeAllViews();
                MoonView mClearNight = new MoonView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mClearNight.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mClearNight);
                break;
            case "11" :
            case "12" :
            case "18" :
            case "35" :
                linear.removeAllViews();
                CloudRainView mRain = new CloudRainView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mRain.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mRain);
                break;
            case "37" :
            case "38" :
            case "39" :
            case "45" :
            case "47" :
                linear.removeAllViews();
                CloudThunderView mThunder = new CloudThunderView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mThunder.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mThunder);
                break;
            case "17" :
            case "40" :
                linear.removeAllViews();
                CloudHvRainView mRainHvy = new CloudHvRainView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mRainHvy.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mRainHvy);
                break;
            case "41" :
            case "42" :
            case "43" :
                linear.removeAllViews();
                CloudSnowView mSnow = new CloudSnowView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mSnow.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mSnow);
                break;

            default:
                linear.removeAllViews();
                SunView mDefault = new SunView(MainActivity.this,false,false, Color.parseColor("#000000"),Color.argb(0,0,0,0));
                mDefault.setLayoutParams(params);
                params.height=500;
                params.weight=500;
                linear.addView(mDefault);

    }
}

    public String getGrad(){
        SharedPreferences sharedPrefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String mCity= sharedPrefs.getString("neznam", "");
        mCity = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+mCity+"%22)%20and%20u%3D'c'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        return mCity;
    }
}

