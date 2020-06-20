package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager manager;
    LocationListener listener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {

         startlisten();

        }
    }

    public void startlisten () {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }

    }

    public void UpdateLocation(Location location) {

        Log.i("Location info", location.toString());

        TextView LatText = (TextView) findViewById(R.id.LatText);

        TextView LonText = (TextView) findViewById(R.id.LonText);

        TextView Accuracy = (TextView) findViewById(R.id.Accuracy);

        TextView AltText = (TextView) findViewById(R.id.AltText);

        LatText.setText("Latitude: " + location.getLatitude());

        LonText.setText("Longitude: " + location.getLongitude());

        Accuracy.setText("Accuracy: " + location.getAccuracy() + " mtrs ");

        AltText.setText("Altitude: " + location.getAltitude() + " mtrs ");

        Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            String address = "Address not found";

            List<Address> listAddress = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if( listAddress != null && listAddress.size()>0) {

                address = "Address: \n \n";

                if(listAddress.get(0).getAddressLine(0) != null )  {

                    address += listAddress.get(0).getAddressLine(0) + " ";

                }

            }

            TextView Address = (TextView) findViewById(R.id.Address);

            Address.setText(address);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                UpdateLocation(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startlisten();

        } else {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);

                Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if( location != null) {

                    UpdateLocation(location);

                }

            }

        }
    }
}