package br.edu.ufam.icomp.lopespimentel.restbdmap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

import br.edu.ufam.icomp.lopespimentel.restbdmap.R;
import br.edu.ufam.icomp.lopespimentel.restbdmap.model.Country;

/**
 * Created by micael on 4/16/17.
 */

public class CountryAdapter extends ArrayAdapter<Country> {
    private List<Country> countries;
    private Context context;

    public CountryAdapter(Context context, List<Country> countries) {
        super(context, 0, countries);
        this.countries = countries;
        this.context = context;
    }

    @Override
    public void addAll(Collection<? extends Country> moreCountries) {
        this.countries.addAll(moreCountries);
        super.addAll(moreCountries);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.country_adapter, null);

        TextView tvName = (TextView) view.findViewById(R.id.country_name);
        TextView tvCountry_1 = (TextView) view.findViewById(R.id.country_1);
        TextView tvCountry_2 = (TextView) view.findViewById(R.id.country_2);

        Country country = countries.get(position);
        tvName.setText(country.getName());
        tvCountry_1.setText("Capital: " + country.getCapital());
        try {
            tvCountry_2.setText("lat: " + country.getLatlng().get(0) + " lng: " + country.getLatlng().get(1));
        } catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
        }

        return view;
    }
}
