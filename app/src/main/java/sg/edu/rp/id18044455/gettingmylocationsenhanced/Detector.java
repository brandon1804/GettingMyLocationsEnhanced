package sg.edu.rp.id18044455.gettingmylocationsenhanced;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileWriter;

public class Detector extends Service {


    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    FusedLocationProviderClient client;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("MyService", "Service created");
        client = LocationServices.getFusedLocationProviderClient(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location data = locationResult.getLastLocation();
                    double lat = data.getLatitude();
                    double lng = data.getLongitude();

                    if (checkPermissions()) {
                        String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/L10PSFolder";
                        File folder = new File(folderLocation);
                        if (!folder.exists()) {
                            boolean result = folder.mkdir();
                            if (result) {
                                Log.d("File Read/Write", "Folder created");
                            }
                        }
                        try {
                            folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/L10PSFolder";
                            File targetFile = new File(folderLocation, "records.txt");
                            FileWriter writer = new FileWriter(targetFile, true);
                            writer.write(lat + ", " + lng + "\n");
                            Log.d("Detector", "Wrote");
                            writer.flush();
                            writer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }//end of permission
                }//end of location validation
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }


        return super.onStartCommand(intent, flags, startId);
    }//end of onStartCommand


    @Override
    public void onDestroy() {
        client.removeLocationUpdates(mLocationCallback);
        Log.d("MyService", "Service exited");
        super.onDestroy();
    }


    private boolean checkPermissions(){

        int permissionCheck_Write = ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_Read = ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck_Write == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Read == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }//end of checkPermissions

}//end of class
