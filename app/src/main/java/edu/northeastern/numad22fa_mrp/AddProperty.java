package edu.northeastern.numad22fa_mrp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
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
import androidx.core.content.FileProvider;

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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class AddProperty extends AppCompatActivity {

    EditText houseId, et_houseLocation, noOfRoom, rentPerRoom, houseDescription;
    Button add;
    TextView loc,unitNumber,state,country,type;
    ImageView houseImg;
    private static final int STORAGE_PERMISSION_CODE = 100;
    StorageReference storageReference;
    public static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String imageString;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    private ProgressDialog progressDialog;
    Button img;
    int CAMERA_REQUEST_CODE = 102;
    int CAMERA_PERM_CODE = 101;
    String currentPhotoPath;
    private Button galleryButton;
    private Button cameraButton;
    private AlertDialog imgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        houseImg = findViewById(R.id.iv_houseImage);
//        houseId = findViewById(R.id.et_houseId);
        noOfRoom = findViewById(R.id.et_noOfRoom);
        rentPerRoom = findViewById(R.id.et_rentPerRoom);
        houseDescription = findViewById(R.id.et_houseDescription);
//      country = findViewById(R.id.et_country);
        add = findViewById(R.id.btn_addHouse);
        loc = findViewById(R.id.et_Loc);
        String data = getIntent().getExtras().getString("location");
        loc.setText(data);
        state = findViewById(R.id.et_state);
        String stateD = getIntent().getExtras().getString("state");
        state.setText(stateD);
        country = findViewById(R.id.et_country);
        String countryD = getIntent().getExtras().getString("country");
        country.setText(countryD);
        type = findViewById(R.id.et_type);
        String typeD = getIntent().getExtras().getString("type");
        type.setText(typeD);




        storageReference = FirebaseStorage.getInstance().getReference().child("Uploads");


        houseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });

        progressDialog = new ProgressDialog(AddProperty.this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Adding...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
//                String houseId = AddProperty.this.houseId.getText().toString();
                String houseId = Long.toString(System.currentTimeMillis());
                String noOfRoom = AddProperty.this.noOfRoom.getText().toString();
                String rentPerRoom = AddProperty.this.rentPerRoom.getText().toString();
                String houseDescription = AddProperty.this.houseDescription.getText().toString();
                //String country = AddProperty.this.country.getText().toString();
                String image = imageString;
                createProperty(houseId,loc.getText().toString(),
                        noOfRoom, rentPerRoom, houseDescription,
                        country.getText().toString(),state.getText().toString(),type.getText().toString(),
                        image);
            }
        });

    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_photo, null);
        cameraButton = view.findViewById(R.id.add_photo_camera);
        galleryButton = view.findViewById(R.id.add_button_gallery);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
            }

        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
            }

        });

        builder.setTitle("Add a photo");
        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        imgDialog = builder.create();
        imgDialog.getWindow().setLayout(300, 150);
        imgDialog.show();
    }
    private void createProperty(String houseId, String houseLocation, String noOfRoom,
                                String rentPerRoom, String houseDescription,String country,
                                String state,String type, String image) {
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
        hashMap.put("type",type);


        databaseReference.child(houseId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void uploadImg() {
        final ProgressDialog pd = new ProgressDialog(AddProperty.this);
        pd.setMessage("Uploading...");
        pd.show();
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) throw Objects.requireNonNull(task.getException());
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    try {
                        Uri downloadingUri = task.getResult();
                        String mUri = downloadingUri.toString();
                        imageString = mUri;
                        Glide.with(AddProperty.this).load(imageUri).into(houseImg);
                    } catch (Exception e) {
                        Toast.makeText(AddProperty.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddProperty.this, "Failed here", Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                imageUri = Uri.fromFile(f);
                mediaScanIntent.setData(imageUri);
                this.sendBroadcast(mediaScanIntent);
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(AddProperty.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadImg();
                    imgDialog.cancel();
                }

            }
        }

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(AddProperty.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImg();
                imgDialog.cancel();
            }
        }
    }



    public void checkPermission(String permission, int requestCode) {
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
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
                Toast.makeText(AddProperty.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
        else{
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoF = null;
            try {
                photoF = createImageFile();
            } catch (IOException ex) {

            }

            if (photoF != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "edu.northeastern.numad22fa_mrp.fileprovider",
                        photoF);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }

        }
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgName = "JPEG_" + timeStamp + "_";
        File storageD = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageF = File.createTempFile(
                imgName,
                ".jpg",
                storageD
        );
        currentPhotoPath = imageF.getAbsolutePath();
        return imageF;
    }


}