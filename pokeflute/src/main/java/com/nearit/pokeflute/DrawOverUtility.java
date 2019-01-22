package com.nearit.pokeflute;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import static android.view.Gravity.BOTTOM;
import static com.nearit.pokeflute.Utils.marshmallowOrRecent;
import static com.nearit.pokeflute.Utils.oreo;
import static com.nearit.pokeflute.Utils.preOreo;

/**
 * @author Federico Boschini
 */
class DrawOverUtility {

    final static int DRAW_OVER_PERM_CODE = 6;

    private final Activity activity;
    private DrawOverAlertListener listener;

    @Nullable
    private OverlayInstructions overlayInstructions;

    DrawOverUtility(Activity activity, DrawOverAlertListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    boolean canDrawOver() {
        if (marshmallowOrRecent()) {
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
        if (marshmallowOrRecent()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setPositiveButton(R.string.pf_enable_draw_over_dialog_positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName())), DRAW_OVER_PERM_CODE);
                        }
                    })
                    .setNegativeButton(R.string.pf_enable_draw_over_dialog_negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onCanceled();
                        }
                    })
                    .setMessage(R.string.pf_enable_draw_over_dialog_message);
            if (enforce) {
                builder.setTitle(R.string.pf_dialog_title);
                builder.setMessage(R.string.pf_enable_draw_over_dialog_enforce_message);
            }
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    void eventuallyDrawOverEverything(int stringRes) {
        if (canDrawOver() || oreo()) {
            WindowManager.LayoutParams params;
            if (preOreo()) {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        PixelFormat.TRANSLUCENT);
            } else {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }

            params.gravity = BOTTOM;

            overlayInstructions = new OverlayInstructions(activity);
            overlayInstructions.setInstructions(activity.getString(stringRes));

            overlayInstructions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.getWindowManager().removeViewImmediate(overlayInstructions);
                }
            });

            if (activity.getWindowManager() != null) {
                try {
                    /*
                      Workaround for Android O bug https://issuetracker.google.com/issues?q=Settings.canDrawOverlays
                      Try to draw even if permission seems to be denied
                     */
                    activity.getWindowManager().addView(overlayInstructions, params);
                } catch (WindowManager.BadTokenException ignored) {
                }
            }

            overlayInstructions.animateFromBottom();
        }
    }

    void removeInstructions() {
        if (overlayInstructions != null && overlayInstructions.getWindowToken() != null && activity.getWindowManager() != null) {
            activity.getWindowManager().removeViewImmediate(overlayInstructions);
        }
    }

    interface DrawOverAlertListener {
        void onCanceled();
    }

}
