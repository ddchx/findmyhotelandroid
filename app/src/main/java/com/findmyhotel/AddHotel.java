package com.findmyhotel;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.findmyhotel.model.Hotel;
import com.findmyhotel.model.HotelMessage;
import com.findmyhotel.model.Hotels;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AddHotel extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private String provider;

    EditText nameText;
    EditText addressText;

    TextView latitudeText;
    TextView longitudeText;

    Button addHotelBtn;

    double lat = 0;
    double lng = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameText = (EditText) findViewById(R.id.hotel_name);
        addressText = (EditText) findViewById(R.id.hotel_address);
        latitudeText = (TextView) findViewById(R.id.latitude);
        longitudeText = (TextView) findViewById(R.id.longitude);
        addHotelBtn = (Button) findViewById(R.id.button_add_hotel);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latitudeText.setText("Location not available");
           longitudeText.setText("Location not available");
        }

        addHotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameText.toString().isEmpty() || addressText.toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Invalid values", Toast.LENGTH_SHORT);
                    return;
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://findmyhotel.local")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // prepare call in Retrofit 2.0
                ServerAPI hotelsAPI = retrofit.create(ServerAPI.class);

                Hotel hotel = new Hotel();
                hotel.setAddress(addressText.getText().toString());
                hotel.setName(nameText.getText().toString());
                hotel.setLatitude(lat);
                hotel.setLongitude(lng);

             /*   hotelsAPI.addHotel(hotel, new Callback<Hotel>() {
                    @Override
                    public void onResponse(Response<Hotel> response, Retrofit retrofit) {
                        Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_LONG);

                    }
                });*/

                Call<HotelMessage> call = hotelsAPI.addHotel(hotel);
                //asynchronous call
                call.enqueue(new Callback<HotelMessage>() {
                    @Override
                    public void onResponse(Response<HotelMessage> response, Retrofit retrofit) {
                        Toast.makeText(AddHotel.this, "Successfully added", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(AddHotel.this, "Error", Toast.LENGTH_LONG);

                    }
                });
            }
        });
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        latitudeText.setText(String.valueOf(lat));
        longitudeText.setText(String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}

