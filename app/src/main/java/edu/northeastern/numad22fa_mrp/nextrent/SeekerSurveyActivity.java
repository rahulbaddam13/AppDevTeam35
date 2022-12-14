package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad22fa_mrp.R;

public class SeekerSurveyActivity extends AppCompatActivity {

    private NumberPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_survey);

        ViewPager2 viewPager = findViewById(R.id.viewPager);

        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), getLifecycle());
        pagerAdapter.addFragment(new BasicQuestionsSeekerSurvey());
        pagerAdapter.addFragment(new HouseQuestionsSeekersSurvey());

        // set Orientation in your ViewPager2
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(pagerAdapter);


    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}