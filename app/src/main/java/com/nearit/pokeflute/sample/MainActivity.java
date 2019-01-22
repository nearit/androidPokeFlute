package com.nearit.pokeflute.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nearit.pokeflute.SolutionActivity;
import com.nearit.pokeflute.Utils;

/**
 * @author Federico Boschini
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (Utils.isAppPotentiallyBlockedByManufacturer()) {
            startActivity(SolutionActivity.createIntent(this));
        }

        // You can even enforce check
        if (Utils.checkForAppBlockersExplicitly(this)) {
            // eventually start SolutionActivity
        }
    }
}
