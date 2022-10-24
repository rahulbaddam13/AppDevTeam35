package edu.northeastern.numad22fa_mrp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button atYourService;
    private Button stickItToEm;
    private Button project;
    private Button my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        atYourService = (Button)findViewById(R.id.AtYourService);
        stickItToEm = (Button)findViewById(R.id.StickItToEm);
        project = (Button)findViewById(R.id.Project);
        my = (Button)findViewById(R.id.my);

        atYourService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openA7();
            }
        });
        stickItToEm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openA8();

            }
        });
        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProject();
            }
        });

    }



    private void openA7() {
        Intent intent = new Intent(this, AtYourService.class);
        startActivity(intent);
    }

    private void openA8() {
        Intent intent = new Intent(this, StickItToEm.class);
        startActivity(intent);
    }

    private void openProject() {
        Intent intent = new Intent(this, FinalProject.class);
        startActivity(intent);
    }

}