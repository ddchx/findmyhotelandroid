package com.findmyhotel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.findmyhotel.model.Hotel;
import com.findmyhotel.model.Hotels;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ListHotels extends AppCompatActivity implements Callback<Hotels> {

    ListView listView;

    EditText inputSearch;

    ArrayList<Hotel> hotelList;

    ArrayAdapter<Hotel> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_hotels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listView);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        arrayAdapter =  new ArrayAdapter<Hotel>(this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new ArrayList<Hotel>());
        listView.setAdapter(arrayAdapter);
        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://findmyhotel.local")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2.0
        ServerAPI hotelsAPI = retrofit.create(ServerAPI.class);

        Call<Hotels> call = hotelsAPI.loadHotels();
        //asynchronous call
        call.enqueue(this);

        // call.execute()

        //Call<Hotels> c = call.clone();
        //c.enqueue(this);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ListHotels.this.arrayAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    @Override
    public void onResponse(Response<Hotels> response, Retrofit retrofit) {
        setProgressBarIndeterminateVisibility(false);
        ArrayAdapter<Hotel> adapter = (ArrayAdapter<Hotel>) listView.getAdapter();
        adapter.clear();
        adapter.addAll(response.body().items);
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(ListHotels.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
