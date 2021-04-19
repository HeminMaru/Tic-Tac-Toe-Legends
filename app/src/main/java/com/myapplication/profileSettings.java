package com.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

import static com.google.firebase.auth.EmailAuthProvider.getCredential;

public class profileSettings extends AppCompatActivity {
    FirebaseAuth mAuth;

    EditText email_update,pass,username_update;
    Button update,cancel;
    ImageButton emailUpdatebutton, usernameUpdatebutton, avatar_ImageButton;
    FirebaseStorage storage;
    StorageReference storageReference;
    String ProfilePhoto;
    ImageView counter;
    AlertDialog avatardialog;
    public void PassReset(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.sendPasswordResetEmail(user.getEmail())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(profileSettings.this, "Password Reset mail sent successfully.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profileSettings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

   public void logout(View view) {
       new AlertDialog.Builder(profileSettings.this)
               .setTitle("Log Out")
               .setMessage("Are you sure you want to log out?")
               .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       mAuth.signOut();
                       Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                       startActivity(intent);
                   }
               })
               .setNegativeButton("Cancel", null)
               .setIcon(android.R.drawable.ic_dialog_alert)
               .show();
   }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }

    public void showemailDialog() {
        TextView email = findViewById(R.id.email_TextView);
        FirebaseUser user = mAuth.getCurrentUser();
        AlertDialog.Builder emailalert;
        emailalert = new AlertDialog.Builder(profileSettings.this, android.R.style.Theme_Material_Dialog_Alert);
        View Mview = getLayoutInflater().inflate(R.layout.dialog_email,null);
        email_update = Mview.findViewById(R.id.updateemailEdittext);
        pass = Mview.findViewById(R.id.passconfirm_EditText);
        update = Mview.findViewById(R.id.Update_Button);
        cancel = Mview.findViewById(R.id.Cancel_Button);
        emailalert.setView(Mview);
        AlertDialog emaildialog = emailalert.create();
        emaildialog.show();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(email_update.getText().toString().matches("") || pass.getText().toString().matches(""))) {
                    AuthCredential credential = getCredential(user.getEmail(), pass.getText().toString());
                    user.reauthenticate(credential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.updateEmail(email_update.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(profileSettings.this, "Email Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(profileSettings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(profileSettings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(profileSettings.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                }
                emaildialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emaildialog.dismiss();
            }
        });
        email.setText(user.getEmail());
    }
    public void showusernameDialog() {
        TextView username = findViewById(R.id.displayname_textView);
        FirebaseUser user = mAuth.getCurrentUser();
        AlertDialog.Builder usernamealert;
        usernamealert = new AlertDialog.Builder(profileSettings.this, android.R.style.Theme_Material_Dialog_Alert);
        View Mview = getLayoutInflater().inflate(R.layout.dialog_username,null);
        username_update = Mview.findViewById(R.id.updateusernameEdittext);
        update = Mview.findViewById(R.id.Update_Button);
        cancel = Mview.findViewById(R.id.Cancel_Button);
        usernamealert.setView(Mview);
        AlertDialog usernamedialog = usernamealert.create();
        usernamedialog.show();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username_update.getText().toString())
                        .build();
                user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(profileSettings.this, "Username updated successFully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profileSettings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                usernamedialog.dismiss();
                username.setText(user.getDisplayName());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernamedialog.dismiss();
            }
        });

    }
    /*
    public void showavatardialog(View v) {
        AlertDialog.Builder avataralert;
        avataralert = new AlertDialog.Builder(profileSettings.this, android.R.style.Theme_Material_Dialog_Alert);
        View Mview = getLayoutInflater().inflate(R.layout.dialog_avatar,null);
        avataralert.setView(Mview);
        avatardialog = avataralert.create();
        avatardialog.show();

    }
    public void onclick(View v) {
        FirebaseUser user = mAuth.getCurrentUser();
        counter = (ImageView) v;
        int position = Integer.parseInt(counter.getTag().toString());

        Log.i("success", counter.getTag().toString());
        if (position == 0)
            ProfilePhoto = "https://www.google.com/search?q=image&sxsrf=ALeKk020puoXwwLzngimhNNmVGAYAToLWA:1617885241635&source=lnms&tbm=isch&sa=X&ved=2ahUKEwiso_WY1O7vAhXBb30KHYXMDGEQ_AUoAXoECAEQAw&biw=1368&bih=722#imgrc=v_lcgG7X1hZauM";
        else if (position == 1)
            ProfilePhoto = "https://firebasestorage.googleapis.com/v0/b/tiktactoe-cd55c.appspot.com/o/avtar2.jpg";
        else if (position == 2)
            ProfilePhoto = "https://firebasestorage.googleapis.com/v0/b/tiktactoe-cd55c.appspot.com/o/avtar3.jpg?alt=media&token=d5d4e432-84b9-48f1-93f7-45bc8378c0bd ";
        else if (position == 3)
            ProfilePhoto = "https://firebasestorage.googleapis.com/v0/b/tiktactoe-cd55c.appspot.com/o/avtar4.jpg?alt=media&token=96ec6e05-3f25-4f91-af68-525ab82f966c ";
        else if (position == 4)
            ProfilePhoto = "https://firebasestorage.googleapis.com/v0/b/tiktactoe-cd55c.appspot.com/o/avtar5.jpg?alt=media&token=23110f8c-396d-45d8-86ad-191f20939450 ";
        else if (position == 5)
            ProfilePhoto = "https://firebasestorage.googleapis.com/v0/b/tiktactoe-cd55c.appspot.com/o/avtar6.jpg?alt=media&token=dbb1fd0b-f980-41a6-bc57-4c977b210c19 ";
        else if (position == 6)
            ProfilePhoto = "https://firebasestorage.googleapis.com/v0/b/tiktactoe-cd55c.appspot.com/o/avtar7.jpg?alt=media&token=6b7671cc-ccd8-4b77-af35-9b4d639e0e1e ";
        else if (position == 7)
            ProfilePhoto = "https://firebasestorage.googleapis.com/v0/b/tiktactoe-cd55c.appspot.com/o/avtar8.jpg?alt=media&token=2c0fdeb6-51be-4a68-9fa2-0058dc344f9a ";
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(ProfilePhoto))
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(profileSettings.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        avatardialog.dismiss();

        Drawable icon;
        try {
            InputStream inputStream = getContentResolver().openInputStream(user.getPhotoUrl());
            icon = new BitmapDrawable(getResources(),inputStream);
        } catch (Exception e) {
            icon = getDrawable(R.drawable.avtar1);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));
        avatar_ImageButton.setScaleType(ImageView.ScaleType.FIT_XY);
        avatar_ImageButton.setBackground(d);
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileSettings.super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        getSupportActionBar().hide();
        TextView email = findViewById(R.id.email_TextView);
        TextView username = findViewById(R.id.displayname_textView);
        usernameUpdatebutton = findViewById(R.id.Usernameupdate_button);
        emailUpdatebutton = findViewById(R.id.emailupdate_button);
        avatar_ImageButton = findViewById(R.id.Avatar_ImageButton);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        email.setText(user.getEmail());
        username.setText(user.getDisplayName());
        usernameUpdatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showusernameDialog();
            }
        });

        emailUpdatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showemailDialog();
            }

        });
        /*avatar_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showavatardialog(v);
            }
        });
         */
    }
}