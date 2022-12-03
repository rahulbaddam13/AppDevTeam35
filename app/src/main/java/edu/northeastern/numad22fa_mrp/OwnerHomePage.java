package edu.northeastern.numad22fa_mrp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OwnerHomePage extends AppCompatActivity {

    Button addProperty, showProperty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_homepage);

        addProperty = findViewById(R.id.btn_addHouse);
        showProperty = findViewById(R.id.btn_seeHouse);

        showProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomePage.this, PropertyList.class));
            }
        });

        addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomePage.this, AddPropertyAddress.class));
            }
        });
    }
}