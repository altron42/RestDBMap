package br.edu.ufam.icomp.lopespimentel.restbdmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import br.edu.ufam.icomp.lopespimentel.restbdmap.adapter.CountryAdapter;
import br.edu.ufam.icomp.lopespimentel.restbdmap.api.ApiClient;
import br.edu.ufam.icomp.lopespimentel.restbdmap.database.Database;
import br.edu.ufam.icomp.lopespimentel.restbdmap.model.Country;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<Country> adapter;
    private ListView countriesLV;
    private List<Country> wordList;
    private Database database;

    private FloatingActionButton fab;
    private FloatingActionButton fab_rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = new Database(this);
        countriesLV = (ListView) findViewById(R.id.country_listview);
        wordList = database.getCountries();
        adapter = new CountryAdapter(this, wordList);
        countriesLV.setAdapter(adapter);

        countriesLV.setOnItemClickListener(new OnCountryClickListener());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.stat_notify_sync_noanim);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setImageResource(android.R.drawable.ic_popup_sync);
                Snackbar.make(view, "Carregando", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
                getData(view);
            }
        });

        fab_rand = (FloatingActionButton) findViewById(R.id.fab_rand);
        fab_rand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectRandomCountry();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showCountryInMap(Country country) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("NAME", country.getName());
        intent.putExtra("CAPITAL", country.getCapital());
        intent.putExtra("LAT", country.getLatlng().get(0));
        intent.putExtra("LNG", country.getLatlng().get(1));
        startActivity(intent);
    }

    private class OnCountryClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Country country = adapter.getItem(position);
            Toast.makeText(getApplicationContext(), country.getName() + " selecionado manualmente", Toast.LENGTH_LONG).show();
            showCountryInMap(country);
        }
    }

    private void SelectRandomCountry() {
        if(adapter.getCount() > 0) {
            Country country = adapter.getItem(new Random().nextInt(adapter.getCount()));
            Toast.makeText(getApplicationContext(), country.getName() + " selecionado aleatoriamente", Toast.LENGTH_LONG).show();
            showCountryInMap(country);
        }
        else {
            Snackbar.make(countriesLV, "Não há dados para serem exibidos", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    // Pega dados do serviço rest e salva direto no banco de dados
    public void getData(final View view) {
        ApiClient.getCountriesClient().getCountries().enqueue(new Callback<List<Country>>() {
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response ){
                if (response.isSuccessful() ) {
                    List<Country> data = response.body();
                    wordList.clear();
                    for ( Country country : data ) {
                        database.insertCountry(country);
                    }
                    wordList = database.getCountries();
                    adapter = new CountryAdapter(MainActivity.this, wordList);
                    countriesLV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    System.out.println(response.errorBody());
                }
                fab.setImageResource(android.R.drawable.stat_notify_sync_noanim);
                Snackbar.make(view, "Concluido ", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }
}
