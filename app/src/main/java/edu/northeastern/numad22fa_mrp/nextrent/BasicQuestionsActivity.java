package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    private ImageView avatar;
    private EditText fullName;
    private EditText emailID;
    private EditText phoneNumber;
    final String[] legalSex = new String[1];
    private Button nextBtn;
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
        avatar = findViewById(R.id.user_avatar);
        if(bundle.getInt("imageId") == 0){
            avatar.setImageResource(R.drawable.user_profile_default);
        } else {
            avatar.setImageResource(bundle.getInt("imageId"));
        }

        //on click of avatar
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clickIntent = new Intent(BasicQuestionsActivity.this, UserAvatarActivity.class);
                clickIntent.putExtra("userKey", userKey);
                startActivity(clickIntent);
            }
        });
        RadioGroup legalSexRG  = (RadioGroup) findViewById(R.id.radioGroupLegalSex);
        legalSexRG.setOnCheckedChangeListener((group, checkedId) -> {
            // do operations specific to this selection
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            legalSex[0] = radioButton.getText().toString();
        });

        //age picker
        /*picker = (NumberPicker) findViewById(R.id.age_number_picker);
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
        });*/

        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the values,do validation.
                String seekerFullName = fullName.getText().toString();
                String seekerEmailId = emailID.getText().toString();
                String seekerPhone = phoneNumber.getText().toString();

                    //move to next screen with the data.
                    Intent clickIntent = new Intent(BasicQuestionsActivity.this, HouseQuestionsActivity.class);
                    clickIntent.putExtra("userKey", userKey);
                    clickIntent.putExtra("avatarId", String.valueOf(avatar.getId()));
                    clickIntent.putExtra("seekerFullName", seekerFullName);
                    clickIntent.putExtra("seekerEmailId", seekerEmailId);
                    clickIntent.putExtra("seekerPhone", seekerPhone);
                    clickIntent.putExtra("legalSex",legalSex[0]);
                    startActivity(clickIntent);

            }
        });
    }
}