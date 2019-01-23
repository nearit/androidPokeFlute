package com.nearit.pokeflute;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nearit.pokeflute.views.AdvancedSolution;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import static com.nearit.pokeflute.DrawOverUtility.DRAW_OVER_PERM_CODE;
import static com.nearit.pokeflute.Utils.HUAWEI_INTENT_EMUI_5_AND_LATER;
import static com.nearit.pokeflute.Utils.HUAWEI_INTENT_EMUI_PREV_TO_5;
import static com.nearit.pokeflute.Utils.MANUFACTURER_ASUS;
import static com.nearit.pokeflute.Utils.MANUFACTURER_ELEPHONE;
import static com.nearit.pokeflute.Utils.MANUFACTURER_HTC;
import static com.nearit.pokeflute.Utils.MANUFACTURER_HUAWEI;
import static com.nearit.pokeflute.Utils.MANUFACTURER_LENOVO;
import static com.nearit.pokeflute.Utils.MANUFACTURER_MEIZU;
import static com.nearit.pokeflute.Utils.MANUFACTURER_NOKIA;
import static com.nearit.pokeflute.Utils.MANUFACTURER_ONEPLUS;
import static com.nearit.pokeflute.Utils.MANUFACTURER_OPPO;
import static com.nearit.pokeflute.Utils.MANUFACTURER_SAMSUNG;
import static com.nearit.pokeflute.Utils.MANUFACTURER_SONY;
import static com.nearit.pokeflute.Utils.MANUFACTURER_VIVO;
import static com.nearit.pokeflute.Utils.MANUFACTURER_XIAOMI;
import static com.nearit.pokeflute.Utils.XIAOMI_AUTOSTART_INTENT;
import static com.nearit.pokeflute.Utils.XIAOMI_AUTOSTART_INTENT_2;
import static com.nearit.pokeflute.Utils.XIAOMI_BATTERY_USAGE_RESTRICTION_INTENT;
import static com.nearit.pokeflute.Utils.canStartActivity;
import static com.nearit.pokeflute.Utils.isDozeOptimized;
import static com.nearit.pokeflute.Utils.lollipopOrRecent;
import static com.nearit.pokeflute.Utils.oreo;
import static com.nearit.pokeflute.Utils.preNougat;

/**
 * @author Federico Boschini
 */
public class SolutionActivity extends AppCompatActivity implements DrawOverUtility.DrawOverAlertListener {

    @SuppressWarnings("unused")
    private final static String TAG = "SolutionActivity";

    private final static int XIAOMI_REQUEST_CODE = 777;

    private boolean canDrawOver = false;
    private boolean drawPermissionCouldBeDenied = false;
    private boolean dozeFixLaunched = false;

    @Nullable
    private HtmlTextView solutionText;
    @Nullable
    private AppCompatButton fixItButton;
    @Nullable
    private AdvancedSolution advancedSolution;
    @Nullable
    private TextView dialogIntroMessage;

    private DrawOverUtility drawOverUtility;
    private ActivityLauncher activityLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawOverUtility = new DrawOverUtility(this, this);
        activityLauncher = new ActivityLauncher(this);

