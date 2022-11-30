package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.northeastern.numad22fa_mrp.R;

public class PropertyManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_manager);
    }
    /**
     * Method that is invoked on click of the floating action button which invokes an activity
     * to enter values.
     * @param view current view.
     */
    public void onFabPopupWindowClick(View view) {

    }
}