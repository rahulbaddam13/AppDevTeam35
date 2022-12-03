package edu.northeastern.numad22fa_mrp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddProperty extends AppCompatActivity {

    EditText houseId, et_houseLocation, noOfRoom, rentPerRoom, houseDescription;
    Button add;
    TextView loc,unitNumber,state,country;
    ImageView houseImg;


    private static final int STORAGE_PERMISSION_CODE = 100;
    StorageReference storageReference;
    public static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String imageString;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        houseImg = findViewById(R.id.iv_houseImage);
        houseId = findViewById(R.id.et_houseId);
        noOfRoom = findViewById(R.id.et_noOfRoom);
        rentPerRoom = findViewById(R.id.et_rentPerRoom);
        houseDescription = findViewById(R.id.et_houseDescription);
//        country = findViewById(R.id.et_country);
        add = findViewById(R.id.btn_addHouse);
        loc = findViewById(R.id.et_Loc);
        String data = getIntent().getExtras().getString("location");
        loc.setText(data);
        Log.v("RahulL",loc.getText().toString());
        state = findViewById(R.id.et_state);
        String stateD = getIntent().getExtras().getString("state");
        state.setText(stateD);
        Log.v("RahulS",state.getText().toString());
        country = findViewById(R.id.et_country);
        String countryD = getIntent().getExtras().getString("country");
        country.setText(countryD);
        Log.v("RahulC",country.getText().toString());
        //unitNumber = findViewById(R.id.et_unit);
//        String data1 = getIntent().getExtras().getString("unit");
//        unitNumber.setText(data1);


        storageReference = FirebaseStorage.getInstance().getReference().child("Uploads");

        houseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
            }
        });

        progressDialog = new ProgressDialog(AddProperty.this);

        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                progressDialog.setMessage("Adding the Property");
                progressDialog.setTitle("Adding...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                String houseId = AddProperty.this.houseId.getText().toString();
                String noOfRoom = AddProperty.this.noOfRoom.getText().toString();
                String rentPerRoom = AddProperty.this.rentPerRoom.getText().toString();
                String houseDescription = AddProperty.this.houseDescription.getText().toString();
                //String country = AddProperty.this.country.getText().toString();
                String image = imageString;
                createProperty(houseId,loc.getText().toString(),
                        noOfRoom, rentPerRoom, houseDescription,
                        country.getText().toString(),state.getText().toString(), image);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createProperty(String houseId, String houseLocation, String noOfRoom,
                                String rentPerRoom, String houseDescription,String country,
                                String state, String image) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(OwnerRegister.HOUSES).child(userId);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("houseId", houseId);
        hashMap.put("noOfRoom", noOfRoom);
        hashMap.put("houseDescription", houseDescription);
        hashMap.put("houseLocation", houseLocation);
        hashMap.put("rentPerRoom", rentPerRoom);
        hashMap.put("houseImage", image);
        hashMap.put("userId", userId);
        hashMap.put("country",country);
        hashMap.put("state",state);
        databaseReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(AddProperty.this, "Property Added Successfully", Toast.LENGTH_SHORT).show();
                    imageString = "";
                } else {
                    Toast.makeText(AddProperty.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //TODO : include reference

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = AddProperty.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(AddProperty.this);
        pd.setMessage("Uploading...");
        pd.show();
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) throw task.getException();
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        try {
                            Uri downloadingUri = task.getResult();
                            Log.d("TAG", "onComplete: uri completed");
                            String mUri = downloadingUri.toString();
                            imageString = mUri;
                            Glide.with(AddProperty.this).load(imageUri).into(houseImg);
                        } catch (Exception e) {
                            Log.d("TAG1", "error Message: " + e.getMessage());
                            Toast.makeText(AddProperty.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        pd.dismiss();
                    } else {
                        Toast.makeText(AddProperty.this, "Failed here", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProperty.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(AddProperty.this, "No image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(AddProperty.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(AddProperty.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AddProperty.this, new String[]{permission}, requestCode);
        } else {
            openImage();
            Toast.makeText(AddProperty.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImage();
                Toast.makeText(AddProperty.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddProperty.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}