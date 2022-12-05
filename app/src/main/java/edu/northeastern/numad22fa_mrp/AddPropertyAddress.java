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
    EditText unitNumber,state;
    EditText country;
    TextView dummy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property_address);
        location =findViewById(R.id.et_Location);
        pass = findViewById(R.id.passInfo);
        //unitNumber = findViewById(R.id.et_unitNumber);
        state = findViewById(R.id.et_state);
        country = findViewById(R.id.et_country);
        dummy = findViewById(R.id.dummy);
        Spinner spinner=findViewById(R.id.spinner_languages);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.propertyType, android.R.layout.simple_spinner_item);

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
                String label= adapterView.getItemAtPosition(i).toString();
                dummy.setText(label);
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
                String sData= state.getText().toString();
                String cData = country.getText().toString();
                String pDta = dummy.getText().toString();
                Intent i = new Intent(AddPropertyAddress.this, AddProperty.class);
                i.putExtra("location",data);
                //i.putExtra("unit",unit);
                i.putExtra("state",sData);
                i.putExtra("country",cData);
                i.putExtra("type",pDta);
                startActivity(i);
                finish();
            }
        });
    }
}