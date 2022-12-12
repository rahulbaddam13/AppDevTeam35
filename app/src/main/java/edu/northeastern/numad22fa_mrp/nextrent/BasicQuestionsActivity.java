package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

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
    RadioGroup legalSexRG;
    RadioButton male, female, other;
    final String[] legalSex = new String[1];
    private Button nextBtn;
    //bundle with data from previous activity.
    Bundle bundle = null;
    String userKey,seekerFullName, seekerEmailId,seekerPhone, legalSexb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_questions);

        bundle = getIntent().getExtras();
        userKey = bundle.getString("userKey");
        seekerFullName = bundle.getString("seekerFullName");
        seekerEmailId =bundle.getString("seekerEmailId");
        seekerPhone = bundle.getString("seekerPhone");
        legalSexb = bundle.getString("legalSex");


        fullName = findViewById(R.id.editTextSeekerFullName);
        emailID = findViewById(R.id.editTextSeekerEmail);
        phoneNumber= findViewById(R.id.editTextSeekerPhoneNumber);
        legalSexRG = (RadioGroup) findViewById(R.id.radioGroupLegalSex);
        male = (RadioButton) findViewById(R.id.radioButtonMale);
        female = (RadioButton) findViewById(R.id.radioButtonFemale);
        other = (RadioButton) findViewById(R.id.radioButtonOthers);
        avatar = findViewById(R.id.user_avatar);
        if(bundle.getInt("imageId") == 0){
            avatar.setImageResource(R.drawable.user_profile_default);
        } else {
            avatar.setImageResource(bundle.getInt("imageId"));
        }

        if(seekerFullName != null){
            fullName.setText(seekerFullName);
        }
        if(seekerEmailId != null){
            emailID.setText(seekerEmailId);
        }
        if(seekerPhone!=null){
            phoneNumber.setText(seekerPhone);
        }
        if(legalSexb != null){
            if("Male".equalsIgnoreCase(legalSexb)){
                male.setSelected(true);
            } else if("Female".equalsIgnoreCase(legalSexb)){
                female.setSelected(true);
            } else{
                other.setSelected(true);
            }
        }

        //on click of avatar
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the values,do validation.
                String seekerFullName = fullName.getText().toString();
                String seekerEmailId = emailID.getText().toString();
                String seekerPhone = phoneNumber.getText().toString();

                Intent clickIntent = new Intent(BasicQuestionsActivity.this, UserAvatarActivity.class);
                clickIntent.putExtra("seekerFullName", seekerFullName);
                clickIntent.putExtra("seekerEmailId", seekerEmailId);
                clickIntent.putExtra("seekerPhone", seekerPhone);
                clickIntent.putExtra("legalSex",legalSex[0]);
                clickIntent.putExtra("userKey", userKey);
                startActivity(clickIntent);
            }
        });
        legalSexRG.setOnCheckedChangeListener((group, checkedId) -> {
            // do operations specific to this selection
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            legalSex[0] = radioButton.getText().toString();
        });

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