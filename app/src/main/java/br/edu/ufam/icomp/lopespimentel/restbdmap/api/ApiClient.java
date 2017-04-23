package br.edu.ufam.icomp.lopespimentel.restbdmap.api;

/**
 * Created by micael on 4/16/17.
 */
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import br.edu.ufam.icomp.lopespimentel.restbdmap.model.Country;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ApiClient {
    private static CountriesInterface countriesService;

    public static CountriesInterface getCountriesClient() {
        if (countriesService == null) {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                    .create();
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl("http://restcountries.eu")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            countriesService = restAdapter.create(CountriesInterface.class);
        }

        return countriesService;
    }

    public interface CountriesInterface {
        @GET("/rest/v1/all")
        Call<List<Country>> getCountries();
    }
}