package com.nearit.pokeflute;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.Arrays;
import java.util.List;

import static android.view.Gravity.BOTTOM;
import static com.nearit.pokeflute.DrawOverUtility.DRAW_OVER_PERM_CODE;
import static com.nearit.pokeflute.Utils.ASUS_INTENT;
import static com.nearit.pokeflute.Utils.HUAWEI_INTENT_EMUI_5_AND_LATER;
import static com.nearit.pokeflute.Utils.HUAWEI_INTENT_EMUI_PREV_TO_5;
import static com.nearit.pokeflute.Utils.MANUFACTURER_ASUS;
import static com.nearit.pokeflute.Utils.MANUFACTURER_ELEPHONE;
import static com.nearit.pokeflute.Utils.MANUFACTURER_HTC;
import static com.nearit.pokeflute.Utils.MANUFACTURER_HUAWEI;
import static com.nearit.pokeflute.Utils.MANUFACTURER_MEIZU;
import static com.nearit.pokeflute.Utils.MANUFACTURER_NOKIA;
import static com.nearit.pokeflute.Utils.MANUFACTURER_ONEPLUS;
import static com.nearit.pokeflute.Utils.MANUFACTURER_OPPO;
import static com.nearit.pokeflute.Utils.MANUFACTURER_SAMSUNG;
import static com.nearit.pokeflute.Utils.MANUFACTURER_SONY;
import static com.nearit.pokeflute.Utils.MANUFACTURER_VIVO;
import static com.nearit.pokeflute.Utils.MANUFACTURER_XIAOMI;
import static com.nearit.pokeflute.Utils.OPPO_INTENT_1;
import static com.nearit.pokeflute.Utils.OPPO_INTENT_2;
import static com.nearit.pokeflute.Utils.OPPO_INTENT_3;
import static com.nearit.pokeflute.Utils.SAMSUNG_INTENT;
import static com.nearit.pokeflute.Utils.VIVO_INTENT_1;
import static com.nearit.pokeflute.Utils.VIVO_INTENT_2;
import static com.nearit.pokeflute.Utils.VIVO_INTENT_3;
import static com.nearit.pokeflute.Utils.XIAOMI_INTENT;

/**
 * @author Federico Boschini
 */
public class SolutionActivity extends AppCompatActivity implements DrawOverUtility.DrawOverAlertListener {

    private final static String TAG = "SolutionActivity";

    private boolean drawnOverInstructions = false;

    private HtmlTextView solutionText;
    private HtmlTextView advancedSolutionText;
    private TextView advancedSolutionHeader;
    private AppCompatButton fixItButton;
    private RelativeLayout advancedSolutionContainer;
    private ImageView advancedSolutionExpander;
    private TextView dialogIntroMessage;

    @Nullable
    private OverlayInstructions overlayInstructions;

    private DrawOverUtility drawOverUtility;

