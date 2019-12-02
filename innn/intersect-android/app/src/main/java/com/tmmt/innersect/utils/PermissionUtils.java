package com.tmmt.innersect.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();

    public interface PermissionGrant {
        void onPermissionGranted(int requestCode);
    }
    public static void checkPermission(Activity activity, String[] permissions, int requestCode, PermissionGrant permissionGrant) {
        if (activity == null) {
            return;
        }
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            try {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            } catch (RuntimeException e) {
                return;
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), requestCode);
        } else {
            permissionGrant.onPermissionGranted(requestCode);
        }
    }

//    private static void shouldShowRationale(final Activity activity, final int requestCode, final String requestPermission) {
//        //TODO
//        String[] permissionsHint = activity.getResources().getStringArray(R.array.permissions);
//        showMessageOKCancel(activity, "Rationale: " + permissionsHint[requestCode], new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ActivityCompat.requestPermissions(activity,
//                        new String[]{requestPermission},
//                        requestCode);
//                Log.d(TAG, "showMessageOKCancel requestPermissions:" + requestPermission);
//            }
//        });
//    }

    private static void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    /**
     * @param activity
     * @param requestCode  Need consistent with requestPermission
     * @param permissions
     * @param grantResults
     */
    public static void requestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                                @NonNull int[] grantResults, PermissionGrant permissionGrant) {
        if (activity == null) {
            return;
        }
        boolean isAllGrant=true;
        for(int result:grantResults){
            if(result!=PackageManager.PERMISSION_GRANTED){
                isAllGrant=false;
                break;
            }
        }
        if(isAllGrant){
            permissionGrant.onPermissionGranted(requestCode);
        }else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permissions[0])){
                Toast.makeText(activity, "没有该权限无法执行此操作", Toast.LENGTH_SHORT).show();
                //openSettingActivity(activity,"没有该权限无法执行此操作");
            }else {

            }
        }
    }

    private static void openSettingActivity(final Activity activity, String message) {
        showMessageOKCancel(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Log.d(TAG, "getPackageName(): " + activity.getPackageName());
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
    }

}