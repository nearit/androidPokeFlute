package com.nearit.pokeflute;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.util.Arrays;
import java.util.List;

/**
 * @author Federico Boschini
 */
public class Utils {

    /**
     * Manufacturers known for using app blockers / power managers.
     */
    static final String MANUFACTURER_NOKIA = "hmd global";
    static final String MANUFACTURER_ONEPLUS = "oneplus";
    static final String MANUFACTURER_XIAOMI = "xiaomi";
    static final String MANUFACTURER_HUAWEI = "huawei";
    static final String MANUFACTURER_MEIZU = "meizu";
    static final String MANUFACTURER_OPPO = "oppo";
    static final String MANUFACTURER_SONY = "sony";
    static final String MANUFACTURER_SAMSUNG = "samsung";
    static final String MANUFACTURER_HTC = "htc";
    static final String MANUFACTURER_ASUS = "asus";
    static final String MANUFACTURER_VIVO = "vivo";
    static final String MANUFACTURER_ELEPHONE = "elephone";
    static final String MANUFACTURER_LENOVO = "lenovo";

    private static final List<String> BLOCKING_MANUFACTURERS = Arrays.asList(
            MANUFACTURER_NOKIA,
            MANUFACTURER_ONEPLUS,
            MANUFACTURER_XIAOMI,
            MANUFACTURER_HUAWEI,
            MANUFACTURER_MEIZU,
            MANUFACTURER_OPPO,
            MANUFACTURER_SONY,
            MANUFACTURER_SAMSUNG,
            MANUFACTURER_HTC,
            MANUFACTURER_ASUS,
            MANUFACTURER_VIVO,
            MANUFACTURER_ELEPHONE,
            MANUFACTURER_LENOVO
    );

    /**
     * Despite manufacturer tends to add some sort of App Blocker / Battery Optimizer, these devices
     * are not affected because Android One or Nexus phones.
     */
    private static final List<String> WHITELISTED_DEVICES = Arrays.asList(
            "tissot_sprout",    // Xiaomi A1
            "jasmine_sprout",   // Xiaomi A2
            "daisy_sprout",     // Xiaomi A2 Lite
            "angler"            // Huawei Nexus 6P
    );

    /**
     * Known app blockers / power managers intents.
     */
    static final Intent HUAWEI_INTENT_EMUI_5_AND_LATER = new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent HUAWEI_INTENT_EMUI_PREV_TO_5 = new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent OPPO_INTENT_1 = new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent OPPO_INTENT_2 = new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent OPPO_INTENT_3 = new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent VIVO_INTENT_1 = new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent VIVO_INTENT_2 = new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent VIVO_INTENT_3 = new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent ASUS_INTENT = new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")).setData(android.net.Uri.parse("mobilemanager://function/entry/AutoStart")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent XIAOMI_AUTOSTART_INTENT = new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent XIAOMI_AUTOSTART_INTENT_2 = new Intent("miui.intent.action.OP_AUTO_START").addCategory(Intent.CATEGORY_DEFAULT).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent XIAOMI_BATTERY_USAGE_RESTRICTION_INTENT = new Intent("miui.intent.action.POWER_HIDE_MODE_APP_LIST").addCategory(Intent.CATEGORY_DEFAULT).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent XIAOMI_INTERNET_DISABLER_INTENT = new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.powercenter.PowerSettings")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent SAMSUNG_INTENT_7_AND_LATER = new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent SAMSUNG_INTENT_LOLLIPOP = new Intent().setComponent(new ComponentName("com.samsung.android.sm", "com.samsung.android.sm.ui.battery.BatteryActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent LENOVO_INTENT = new Intent().setComponent(new ComponentName("com.lenovo.powersetting", "com.lenovo.powersetting.PowerSettingActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    static final Intent ONEPLUS_BATTERY_OPTIMIZATION_INTENT = new Intent().setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$BgOptimizeSwitchActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    @SuppressWarnings("WeakerAccess")
    static final Intent HTC_BOOST_APP_INTENT = new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    @SuppressWarnings("WeakerAccess")
    static final Intent NOKIA_INTENT = new Intent().setComponent(new ComponentName("com.evenwell.powersaving.g3", "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity3")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    @SuppressWarnings("WeakerAccess")
    static final Intent ONEPLUS_DEEP_OPTIMIZATION_INTENT = new Intent().setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$BgOptimizeAppListActivity")).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    @SuppressWarnings("WeakerAccess")
    public static final List<Intent> POWERMANAGER_INTENTS = Arrays.asList(
            XIAOMI_AUTOSTART_INTENT,
            HUAWEI_INTENT_EMUI_PREV_TO_5,
            HUAWEI_INTENT_EMUI_5_AND_LATER,
            OPPO_INTENT_1,
            OPPO_INTENT_2,
            OPPO_INTENT_3,
            VIVO_INTENT_1,
            VIVO_INTENT_2,
            VIVO_INTENT_3,
            ASUS_INTENT,
            NOKIA_INTENT,
            SAMSUNG_INTENT_7_AND_LATER,
            SAMSUNG_INTENT_LOLLIPOP,
            HTC_BOOST_APP_INTENT,
            LENOVO_INTENT,
            ONEPLUS_BATTERY_OPTIMIZATION_INTENT,
            ONEPLUS_DEEP_OPTIMIZATION_INTENT
    );


    /**
     * Checks if a power optimiser could be blocking/limiting the app.
     *
     * @return 'true' if the manufacturer is known for adopting app blockers, 'false' otherwise
     */
    public static boolean isAppPotentiallyBlockedByManufacturer() {
        boolean potentiallyBlocked = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (String manufacturer : BLOCKING_MANUFACTURERS) {
                if ((Build.MANUFACTURER.toLowerCase().contains(manufacturer) && !inWhiteList()) || oppoNougatOrRecent() || sonyLollipopOrRecent()) {
                    potentiallyBlocked = true;
                }
            }
        }
        return potentiallyBlocked;
    }

    /**
     * Checks if one of the known App Blockers is installed.
     *
     * @param context a valid Context
     * @return 'true' if an app blocker is installed, 'false' otherwise
     */
    public static boolean checkForAppBlockersExplicitly(Context context) {
        boolean installed = false;
        for (Intent intent : POWERMANAGER_INTENTS) {
            if (canStartActivity(context, intent)) installed = true;
        }
        return installed;
    }

    static boolean canStartActivity(Context context, Intent intent) {
        boolean intentResolved = false;
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            intentResolved = true;
        }
        return intentResolved;
    }

    private static boolean inWhiteList() {
        return WHITELISTED_DEVICES.contains(Build.DEVICE);
    }

    private static boolean oppoNougatOrRecent() {
        return Build.MANUFACTURER.toLowerCase().contains(MANUFACTURER_OPPO) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    private static boolean sonyLollipopOrRecent() {
        return Build.MANUFACTURER.toLowerCase().contains(MANUFACTURER_SONY) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    static boolean preNougat() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.N;
    }

    static boolean lollipopOrRecent() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    static boolean marshmallowOrRecent() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    static boolean oreo() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.O;
    }

    static boolean preOreo() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O;
    }

    static boolean preJellyBean() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN;
    }
}
