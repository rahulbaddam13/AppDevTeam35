package edu.northeastern.numad22fa_mrp.nextrent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import edu.northeastern.numad22fa_mrp.R;

public class UserAvatarActivity extends AppCompatActivity {

    private ImageView avatar;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6;
    //bundle with data from previous activity.
    Bundle bundle = null;
    String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_avatar);

        bundle = getIntent().getExtras();
        userKey = bundle.getString("userKey");

        avatar = (ImageView) findViewById(R.id.user_avatar);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAvatarActivity.this, BasicQuestionsActivity.class);
                intent.putExtra("imageId", R.drawable.avatar_girl);
                intent.putExtra("userKey", userKey);
                startActivity(intent);
                finish();
            }
        });

        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAvatarActivity.this, BasicQuestionsActivity.class);
                intent.putExtra("imageId", R.drawable.boy_avatar);
                intent.putExtra("userKey", userKey);
                startActivity(intent);
                finish();
            }
        });
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAvatarActivity.this, BasicQuestionsActivity.class);
                intent.putExtra("imageId", R.drawable.punk_avatar);
                intent.putExtra("userKey", userKey);
                startActivity(intent);
                finish();
            }
        });
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAvatarActivity.this, BasicQuestionsActivity.class);
                intent.putExtra("imageId", R.drawable.emo_girl_avatar);
                intent.putExtra("userKey", userKey);
                startActivity(intent);
                finish();
            }
        });
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAvatarActivity.this, BasicQuestionsActivity.class);
                intent.putExtra("imageId", R.drawable.girl_two_avatar);
                intent.putExtra("userKey", userKey);
                startActivity(intent);
                finish();
            }
        });
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAvatarActivity.this, BasicQuestionsActivity.class);
                intent.putExtra("imageId", R.drawable.boy_two_avatar);
                intent.putExtra("userKey", userKey);
                startActivity(intent);
                finish();
            }
        });

    }
}