    private final static Intent goToSettings = new Intent(android.provider.Settings.ACTION_SETTINGS);
    private final static Intent goToPowerManager = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawOverUtility = new DrawOverUtility(this, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (drawOverUtility.canDrawOver()) {
                drawnOverInstructions = true;
                init();
            } else {
                drawOverUtility.showDrawOverDialog();
            }
        } else {
            finish();
        }
    }

    private void init() {
        setContentView(R.layout.pf_activity_solution);
        dialogIntroMessage = findViewById(R.id.dialogIntroMessage);
        solutionText = findViewById(R.id.solutionText);
        advancedSolutionText = findViewById(R.id.advancedSolutionText);
        fixItButton = findViewById(R.id.goFixIt);
        advancedSolutionContainer = findViewById(R.id.advancedSolutionContainer);
        advancedSolutionExpander = findViewById(R.id.advancedSolutionExpander);
        advancedSolutionHeader = findViewById(R.id.advancedSolutionHeader);

        dialogIntroMessage.setText(Html.fromHtml(String.format(getResources().getString(R.string.dialog_intro_message), getResources().getString(R.string.fix_it_button))));

        showManufacturerSpecificSolution();

        AppCompatButton notNowButton = findViewById(R.id.notNow);
        notNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    public void onAdvancedSolutionClicked(View view) {
        if (advancedSolutionText.getVisibility() == View.GONE) {
            advancedSolutionExpander.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            advancedSolutionText.setVisibility(View.VISIBLE);
        } else {
            advancedSolutionExpander.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            advancedSolutionText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == DRAW_OVER_PERM_CODE) {
            if (!drawOverUtility.canDrawOver()) {
                drawOverUtility.showEnforceDrawOverDialog();
            } else {
                drawnOverInstructions = true;
                init();
            }
        }
    }

    private void showManufacturerSpecificSolution() {
        switch (Build.MANUFACTURER.toLowerCase()) {
            /*case MANUFACTURER_NOKIA: {
                solutionText.setHtml(getResources().getString(R.string.nokia));
                advancedSolutionContainer.setVisibility(View.VISIBLE);
                advancedSolutionText.setHtml(getResources().getString(R.string.nokia_advanced));
                advancedSolutionHeader.setText(R.string.advanced_header);
                onClickGoToSettings();
            }*/
            case MANUFACTURER_HUAWEI: {
                solutionText.setHtml(getResources().getString(R.string.huawei));
                advancedSolutionContainer.setVisibility(View.VISIBLE);
                advancedSolutionText.setHtml(getResources().getString(R.string.huawei_additional));
                advancedSolutionHeader.setText(R.string.additional_header);
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean started = startHuaweiActivity();
                        if (!started) startActivity(goToSettings);
                    }
                });
                break;
            }
            case MANUFACTURER_XIAOMI: {
                solutionText.setHtml(getResources().getString(R.string.xiaomi));
                advancedSolutionContainer.setVisibility(View.VISIBLE);
                advancedSolutionText.setHtml(getResources().getString(R.string.xiaomi_additional));
                advancedSolutionHeader.setText(R.string.additional_header);
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean started = startXiaomiActivity();
                        if (!started) startActivity(goToSettings);
                    }
                });
                break;
            }
            case MANUFACTURER_ASUS: {
                solutionText.setHtml(getResources().getString(R.string.asus));
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean started = startAsusActivity();
                        if (!started) startActivity(goToSettings);
                    }
                });
                break;
            }
            case MANUFACTURER_OPPO: {
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean started = startOppoActivity();
                        if (!started) startActivity(goToSettings);
                    }
                });
                break;
            }
            case MANUFACTURER_ONEPLUS: {
                solutionText.setHtml(getResources().getString(R.string.oneplus));
                onClickGoToSettings();
                break;
            }
            case MANUFACTURER_VIVO: {
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean started = startVivoActivity();
                        if (!started) startActivity(goToSettings);
                    }
                });
                break;
            }
            case MANUFACTURER_HTC: {
                solutionText.setHtml(getResources().getString(R.string.htc));
                onClickGoToSettings();
                break;
            }
            case MANUFACTURER_MEIZU: {
                if (!drawnOverInstructions)
                    solutionText.setHtml(getResources().getString(R.string.meizu));
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(goToSettings);
                        if (drawnOverInstructions)
                            drawOverEverything(getResources().getString(R.string.meizu_on_screen));
                    }
                });
                break;
            }
            case MANUFACTURER_SAMSUNG: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (!drawnOverInstructions)
                        solutionText.setHtml(getResources().getString(R.string.samsung_7_or_recent));
                    fixItButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean started = startSamsungActivity();
                            if (started) {
                                if (drawnOverInstructions)
                                    drawOverEverything(getResources().getString(R.string.samsung_7_or_recent_on_screen));
                            } else {
                                startActivity(goToSettings);
                            }
                        }
                    });
                } else {
                    solutionText.setHtml(getResources().getString(R.string.samsung));
                    onClickGoToSettings();
                }
                break;
            }
            case MANUFACTURER_SONY: {
                if (!drawnOverInstructions)
                    solutionText.setHtml(getResources().getString(R.string.sony));
                fixItButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean started = eventuallyStartActivity(goToPowerManager);
                        if (started) {
                            if (drawnOverInstructions)
                                drawOverEverything(getResources().getString(R.string.sony_on_screen));
                        } else {
                            startActivity(goToSettings);
                            if (drawnOverInstructions)
                                drawOverEverything(getResources().getString(R.string.sony_settings_on_screen));
                        }
                    }
                });
                break;
            }
            case MANUFACTURER_ELEPHONE: {
                solutionText.setHtml(getResources().getString(R.string.elephone));
                onClickGoToSettings();
                break;
            }
            default: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fixItButton.setText(R.string.go_to_settings_button);
                    dialogIntroMessage.setText(Html.fromHtml(String.format(getResources().getString(R.string.dialog_intro_message), getResources().getString(R.string.go_to_settings_button))));

                    if (!drawnOverInstructions)
                        solutionText.setHtml(getResources().getString(R.string.doze));

                    fixItButton.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent().setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                            if (drawnOverInstructions)
                                drawOverEverything(getResources().getString(R.string.doze));

                            // KEEP AS A REMINDER: startActivity(new Intent(Intent.ACTION_POWER_USAGE_SUMMARY));
                        }
                    });
                } else {
                    finish();
                }

                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (overlayInstructions != null && overlayInstructions.getWindowToken() != null) {
            getWindowManager().removeViewImmediate(overlayInstructions);
        }
    }

    private void drawOverEverything(String html) {
        WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
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

        overlayInstructions = new OverlayInstructions(this);
        overlayInstructions.setInstructions(html);

        overlayInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindowManager().removeViewImmediate(overlayInstructions);
            }
        });

        if (getWindowManager() != null) {
            getWindowManager().addView(overlayInstructions, params);
        }

        overlayInstructions.animateToBottom();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (overlayInstructions != null && getWindowManager() != null) {
            getWindowManager().removeViewImmediate(overlayInstructions);
        }
    }

    private boolean startHuaweiActivity() {
        List<Intent> huaweiIntents = Arrays.asList(HUAWEI_INTENT_EMUI_5_AND_LATER, HUAWEI_INTENT_EMUI_PREV_TO_5);
        for (Intent intent : huaweiIntents) {
            if (eventuallyStartActivity(intent)) return true;
        }
        return false;
    }

    private boolean startXiaomiActivity() {
        return eventuallyStartActivity(XIAOMI_INTENT);
    }

    private boolean startOppoActivity() {
        List<Intent> oppoIntents = Arrays.asList(OPPO_INTENT_1, OPPO_INTENT_2, OPPO_INTENT_3);
        for (Intent intent : oppoIntents) {
            if (eventuallyStartActivity(intent)) return true;
        }
        return false;
    }

    private boolean startAsusActivity() {
        return eventuallyStartActivity(ASUS_INTENT);
    }

    private boolean startVivoActivity() {
        List<Intent> vivoIntents = Arrays.asList(VIVO_INTENT_1, VIVO_INTENT_2, VIVO_INTENT_3);
        for (Intent intent : vivoIntents) {
            if (eventuallyStartActivity(intent)) return true;
        }
        return false;
    }

    private boolean startSamsungActivity() {
        return eventuallyStartActivity(SAMSUNG_INTENT);
    }

    private boolean eventuallyStartActivity(Intent intent) {
        boolean intentResolved = false;
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            startActivity(intent);
            intentResolved = true;
        }
        return intentResolved;
    }

    private void onClickGoToSettings() {
        dialogIntroMessage.setText(Html.fromHtml(String.format(getResources().getString(R.string.dialog_intro_message), getResources().getString(R.string.go_to_settings_button))));
        fixItButton.setText(R.string.go_to_settings_button);
        fixItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToSettings);
            }
        });
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, SolutionActivity.class);
    }

    @Override
    public void onCanceled() {
        drawnOverInstructions = false;
        init();
    }
}
