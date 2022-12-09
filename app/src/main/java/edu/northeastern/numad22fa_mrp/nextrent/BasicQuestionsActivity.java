package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import edu.northeastern.numad22fa_mrp.R;

public class BasicQuestionsActivity extends AppCompatActivity {

    //On touch parameters
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    //Number picker
    private NumberPicker picker;
    private String[] pickerVal = new String[83];

    private EditText fullName;
    private EditText emailID;
    private EditText phoneNumber;
    final String[] legalSex = new String[1];
    private String agePicker;
    //bundle with data from previous activity.
    Bundle bundle = null;
    String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_questions);

        bundle = getIntent().getExtras();
        userKey = bundle.getString("userKey");

        fullName = findViewById(R.id.editTextSeekerFullName);
        emailID = findViewById(R.id.editTextSeekerEmail);
        phoneNumber= findViewById(R.id.editTextSeekerPhoneNumber);

        RadioGroup legalSexRG  = (RadioGroup) findViewById(R.id.radioGroupLegalSex);
        legalSexRG.setOnCheckedChangeListener((group, checkedId) -> {
            // do operations specific to this selection
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            legalSex[0] = radioButton.getText().toString();
        });

        //age picker
        picker = (NumberPicker) findViewById(R.id.age_number_picker);
        int j = 0;
        for(int i = 18; i <= 100; i++,j++){
            pickerVal[j] = String.valueOf(i);
        }
        picker.setMinValue(18);
        picker.setMaxValue(pickerVal.length - 1);
        picker.setDisplayedValues(pickerVal);

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                agePicker = String.valueOf(picker.getValue());
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    if (x2 > x1)
                    {
                        //Do nothing
                    }// Right to left swipe action
                    else
                    {
                        //Get the values,do validation.
                        String seekerFullName = fullName.getText().toString();
                        String seekerEmailId = emailID.getText().toString();
                        String seekerPhone = phoneNumber.getText().toString();
                        if(seekerFullName.isEmpty()){
                            fullName.setBackgroundColor(Color.parseColor("#ff8282"));
                        } else {
                            //move to next screen with the data.
                            Intent clickIntent = new Intent(BasicQuestionsActivity.this, HouseQuestionsActivity.class);
                            clickIntent.putExtra("userKey", userKey);
                            clickIntent.putExtra("seekerFullName", seekerFullName);
                            clickIntent.putExtra("seekerEmailId", seekerEmailId);
                            clickIntent.putExtra("seekerPhone", seekerPhone);
                            clickIntent.putExtra("legalSex",legalSex[0]);
                            clickIntent.putExtra("age", agePicker);
                            startActivity(clickIntent);
                        }

                    }

                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}