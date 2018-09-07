package com.example.logarithm.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    JSONDownloader task;
    TextView  tempShow;
    TextView  cloud;
    TextView lon;
    TextView lat;
    String json;
    String cityName;


    class JSONDownloader extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            URL url;
            try {
                HttpURLConnection connection;
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream ip = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(ip);
                int data = reader.read();
                String result = "";
                while (data != -1) {
                    char ele = (char) data;
                    result = result + ele;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {

                e.printStackTrace();
                return null;
            }


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempShow=(TextView) findViewById(R.id.tempShow);
        cloud=(TextView) findViewById(R.id.cloud);
        lon=(TextView)findViewById(R.id.lon);
        lat=(TextView)findViewById(R.id.lat);


    }

    public void getUrl(View view) {
        try {
            task = new JSONDownloader();
            json = task.execute("http://openweathermap.org/data/2.5/weather?q="+ cityName+"&appid=b6907d289e10d714a6e88b30761fae22").get();

        } catch (Exception e) {

            Log.i("The Exception is  ", e.toString());

        }
    }

    public void showWeather(View view) {
        TextView city = (TextView) findViewById(R.id.city);
        cityName = city.getText().toString();
        try {

            getUrl(view);
            if(json!=null) {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject main = jsonObject.getJSONObject("main");
                JSONArray weather = jsonObject.getJSONArray("weather");
                JSONObject weather2 = weather.getJSONObject(0);
                String cloud1 = weather2.getString("main");
                String temp = main.getString("temp");
                JSONObject cord = jsonObject.getJSONObject("coord");
                String lon1 = cord.getString("lon");
                String lat1 = cord.getString("lat");
                lon.setText("Longitude :  " + lon1);
                lat.setText("Longitude :  " + lat1);
                cloud.setText("Weather Type :" + cloud1);
                tempShow.setText("Temparature: " + temp + " 'c");
                json = null;
            }


        } catch (Exception e) {
            e.printStackTrace();
            tempShow.setText("Sorry");
            lon.setText(" -Rohan ");
            lat.setText("");
            cloud.setText("No Data Available !");
        }
    }
}