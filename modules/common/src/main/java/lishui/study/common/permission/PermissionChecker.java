package lishui.study.common.permission;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by lishui.lin on 20-7-29
 */
public class PermissionChecker {

    public void checkPermissions(Context context, String[] permissions, CheckResultCallback callback) {
        List<String> permissionRequestList = new ArrayList<>();
        Stream.of(permissions).forEach(permission -> {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionRequestList.add(permission);
            }
        });

        if (callback == null) {
            return;
        }

        if (permissionRequestList.isEmpty()) {
            callback.onPermissionsGranted();
        } else {
            String[] permissionArray = new String[permissionRequestList.size()];
            callback.onPermissionsDenied(permissionRequestList.toArray(permissionArray));
        }
    }

    public interface CheckResultCallback {
        void onPermissionsGranted();
        void onPermissionsDenied(String[] deniedPermission);
    }
}

