package com.example.appmap;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class PermissionsMarshmallow {
    private final Context mContext;

    public PermissionsMarshmallow(Context context) {
        mContext = context;
    }

    public boolean hasPermissions(String... permissions) {
        if (mContext != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void requestPermissions(String[] PERMISSIONS, int PERMISSION_ALL) {
        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, PERMISSION_ALL);
    }

}
//
//public class MainActivity extends AppCompatActivity {
//    ...
//    private PermissionsMarshmallow permissionsMashmallow = new PermissionsMarshmallow(this);
//    private String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        ...
//        CheckPermissionGranted();
//    }
//
//    private void CheckPermissionGranted() {
//        if (permissionsMashmallow.hasPermissions(PERMISSIONS)) {
//            //  permission granted
//        } else {
//            // request permission
//            permissionsMashmallow.requestPermissions(PERMISSIONS, 2);
//        }
//    }
//}
