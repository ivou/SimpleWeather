package com.ivoanic.simpleweather.data.remote;

import com.ivoanic.simpleweather.data.model.Weather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Ivo on 13.9.2017..
 */

public interface WeatherAPI {


    String BASE_URL = "https://query.yahooapis.com/v1/public/";

    String parameters = "yql";


    @GET Call<Weather> getWeather(@Url String url);

    class Factory {

        private static WeatherAPI service;

        public static WeatherAPI getInstance() {

            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
                service = retrofit.create(WeatherAPI.class);
                return service;
            } else {
                return service;
            }
        }

    }
}
