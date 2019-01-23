package com.nearit.pokeflute;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;

import static com.nearit.pokeflute.Utils.ASUS_INTENT;
import static com.nearit.pokeflute.Utils.HUAWEI_INTENT_EMUI_5_AND_LATER;
import static com.nearit.pokeflute.Utils.HUAWEI_INTENT_EMUI_PREV_TO_5;
import static com.nearit.pokeflute.Utils.LENOVO_INTENT;
import static com.nearit.pokeflute.Utils.ONEPLUS_BATTERY_OPTIMIZATION_INTENT;
import static com.nearit.pokeflute.Utils.OPPO_INTENT_1;
import static com.nearit.pokeflute.Utils.OPPO_INTENT_2;
import static com.nearit.pokeflute.Utils.OPPO_INTENT_3;
import static com.nearit.pokeflute.Utils.SAMSUNG_INTENT_7_AND_LATER;
import static com.nearit.pokeflute.Utils.SAMSUNG_INTENT_LOLLIPOP;
import static com.nearit.pokeflute.Utils.VIVO_INTENT_1;
import static com.nearit.pokeflute.Utils.VIVO_INTENT_2;
import static com.nearit.pokeflute.Utils.VIVO_INTENT_3;
import static com.nearit.pokeflute.Utils.XIAOMI_AUTOSTART_INTENT;
import static com.nearit.pokeflute.Utils.XIAOMI_AUTOSTART_INTENT_2;
import static com.nearit.pokeflute.Utils.XIAOMI_BATTERY_USAGE_RESTRICTION_INTENT;
import static com.nearit.pokeflute.Utils.XIAOMI_INTERNET_DISABLER_INTENT;
import static com.nearit.pokeflute.Utils.marshmallowOrRecent;

/**
 * @author Federico Boschini
 */
class ActivityLauncher {

    final static int POWER_MANAGEMENT_REQUEST_CODE = 555;
    private final Activity activity;

    private final static Intent goToSettings = new Intent(android.provider.Settings.ACTION_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    private final static Intent goToPowerManager = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    @RequiresApi(api = Build.VERSION_CODES.M)
    private final static Intent goToBatteryOptimization = new Intent().setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    ActivityLauncher(@NonNull Activity activity) {
        this.activity = activity;
    }

    void goToSettings(int requestCode) {
        activity.startActivityForResult(goToSettings, requestCode);
    }

    void goToSettings() {
        activity.startActivityForResult(goToSettings, POWER_MANAGEMENT_REQUEST_CODE);
    }

    boolean goToPowerManager() {
        return eventuallyStartActivity(goToPowerManager);
    }

    boolean goToBatteryOptimization() {
        if (marshmallowOrRecent()) {
            return eventuallyStartActivity(goToBatteryOptimization);
        }
        return false;
    }

    boolean startLenovoActivity() {
        return eventuallyStartActivity(LENOVO_INTENT);
    }

    boolean startOnePlusActivity() {
        return eventuallyStartActivity(ONEPLUS_BATTERY_OPTIMIZATION_INTENT);
    }

    boolean startHuawei7Activity() {
        return eventuallyStartActivity(HUAWEI_INTENT_EMUI_5_AND_LATER);
    }

    boolean startHuaweiPre7Activity() {
        return eventuallyStartActivity(HUAWEI_INTENT_EMUI_PREV_TO_5);
    }

    boolean startXiaomiAutoRunActivity() {
        List<Intent> autoRunIntents = Arrays.asList(XIAOMI_AUTOSTART_INTENT, XIAOMI_AUTOSTART_INTENT_2);
        for (Intent intent : autoRunIntents) {
            if (eventuallyStartActivity(intent)) return true;
        }
        return false;
    }

    boolean startXiaomiBatteryUsageActivity() {
        return eventuallyStartActivity(XIAOMI_BATTERY_USAGE_RESTRICTION_INTENT);
    }

    boolean startXiaomiInternetDisablerActivity() {
        return eventuallyStartActivity(XIAOMI_INTERNET_DISABLER_INTENT);
    }

    boolean startOppoActivity() {
        List<Intent> oppoIntents = Arrays.asList(OPPO_INTENT_1, OPPO_INTENT_2, OPPO_INTENT_3);
        for (Intent intent : oppoIntents) {
            if (eventuallyStartActivity(intent)) return true;
        }
        return false;
    }

    boolean startAsusActivity() {
        return eventuallyStartActivity(ASUS_INTENT);
    }

    boolean startVivoActivity() {
        List<Intent> vivoIntents = Arrays.asList(VIVO_INTENT_1, VIVO_INTENT_2, VIVO_INTENT_3);
        for (Intent intent : vivoIntents) {
            if (eventuallyStartActivity(intent)) return true;
        }
        return false;
    }

    boolean startSamsung7Activity() {
        return eventuallyStartActivity(SAMSUNG_INTENT_7_AND_LATER);
    }

    boolean startSamsungPre7Activity() {
        return eventuallyStartActivity(SAMSUNG_INTENT_LOLLIPOP);
    }

    boolean eventuallyStartActivity(Intent intent) {
        boolean intentResolved = false;
        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            activity.startActivityForResult(intent, POWER_MANAGEMENT_REQUEST_CODE);
            intentResolved = true;
        }
        return intentResolved;
    }

}
