package com.nearit.pokeflute.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nearit.pokeflute.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

/**
 * @author Federico Boschini
 */
public class AdvancedSolution extends RelativeLayout {

    private final Context context;

    @Nullable
    private TextView header;
    @Nullable
    private ImageView expanderIcon;
    @Nullable
    private HtmlTextView solution;

    public AdvancedSolution(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AdvancedSolution(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public AdvancedSolution(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        inflate(context, R.layout.pf_layout_advanced_solution, this);
        header = findViewById(R.id.advancedSolutionHeader);
        expanderIcon = findViewById(R.id.advancedSolutionExpander);
        solution = findViewById(R.id.advancedSolutionText);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        if (expanderIcon != null) {
            expanderIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();
                }
            });
        }

        if (header != null) {
            header.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();
                }
            });
        }
    }

    private void toggle() {
        if (solution != null && expanderIcon != null) {
            if (solution.getVisibility() == View.GONE) {
                expanderIcon.setImageResource(R.drawable.pf_ic_arrow_drop_up_black_24dp);
                solution.setVisibility(View.VISIBLE);
            } else {
                expanderIcon.setImageResource(R.drawable.pf_ic_arrow_drop_down_black_24dp);
                solution.setVisibility(View.GONE);
            }
        }
    }

    public void setSolution(@StringRes int stringResIs) {
        if (solution != null) {
            solution.setHtml(context.getString(stringResIs));
        }
    }

    public void setHeader(@StringRes int stringResId) {
        if (header != null) {
            header.setText(stringResId);
        }
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }
}
