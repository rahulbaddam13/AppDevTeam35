package edu.northeastern.numad22fa_mrp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddPropertyAddress extends AppCompatActivity {
    EditText location;
    Button pass;
    EditText unitNumber,state;
    EditText country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property_address);
        location =findViewById(R.id.et_Location);
        pass = findViewById(R.id.passInfo);
        //unitNumber = findViewById(R.id.et_unitNumber);
        state = findViewById(R.id.et_state);
        country = findViewById(R.id.et_country);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = location.getText().toString();
                //String unit = unitNumber.getText().toString();
                String sData= state.getText().toString();
                String cData = country.getText().toString();
                Intent i = new Intent(AddPropertyAddress.this, AddProperty.class);
                i.putExtra("location",data);
                //i.putExtra("unit",unit);
                i.putExtra("state",sData);
                i.putExtra("country",cData);
                startActivity(i);
                finish();
            }
        });
    }
}