        if (lollipopOrRecent()) {
            if (drawOverUtility.canDrawOver()) {
                canDrawOver = true;
                init();
            } else {
                drawOverUtility.showDrawOverDialog();
            }
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void init() {
        setContentView(R.layout.pf_activity_solution);
        dialogIntroMessage = findViewById(R.id.dialogIntroMessage);
        solutionText = findViewById(R.id.solutionText);
        fixItButton = findViewById(R.id.goFixIt);
        advancedSolution = findViewById(R.id.advancedSolution);

        if (dialogIntroMessage != null) {
            dialogIntroMessage.setText(Html.fromHtml(getStringRes(R.string.pf_default_intro_message)));
        }

        showManufacturerSpecificSolution();
    }

    public void dismissActivity(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case DRAW_OVER_PERM_CODE: {
                if (!drawOverUtility.canDrawOver()) {
                    if (!oreo()) {
                        drawOverUtility.showEnforceDrawOverDialog();
                    } else {
                        drawPermissionCouldBeDenied = true;
                        init();
                    }
                } else {
                    canDrawOver = true;
                    init();
                }
                break;
            }
            case XIAOMI_REQUEST_CODE: {
                break;
            }
            default: {
                drawOverUtility.removeInstructions();

                if (dozeFixLaunched) {
                    if (!isDozeOptimized(this)) {
                        setResult(RESULT_OK);
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                } else {
                    setResult(RESULT_OK);
                }

                finish();
            }
        }
    }

    private void showManufacturerSpecificSolution() {
        switch (Build.MANUFACTURER.toLowerCase()) {
            case MANUFACTURER_NOKIA: {
                handleNokia();
                break;
            }
            case MANUFACTURER_HUAWEI: {
                handleHuawei();
                break;
            }
            case MANUFACTURER_XIAOMI: {
                handleXiaomi();
                break;
            }
            case MANUFACTURER_ASUS: {
                handleAsus();
                break;
            }
            case MANUFACTURER_OPPO: {
                handleOppo();
                break;
            }
            case MANUFACTURER_ONEPLUS: {
                handleOnePlus();
                break;
            }
            case MANUFACTURER_VIVO: {
                handleVivo();
                break;
            }
            case MANUFACTURER_HTC: {
                handleHtc();
                break;
            }
            case MANUFACTURER_MEIZU: {
                handleMeizu();
                break;
            }
            case MANUFACTURER_SAMSUNG: {
                handleSamsung();
                break;
            }
            case MANUFACTURER_SONY: {
                handleSony();
                break;
            }
            case MANUFACTURER_ELEPHONE: {
                handleElephone();
                break;
            }
            case MANUFACTURER_LENOVO: {
                handleLenovo();
                break;
            }
            default: {
                handleDefault();
                break;
            }
        }
    }

    private void handleNokia() {
        if (dialogIntroMessage != null) {
            dialogIntroMessage.setText(R.string.pf_nokia_intro_message);
        }
        if (solutionText != null) {
            solutionText.setHtml(getStringRes(R.string.pf_nokia_default));
        }
        if (advancedSolution != null) {
            advancedSolution.show();
            advancedSolution.collapse();
            advancedSolution.setSolution(R.string.pf_nokia_advanced);
            advancedSolution.setHeader(R.string.pf_advanced_header);
        }
        if (fixItButton != null) {
            fixItButton.setText(R.string.pf_got_it_button);
            fixItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }

    private void handleHuawei() {
        // TODO: should show instructions for power plan too?
        // advancedSolution.show();
        // advancedSolutuion.setSolution(R.string.huawei_additional));
        // advancedSolution.setHeader(R.string.pf_additional_header);
        if (preNougat()) {
            if (solutionText != null && (!canDrawOver || drawPermissionCouldBeDenied)) {
                if (!canStartActivity(this, HUAWEI_INTENT_EMUI_PREV_TO_5)) {
                    solutionText.setHtml(getStringRes(R.string.pf_huawei_pre_7_default));
                } else {
                    solutionText.setHtml(getStringRes(R.string.pf_huawei_pre_7_on_screen));
                }
            }
            if (fixItButton != null) {
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean started = activityLauncher.startHuaweiPre7Activity();
                        if (started) {
                            drawOverUtility.eventuallyDrawOverEverything(R.string.pf_huawei_pre_7_on_screen);
                        } else {
                            activityLauncher.goToSettings();
                            drawOverUtility.eventuallyDrawOverEverything(R.string.pf_huawei_pre_7_on_screen_from_settings);
                        }
                    }
                });
            }
        } else {
            if (solutionText != null && (!canDrawOver || drawPermissionCouldBeDenied)) {
                if (!canStartActivity(this, HUAWEI_INTENT_EMUI_5_AND_LATER)) {
                    if (!canStartActivity(this, HUAWEI_INTENT_EMUI_PREV_TO_5)) {
                        solutionText.setHtml(getStringRes(R.string.pf_huawei_7_or_recent_default));
                    } else {
                        solutionText.setHtml(getStringRes(R.string.pf_huawei_7_or_recent_lockscreen_cleanup));
                    }
                } else {
                    solutionText.setHtml(getStringRes(R.string.pf_huawei_7_or_recent_on_screen));
                }
            }
            if (fixItButton != null) {
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean started = activityLauncher.startHuawei7Activity();
                        if (started) {
                            drawOverUtility.eventuallyDrawOverEverything(R.string.pf_huawei_7_or_recent_on_screen);
                        } else {
                            boolean fallbackStarted = activityLauncher.startHuawei7FallbackActivity();
                            if (fallbackStarted) {
                                drawOverUtility.eventuallyDrawOverEverything(R.string.pf_huawei_7_or_recent_lockscreen_cleanup_on_screen);
                            } else {
                                activityLauncher.goToSettings();
                                drawOverUtility.eventuallyDrawOverEverything(R.string.pf_huawei_7_or_recent_on_screen_from_settings);
                            }
                        }
                    }
                });
            }
        }
    }

    private void handleXiaomi() {
        setContentView(R.layout.pf_activity_xiaomi_solution);

        AppCompatButton notNow = findViewById(R.id.xiaomiNotNowButton);
        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        HtmlTextView batterySolution = findViewById(R.id.xiaomiBatteryUsageSolution);
        if (canStartActivity(this, XIAOMI_BATTERY_USAGE_RESTRICTION_INTENT)) {
            if (!canDrawOver || drawPermissionCouldBeDenied) {
                batterySolution.setHtml(getStringRes(R.string.pf_xiaomi_battery_restriction));
            } else {
                batterySolution.setHtml(getStringRes(R.string.pf_xiaomi_battery_restriction_on_screen));
            }
        } else {
            batterySolution.setHtml(getStringRes(R.string.pf_xiaomi_battery_restriction_on_screen_from_settings));
        }
        AppCompatButton batteryButton = findViewById(R.id.xiaomiBatteryUsageButton);
        batteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean started = activityLauncher.startXiaomiBatteryUsageActivity();
                if (started) {
                    drawOverUtility.eventuallyDrawOverEverything(R.string.pf_xiaomi_battery_restriction_on_screen);
                } else {
                    activityLauncher.goToSettings(XIAOMI_REQUEST_CODE);
                    drawOverUtility.eventuallyDrawOverEverything(R.string.pf_xiaomi_battery_restriction_on_screen_from_settings);
                }
            }
        });

        HtmlTextView autoRunSolution = findViewById(R.id.xiaomiAutoRunSolution);
        if (canStartActivity(this, XIAOMI_AUTOSTART_INTENT) || canStartActivity(this, XIAOMI_AUTOSTART_INTENT_2)) {
            if (!canDrawOver || drawPermissionCouldBeDenied) {
                autoRunSolution.setHtml(getStringRes(R.string.pf_xiaomi_autorun));
            } else {
                autoRunSolution.setHtml(getStringRes(R.string.pf_xiaomi_autorun_on_screen));
            }
        } else {
            autoRunSolution.setHtml(getStringRes(R.string.pf_xiaomi_autorun_on_screen_from_settings));
        }
        AppCompatButton autoRunButton = findViewById(R.id.xiaomiAutoRunButton);
        autoRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean started = activityLauncher.startXiaomiAutoRunActivity();
                if (started) {
                    drawOverUtility.eventuallyDrawOverEverything(R.string.pf_xiaomi_autorun_on_screen);
                } else {
                    activityLauncher.goToSettings(XIAOMI_REQUEST_CODE);
                    drawOverUtility.eventuallyDrawOverEverything(R.string.pf_xiaomi_autorun_on_screen_from_settings);
                }
            }
        });

        HtmlTextView internetDisablerSolution = findViewById(R.id.xiaomiInternetDisablerSolution);
        if (canStartActivity(this, XIAOMI_BATTERY_USAGE_RESTRICTION_INTENT)) {
            if (!canDrawOver || drawPermissionCouldBeDenied) {
                internetDisablerSolution.setHtml(getStringRes(R.string.pf_xiaomi_internet_disabler));
            } else {
                internetDisablerSolution.setHtml(getStringRes(R.string.pf_xiaomi_internet_disabler_on_screen));
            }
        } else {
            internetDisablerSolution.setHtml(getStringRes(R.string.pf_xiaomi_internet_disabler_on_screen_from_settings));
        }
        AppCompatButton internetDisablerButton = findViewById(R.id.xiaomiInternetDisablerButton);
        internetDisablerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean started = activityLauncher.startXiaomiInternetDisablerActivity();
                if (started) {
                    drawOverUtility.eventuallyDrawOverEverything(R.string.pf_xiaomi_internet_disabler_on_screen);
                } else {
                    activityLauncher.goToSettings(XIAOMI_REQUEST_CODE);
                    drawOverUtility.eventuallyDrawOverEverything(R.string.pf_xiaomi_internet_disabler_on_screen_from_settings);
                }
            }
        });
    }

    private void handleAsus() {
        if (solutionText != null && (!canDrawOver || drawPermissionCouldBeDenied)) {
            solutionText.setHtml(getStringRes(R.string.pf_asus_default));
        }
        if (fixItButton != null) {
            fixItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean started = activityLauncher.startAsusActivity();
                    if (started) {
                        drawOverUtility.eventuallyDrawOverEverything(R.string.pf_asus_on_screen);
                    } else {
                        showError();
                        // TODO: not sure if Asus Mobile Manager can be accessed from settings
                        /*startActivityForResult(goToSettings, POWER_MANAGEMENT_REQUEST_CODE);
                        eventuallyDrawOverEverything(getStringRes(R.string.asus_on_screen_from_settings));*/
                    }
                }
            });
        }
    }

    private void handleOppo() {
        if (fixItButton != null) {
            fixItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean started = activityLauncher.startOppoActivity();
                    if (!started) activityLauncher.goToSettings();
                }
            });
        }
    }

    private void handleOnePlus() {
        if (solutionText != null && (!canDrawOver || drawPermissionCouldBeDenied)) {
            solutionText.setHtml(getStringRes(R.string.pf_oneplus_default));
        }
        if (fixItButton != null) {
            fixItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean started = activityLauncher.startOnePlusActivity();
                    if (started) {
                        drawOverUtility.eventuallyDrawOverEverything(R.string.pf_oneplus_on_screen);
                    } else {
                        activityLauncher.goToPowerManager();
                        drawOverUtility.eventuallyDrawOverEverything(R.string.pf_oneplus_on_screen_from_battery_settings);
                    }
                }
            });
        }
    }

    private void handleVivo() {
        if (fixItButton != null) {
            fixItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean started = activityLauncher.startVivoActivity();
                    if (!started) activityLauncher.goToSettings();
                }
            });
        }
    }

    private void handleHtc() {
        if (solutionText != null && (!canDrawOver || drawPermissionCouldBeDenied)) {
            solutionText.setHtml(getStringRes(R.string.pf_htc_default));
        }
        if (fixItButton != null) {
            fixItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean started = activityLauncher.goToBatteryOptimization();
                    if (started) {
                        drawOverUtility.eventuallyDrawOverEverything(R.string.pf_htc_on_screen);
                    } else {
                        activityLauncher.goToSettings();
                        drawOverUtility.eventuallyDrawOverEverything(R.string.pf_htc_on_screen_from_settings);
                    }
                }
            });
        }
    }

    private void handleMeizu() {
        // TODO: should show instructions for power plan too?
        /*advancedSolutionContainer.setVisibility(View.VISIBLE);
        advancedSolutionText.setHtml(getStringRes(R.string.meizu_advanced));
        advancedSolutionHeader.setText(R.string.additional_header);*/
        if (solutionText != null && (!canDrawOver || drawPermissionCouldBeDenied)) {
            solutionText.setHtml(getStringRes(R.string.pf_meizu_default));
        }
        if (fixItButton != null) {
            fixItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activityLauncher.goToSettings();
                    drawOverUtility.eventuallyDrawOverEverything(R.string.pf_meizu_on_screen);
                }
            });
        }
    }

    private void handleSamsung() {
        if (preNougat()) {
            if (solutionText != null && (!canDrawOver || drawPermissionCouldBeDenied)) {
                solutionText.setHtml(getStringRes(R.string.pf_samsung_pre_7_default));
            }
            if (fixItButton != null) {
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean started = activityLauncher.startSamsungPre7Activity();
                        if (started) {
                            drawOverUtility.eventuallyDrawOverEverything(R.string.pf_samsung_pre_7_on_screen);
                        } else {
                            activityLauncher.goToSettings();
                            drawOverUtility.eventuallyDrawOverEverything(R.string.pf_samsung_pre_7_on_screen_from_settings);
                        }
                    }
                });
            }
        } else {
            if (solutionText != null && (!canDrawOver || drawPermissionCouldBeDenied)) {
                solutionText.setHtml(getStringRes(R.string.pf_samsung_7_or_recent_default));
            }
            if (fixItButton != null) {
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean started = activityLauncher.startSamsung7Activity();
                        if (started) {
                            drawOverUtility.eventuallyDrawOverEverything(R.string.pf_samsung_7_or_recent_on_screen);
                        } else {
                            activityLauncher.goToSettings();
                            drawOverUtility.eventuallyDrawOverEverything(R.string.pf_samsung_7_or_recent_on_screen_from_settings);
                        }
                    }
                });
            }
        }
    }

    private void handleSony() {
        if (solutionText != null && (!canDrawOver || drawPermissionCouldBeDenied)) {
            solutionText.setHtml(getStringRes(R.string.pf_sony_default));
        }

        if (fixItButton != null) {
            fixItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean started = activityLauncher.goToPowerManager();
                    if (started) {
                        drawOverUtility.eventuallyDrawOverEverything(R.string.pf_sony_on_screen);
                    } else {
                        activityLauncher.goToSettings();
                        drawOverUtility.eventuallyDrawOverEverything(R.string.pf_sony_on_screen_from_settings);
                    }
                }
            });
        }
    }

    private void handleElephone() {
        if (solutionText != null) {
            solutionText.setHtml(getStringRes(R.string.pf_elephone_default));
        }
        if (fixItButton != null) {
            fixItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activityLauncher.goToSettings();
                }
            });
        }
    }

    private void handleLenovo() {
        if (solutionText != null && (!canDrawOver || drawPermissionCouldBeDenied)) {
            solutionText.setHtml(getStringRes(R.string.pf_lenovo_default));
        }
        if (fixItButton != null) {
            fixItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean started = activityLauncher.startLenovoActivity();
                    if (started) {
                        drawOverUtility.eventuallyDrawOverEverything(R.string.pf_lenovo_on_screen);
                    } else {
                        activityLauncher.goToSettings();
                        drawOverUtility.eventuallyDrawOverEverything(R.string.pf_lenovo_on_screen_from_settings);
                    }
                }
            });
        }
    }

    private void handleDefault() {
        if (isDozeOptimized(this)) {
            if (fixItButton != null) {
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activityLauncher.goToBatteryOptimization();
                        drawOverUtility.eventuallyDrawOverEverything(R.string.pf_doze_default);

                        dozeFixLaunched = true;

                        // KEEP AS A REMINDER: startActivity(new Intent(Intent.ACTION_POWER_USAGE_SUMMARY));

                        // KEEP AS A REMINDER, requires permission, can cause publish rejection:
                        // startActivity(requestWhiteList.setData(Uri.parse("package:" + getPackageName())));
                    }
                });
            }

            if (solutionText != null && (!canDrawOver || drawPermissionCouldBeDenied)) {
                solutionText.setHtml(getStringRes(R.string.pf_doze_default));
            }

        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    private String getStringRes(int resId) {
        try {
            return getString(resId);
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }

    private void showError() {
        Toast.makeText(this, "Cannot fix issue", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCanceled() {
        canDrawOver = false;
        init();
    }


    public static Intent createIntent(Context context) {
        return new Intent(context, SolutionActivity.class);
    }
}
