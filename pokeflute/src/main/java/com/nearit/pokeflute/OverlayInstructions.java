package com.nearit.pokeflute;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import org.sufficientlysecure.htmltextview.HtmlTextView;

/**
 * @author Federico Boschini
 */
public class OverlayInstructions extends RelativeLayout {

    private final Context context;
    private HtmlTextView instructions;
    private RelativeLayout container;

    public OverlayInstructions(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public OverlayInstructions(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public OverlayInstructions(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        inflate(context, R.layout.layout_screen_overlay_instructions, this);
        instructions = findViewById(R.id.overlaySolutionText);
        container = findViewById(R.id.overlayMainContainer);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(OverlayInstructions.class.getSimpleName(), "pppp");
            }
        });
    }

    public void setInstructions(@NonNull String text) {
        instructions.setHtml(text);
    }

    public void animateToBottom() {
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    container.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                int displayHeight = context.getResources().getDisplayMetrics().heightPixels;
                WindowManager windowManager = (WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE);
                if (windowManager != null) {
                    Display display = windowManager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    displayHeight = point.y;
                }

                Animation animation = new TranslateAnimation(
                        0,
                        0,
                        displayHeight,
                        displayHeight - getBaseline() - container.getHeight()
                );
                animation.setDuration(500);
                animation.setFillAfter(true);
                container.startAnimation(animation);
            }
        });

    }

}
