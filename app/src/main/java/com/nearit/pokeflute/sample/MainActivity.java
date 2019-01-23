package com.nearit.pokeflute.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nearit.pokeflute.SolutionActivity;
import com.nearit.pokeflute.Utils;

/**
 * @author Federico Boschini
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 999;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (Utils.isAppPotentiallyBlockedByManufacturer()) {
            startActivityForResult(SolutionActivity.createIntent(this), REQUEST_CODE);
        }

        // You can even enforce check
        if (Utils.checkForAppBlockersExplicitly(this)) {
            // eventually start SolutionActivity
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Probably fixed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Dismissed. Not Fixed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
