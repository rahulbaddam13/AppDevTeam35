package edu.northeastern.numad22fa_mrp.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetWeatherService {

    @GET("/v1/era5")
    Call<WeatherData> getWeatherInfo(@Query("latitude") String latitude,
                                        @Query("longitude") String longitude,
                                        @Query("start_date") String startDate,
                                        @Query("end_date") String endDate,
                                        @Query("daily") String daily,
                                        @Query("timezone") String timezone);
}
