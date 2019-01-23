package com.nearit.pokeflute;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import static com.nearit.pokeflute.Utils.preJellyBean;

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
        inflate(context, R.layout.pf_layout_screen_overlay_instructions, this);
        instructions = findViewById(R.id.overlaySolutionText);
        container = findViewById(R.id.overlayMainContainer);
    }

    public void setInstructions(@NonNull String text) {
        instructions.setHtml(text);
    }

    public void animateFromBottom() {
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (preJellyBean()) {
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
