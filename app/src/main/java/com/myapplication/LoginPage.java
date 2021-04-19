package com.myapplication;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginPage extends AppCompatActivity implements View.OnClickListener{
    SignInButton signInButton;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth mAuth;
    boolean isreg = false;
    EditText Email_ID;
    EditText User_Name;
    EditText Password;
    TextView Forgot_Pass;
    public void reglogin(View view) {
        Email_ID = findViewById(R.id.emailEditText);
        User_Name = findViewById(R.id.usernameEditText);
        Password = findViewById(R.id.passwordEditText);
        if(Email_ID.getText().toString().matches("")&&User_Name.getText().toString().matches("")&&Password.getText().toString().matches("")) {
            Toast.makeText(this, "Some of the credentials are empty.", Toast.LENGTH_SHORT).show();
        }
        else {
            if (!isreg) {
                mAuth.signInWithEmailAndPassword(Email_ID.getText().toString().trim(), Password.getText().toString().trim())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                    startActivity(intent);
                                    Toast.makeText(LoginPage.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();

                                } else {
                                    Toast.makeText(LoginPage.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            } else {
                mAuth.createUserWithEmailAndPassword(Email_ID.getText().toString().trim(), Password.getText().toString().trim())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginPage.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(User_Name.getText().toString().trim()).build();
                                    user.updateProfile(profileUpdates).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    assert user != null;
                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(LoginPage.this, "Verification mail sent successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    isreg = !isreg;
                                    Button reglogin = findViewById(R.id.regloginButton);
                                    TextView ChangeText = findViewById(R.id.switchTextView);
                                    TextView aboveChangeText = findViewById(R.id.aboveswitchTextView);
                                    reglogin.setText("Log in");
                                    aboveChangeText.setText("Don't have an Account?");
                                    ChangeText.setText("Click here to register!");
                                    Forgot_Pass.setVisibility(View.VISIBLE);
                                    User_Name.setVisibility(View.INVISIBLE);
                                    Email_ID.setText("");
                                    Password.setText("");
                                    mAuth.signOut();

                                } else{
                                    Toast.makeText(LoginPage.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }

        }
    }
    public void onClick(View view) {
        if(view.getId()==R.id.switchTextView) {
            Button reglogin = findViewById(R.id.regloginButton);
            TextView ChangeText = findViewById(R.id.switchTextView);
            TextView aboveChangeText = findViewById(R.id.aboveswitchTextView);
            Email_ID = findViewById(R.id.emailEditText);
            User_Name = findViewById(R.id.usernameEditText);
            Password = findViewById(R.id.passwordEditText);
            Forgot_Pass = findViewById(R.id.forgotpasstextView);
            isreg = !isreg;
            if (isreg) {
                reglogin.setText("Register");
                aboveChangeText.setText("Already have an Account?");
                ChangeText.setText("Click here to login!");
                User_Name.setVisibility(View.VISIBLE);
                Forgot_Pass.setVisibility(View.INVISIBLE);
                User_Name.setText("");
                Password.setText("");
                Email_ID.setText("");

            } else {
                reglogin.setText("Log in");
                aboveChangeText.setText("Don't have an Account?");
                ChangeText.setText("Click here to register!");
                User_Name.setVisibility(View.INVISIBLE);
                Forgot_Pass.setVisibility(View.VISIBLE);
                User_Name.setText("");
                Password.setText("");
            }
        }
        else if(view.getId() == R.id.forgotpasstextView) {
            final EditText input = new EditText(this);
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setTitle("Reset Password")
                    .setMessage("Enter an email you used during registration.")
                    .setView(input)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = String.valueOf(input.getText());
                            mAuth = FirebaseAuth.getInstance();
                            mAuth.sendPasswordResetEmail(value)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(LoginPage.this, "Password Reset mail sent successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                     })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    }).show();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        signInButton =findViewById(R.id.googleSignInButton);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("140723331381-b33s2cvle4tnkhpp7304fapv7uicu15a.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginPage.this, googleSignInOptions);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100) {
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            if(googleSignInAccountTask.isSuccessful()) {
                try {
                    GoogleSignInAccount googleSignInAccount = googleSignInAccountTask.
                            getResult(ApiException.class);
                    GoogleAuthCredential googleAuthCredential = (GoogleAuthCredential) GoogleAuthProvider
                            .getCredential(googleSignInAccount.getIdToken(),null);
                    mAuth.signInWithCredential(googleAuthCredential);
                } catch (ApiException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Google Sign in Successfull", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }else if(googleSignInAccountTask.isCanceled()) {
                Toast.makeText(this, "Google Sign in revoked", Toast.LENGTH_SHORT).show();
            }
            googleSignInAccountTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}