package edu.northeastern.numad22fa_mrp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddPropertyAddress extends AppCompatActivity {
    EditText location;
    Button pass;
    EditText state;
    EditText country;
    TextView dummy;
    EditText address;
    String label;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property_address);
        location = findViewById(R.id.et_Location);
        pass = findViewById(R.id.passInfo);
        //unitNumber = findViewById(R.id.et_unitNumber);
        state = findViewById(R.id.et_state);
        country = findViewById(R.id.et_country);
        address = findViewById(R.id.et_address);
        Spinner spinner = findViewById(R.id.spinner_languages);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.propertyType, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String label= adapterView.getItemAtPosition(i).toString();
//                dummy.setText(label);
//
//            }
//        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                label = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


            pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String data = location.getText().toString();
                    //String unit = unitNumber.getText().toString();
                    String tData = label;
                    String sData = state.getText().toString();
                    String cData = country.getText().toString();
                    String aData = address.getText().toString();
                    if (address.getText().toString().isEmpty()) {
                        address.setError("Please Enter the Address");
                    } else if (location.getText().toString().isEmpty()) {
                        location.setError("Please Enter the City");
                    } else if (state.getText().toString().isEmpty()) {
                        state.setError("Please Enter the State");
                    } else if (country.getText().toString().isEmpty()) {
                        country.setError("Please Enter the Country");
                    } else {
                        Intent i = new Intent(AddPropertyAddress.this, AddProperty.class);
                        i.putExtra("location", data);
                        //i.putExtra("unit",unit);
                        i.putExtra("address", aData);
                        i.putExtra("state", sData);
                        i.putExtra("country", cData);
                        i.putExtra("type", tData);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
    }
