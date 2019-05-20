package com.example.jobd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private Button submitButton, logoutButton;
    private Uri imageUri;
    private DatabaseReference databaseUser;
    private FirebaseAuth auth;
    private StorageReference storageImage;
    private ProgressDialog progress;

    private String name, surname, dob, email, address, phone, postcode, image;

    private TextView nameView, surnameView, dobView, emailView, addressView, phoneView;

    private String userID;


    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progress = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        databaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        storageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");

        nameView = (TextView) findViewById(R.id.user_name);
        surnameView = (TextView) findViewById(R.id.user_surname);
        dobView = (TextView) findViewById(R.id.user_dob);
        emailView = (TextView) findViewById(R.id.user_email);
        phoneView = (TextView) findViewById(R.id.user_phone);
        addressView = (TextView) findViewById(R.id.user_address);
        imageButton = (ImageButton) findViewById(R.id.setupImageButton);
        submitButton = (Button) findViewById(R.id.setupSubmitbutton);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        /** display user data **/

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child(userID).child("name").getValue().toString();
                surname = dataSnapshot.child(userID).child("surname").getValue().toString();
                dob = dataSnapshot.child(userID).child("dob").getValue().toString();
                phone = dataSnapshot.child(userID).child("phone").getValue().toString();
                postcode = dataSnapshot.child(userID).child("postcode").getValue().toString();
                address = dataSnapshot.child(userID).child("address").getValue().toString();
                image = dataSnapshot.child(userID).child("image").getValue().toString();
                email = dataSnapshot.child(userID).child("email").getValue().toString();

                address = address + ", " + postcode;

                nameView.setText(name);
                surnameView.setText(surname);
                dobView.setText(dob);
                emailView.setText(email);
                phoneView.setText(phone);
                addressView.setText(address);
                //imageButton.setImageURI(image);
                Picasso.get().load(image).into(imageButton);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /** update image **/
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetupAccount();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent (ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });



        /** bottom navigation bar **/
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void startSetupAccount(){

        if(imageUri !=  null) {

            progress.setMessage("Finishing setup...");
            progress.show();

            StorageReference filepath = storageImage.child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    final String download_url = String.valueOf(downloadUrl);

                    //Toast.makeText(ProfileActivity.this, download_url, Toast.LENGTH_LONG).show();
                    databaseUser.child(userID).child("image").setValue(download_url);

                    progress.dismiss();
                }
            });
        }

    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                imageButton.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }



    /** Bottom navigation bar **/

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(ProfileActivity.this, WorkingHomeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_profile:
                    Intent intent1 = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_help:
                    Intent intent2 = new Intent(ProfileActivity.this, HelpActivity.class);
                    startActivity(intent2);
                    break;
            }

            return true;
        }
    };
}
