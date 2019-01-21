package com.nearit.pokeflute;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;

import java.util.Arrays;
import java.util.List;

/**
 * @author Federico Boschini
 */
class Utils {

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
            "angler",           // Huawei Nexus 6P
            "toro"              // Samsung Galaxy Nexus
    );

    /**
     * Known app blockers / power managers intents.
     */
    static final Intent HUAWEI_INTENT_EMUI_5_AND_LATER = new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity"));
    static final Intent HUAWEI_INTENT_EMUI_PREV_TO_5 = new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
    static final Intent OPPO_INTENT_1 = new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
    static final Intent OPPO_INTENT_2 = new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity"));
    static final Intent OPPO_INTENT_3 = new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity"));
    static final Intent VIVO_INTENT_1 = new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
    static final Intent VIVO_INTENT_2 = new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"));
    static final Intent VIVO_INTENT_3 = new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
    static final Intent ASUS_INTENT = new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")).setData(android.net.Uri.parse("mobilemanager://function/entry/AutoStart"));
    static final Intent XIAOMI_INTENT = new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
    static final Intent NOKIA_INTENT = new Intent().setComponent(new ComponentName("com.evenwell.powersaving.g3", "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity3"));
    static final Intent SAMSUNG_INTENT_7_AND_LATER = new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity"));
    static final Intent SAMSUNG_INTENT_LOLLIPOP = new Intent().setComponent(new ComponentName("com.samsung.android.sm", "com.samsung.android.sm.ui.battery.BatteryActivity"));
    static final Intent HTC_BOOST_APP_INTENT = new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity"));
    static final Intent LENOVO_INTENT = new Intent().setComponent(new ComponentName("com.lenovo.powersetting", "com.lenovo.powersetting.PowerSettingActivity"));
    static final Intent ONEPLUS_BATTERY_OPTIMIZATION_INTENT = new Intent().setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$BgOptimizeSwitchActivity"));
    static final Intent ONEPLUS_DEEP_OPTIMIZATION_INTENT = new Intent().setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$BgOptimizeAppListActivity"));

    public static final List<Intent> POWERMANAGER_INTENTS = Arrays.asList(
            XIAOMI_INTENT,
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
     * @return 'true' if an app blocker is installed, 'false' otherwise
     */
    public static boolean isAppPotentiallyBlockedByManufacturer() {
        boolean potentiallyBlocked = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String manufacturer : BLOCKING_MANUFACTURERS) {
                if (Build.MANUFACTURER.toLowerCase().contains(manufacturer) && !inWhiteList()) {
                    potentiallyBlocked = oppoNougatOrRecent() && sonyLollipopOrRecent();
                }
            }
        }
        return potentiallyBlocked;
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

}
