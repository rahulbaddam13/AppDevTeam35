package edu.northeastern.numad22fa_mrp.model;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IPlaceHolder {

    //Get location info
    @GET("points/{point}")
    Call<WeatherHeader> getLocation(@Path("point") String point);

    // Get forecast for location
    @GET("gridpoints/{wfo}/{x},{y}/forecast")
    Call<WeatherInfo> getWeatherInfo(@Path("wfo") String wfo,
                              @Path("x") int x,
                              @Path("y") int y);
}
