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
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);

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
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(lastKnownLocation!=null){
                updateLocation(lastKnownLocation);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            startListening();

        }
    }

    public  void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);
        }

    }

    public void updateLocation(Location location){
        TextView latitude=findViewById(R.id.latitude);
        TextView longitude=findViewById(R.id.longitude);
        TextView accuracy=findViewById(R.id.accuracy);
        TextView address=findViewById(R.id.address);
        TextView altitude=findViewById(R.id.altitude);

        latitude.setText("Latitude: "+ Double.toString(location.getLatitude()));
        longitude.setText("Longitude: "+ Double.toString(location.getLongitude()));
        accuracy.setText("Accuracy: "+ Double.toString(location.getAccuracy()));
        altitude.setText("Altitude: "+ Double.toString(location.getAltitude()));

        String add="Could not fond address: (";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            if(addressList!=null && addressList.size()>0){
                add="Address:\n";

                if(addressList.get(0).getCountryName()!=null){
                    add+=addressList.get(0).getCountryName()+ "\n";
                }
                if(addressList.get(0).getLocality()!=null){
                    add+=addressList.get(0).getLocality()+ " ";
                }
                if(addressList.get(0).getSubLocality()!=null){
                    add+=addressList.get(0).getSubLocality()+ " ";
                }
                if(addressList.get(0).getAdminArea()!=null){
                    add+=addressList.get(0).getAdminArea()+ " ";
                }
                if(addressList.get(0).getPostalCode()!=null){
                    add+=addressList.get(0).getPostalCode();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        address.setText(add);
    }
}
