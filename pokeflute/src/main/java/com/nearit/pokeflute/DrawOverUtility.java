package com.nearit.pokeflute;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * @author Federico Boschini
 */
class DrawOverUtility {

    final static int DRAW_OVER_PERM_CODE = 6;

    private final Activity activity;
    private DrawOverAlertListener listener;

    DrawOverUtility(Activity activity, DrawOverAlertListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    boolean canDrawOver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(activity);
        } else {
            return true;
        }
    }

    void showDrawOverDialog() {
        showDrawOverDialog(activity, false);
    }

    void showEnforceDrawOverDialog() {
        showDrawOverDialog(activity, true);
    }

    private void showDrawOverDialog(final Activity activity, final boolean enforce) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setPositiveButton(R.string.enable_draw_over_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName())), DRAW_OVER_PERM_CODE);
                        }
                    })
                    .setNegativeButton(R.string.not_now_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onCanceled();
                        }
                    })
                    .setMessage(R.string.enable_draw_over_message);
            if (enforce) {
                builder.setTitle(R.string.dialog_title);
                builder.setMessage(R.string.enable_draw_over_enforce_message);
            }
            AlertDialog dialog = builder.create();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    listener.onCanceled();
                }
            });
            dialog.show();
        }
    }

    interface DrawOverAlertListener {
        void onCanceled();
    }

}
