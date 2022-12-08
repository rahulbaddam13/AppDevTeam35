package edu.northeastern.numad22fa_mrp.nextrent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import edu.northeastern.numad22fa_mrp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasicQuestionsSeekerSurvey#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicQuestionsSeekerSurvey extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NumberPicker picker;
    private String[] pickerVal = new String[82];

    public BasicQuestionsSeekerSurvey() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicQuestionsSeekerSurvey.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicQuestionsSeekerSurvey newInstance(String param1, String param2) {
        BasicQuestionsSeekerSurvey fragment = new BasicQuestionsSeekerSurvey();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_basic_questions_seeker_survey, container, false);

        /*EditText fullName = view.findViewById(R.id.seekerFullName);
        EditText emailID = view.findViewById(R.id.seekerEmailID);
        EditText phoneNumber = view.findViewById(R.id.seekerPhoneNumber);*/
        //age picker
        /*picker = view.findViewById(R.id.age_number_picker);
        picker.setMaxValue(83);
        picker.setMinValue(0);
        int j = 0;
        for(int i = 18; i <= 100; i++,j++){
            pickerVal[j] = String.valueOf(i);
        }
        picker.setDisplayedValues(pickerVal);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int valuePicker = picker.getValue();
                System.out.println("picker value "+ valuePicker + "");
            }
        });*/
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic_questions_seeker_survey, container, false);
    }
}