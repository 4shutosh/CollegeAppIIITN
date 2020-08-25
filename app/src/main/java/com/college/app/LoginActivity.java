package com.college.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.college.app.databinding.ActivityLoginBinding;
import com.college.app.profile.ProfileActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    ActivityLoginBinding activityLoginBinding;

    BottomSheetBehavior<?> resetBottomSheetBehavior;
    BottomSheetBehavior<?> signUpBottomSheetBehavior;

    private FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;

    private Integer RC_SIGN_IN = 1;
    private Integer REQUEST_IMAGE_CAPTURE = 100;
    private Uri userImageUri;
    Bitmap bitmap;

    String userImageUrl;

    User user;

    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = activityLoginBinding.getRoot();

        setContentView(view);

        if (!isNetworkConnected()) {
            if (!isInternetAvailable()) {
                Toast.makeText(this, "Please connect to internet", Toast.LENGTH_LONG).show();
            }
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
//            }
//            else if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
//            }
//        }
        activityLoginBinding.btnLogin.setOnClickListener(v -> login());
        activityLoginBinding.txtSignup.setOnClickListener(v -> signUpBottomSheet());
        activityLoginBinding.resetText.setOnClickListener(v -> resetBottomSheet());

        activityLoginBinding.authIconsBar.googleIcon.setOnClickListener(v -> googleSignIn());

//        bottomsheetSignupBinding = BottomsheetSignupBinding.inflate(getLayoutInflater());
        activityLoginBinding.bottomSheetSingupLinearlayout.btnSingUp.setOnClickListener(v -> signUpUser());
//        bottomsheetSignupBinding.pillSignUp.setOnClickListener(v -> pillAction());


        activityLoginBinding.bottomSheetResetLinearlayout.btnReset.setOnClickListener(v -> passwordReset());
        activityLoginBinding.bottomSheetResetLinearlayout.pillReset.setOnClickListener(v -> pillAction());


        //  Firebase
        auth = FirebaseAuth.getInstance();
        configureGoogleSignIn();

        // bottom sheet
        ConstraintLayout br = findViewById(R.id.bottom_sheet_reset);
        ConstraintLayout bs = findViewById(R.id.bottom_sheet_signup);
        resetBottomSheetBehavior = BottomSheetBehavior.from(br);
        signUpBottomSheetBehavior = BottomSheetBehavior.from(bs);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Toast.makeText(this, "Data found", Toast.LENGTH_SHORT).show();
            Bundle extras = intent.getExtras();
            String title = extras.getString("Title");
            String body = extras.getString("Message");
        }
    }


    @Override
    public void onBackPressed() {
        if (resetBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            resetBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            activityLoginBinding.bottomSheetResetLinearlayout.resetEmail.getText().clear();
        } else if (signUpBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            signUpBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            activityLoginBinding.emailLogin.getText().clear();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
//        FirebaseUser currentUSer = auth.getCurrentUser();
//        updateUI(currentUSer);
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void resetBottomSheet() {
        if (resetBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            resetBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            activityLoginBinding.emailLogin.getText().clear();
            activityLoginBinding.bottomSheetResetLinearlayout.resetEmail.getText().clear();
        } else {
            resetBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            activityLoginBinding.bottomSheetResetLinearlayout.resetEmail.getText().clear();
        }
    }

    void login() {
        if (activityLoginBinding.emailLogin.getText().toString().isEmpty()) {
            activityLoginBinding.emailLogin.setError(getResources().getString(R.string.username_login_error_username));
            activityLoginBinding.emailLogin.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(activityLoginBinding.emailLogin.getText().toString()).matches()) {
            activityLoginBinding.emailLogin.setError(getResources().getString(R.string.username_login_error_email));
            activityLoginBinding.emailLogin.requestFocus();
            return;
        }
        if (activityLoginBinding.passwordLogin.getText().toString().isEmpty()) {
            activityLoginBinding.passwordLogin.setError(getResources().getString(R.string.password_error));
            activityLoginBinding.passwordLogin.requestFocus();
            return;
        }
        activityLoginBinding.progressCircular.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(activityLoginBinding.emailLogin.getText().toString(), activityLoginBinding.passwordLogin.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(LoginActivity.this, R.string.login_in,
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            activityLoginBinding.progressCircular.setVisibility(View.GONE);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, getResources().getText(R.string.error_authentication_failed),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        activityLoginBinding.emailLogin.getText().clear();
        activityLoginBinding.passwordLogin.getText().clear();
    }


    private void updateUI(FirebaseUser currentUser) {

        if (currentUser != null) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("mode", "Login");
            LoginActivity.this.startActivity(intent);
            LoginActivity.this.finish();
        } else {
            Toast.makeText(
                    this, getResources().getText(R.string.error_signIn), Toast.LENGTH_SHORT
            ).show();
        }
    }

    // google sign in
    private void configureGoogleSignIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    void googleSignIn() {
        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Google SignIn Failed!", Toast.LENGTH_SHORT).show();
            }
        }
//        } else if (requestCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
////            userImageUri = data.getData();
////            try {
////                if (Build.VERSION.SDK_INT >= 29) {
//////                ImageDecoder.decodeBitmap(ImageDecoder.createSource());
////                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(userImageUri, "r");
////                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
////                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
////                    inputUserImage.setImageBitmap(bitmap);
////                } else {
////                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), userImageUri);
////                    Picasso.with(this).load(String.valueOf(bitmap)).into(inputUserImage);
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////                Log.i(TAG, "onActivityResult: Failed image load");
////            }
//        }
////            inputUserImage.setImageBitmap(bitmap);
////            uploadImageToFirebaseStorage(bitmap);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acc) {
        activityLoginBinding.progressCircular.setVisibility(View.VISIBLE);
        Log.d(TAG, "firebaseAuthWithGoogle:" + acc.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            activityLoginBinding.progressCircular.setVisibility(View.GONE);
//                            updateUI(user);

                            final String name = user.getDisplayName();
                            final String email = user.getEmail();
                            final String photoUrl = user.getPhotoUrl().toString();
                            // add image as well "PhotoUri"

                            mUser = new User(name, email, photoUrl);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: database write success");
                                        Toast.makeText(LoginActivity.this, getText(R.string.success), Toast.LENGTH_SHORT).show();
                                        updateUI(user);
                                    } else {
                                        // TODO add error message
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getBaseContext(), getResources().getText(R.string.error_authentication_failed), Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    void passwordReset() {
        if (resetBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

            if (activityLoginBinding.bottomSheetResetLinearlayout.resetEmail.getText().toString().isEmpty()) {
                activityLoginBinding.bottomSheetResetLinearlayout.resetEmail.setError(getResources().getString(R.string.username_login_error_username));
                activityLoginBinding.bottomSheetResetLinearlayout.resetEmail.requestFocus();
                return;
            }
            auth.sendPasswordResetEmail(activityLoginBinding.bottomSheetResetLinearlayout.resetEmail.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), getResources().getText(R.string.message_emailSent), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(), getResources().getText(R.string.error_noUser), Toast.LENGTH_SHORT).show();
                        }
                    });
            activityLoginBinding.bottomSheetResetLinearlayout.resetEmail.getText().clear();
            resetBottomSheet();
        }
    }

    //Sign Up Bottom Sheet and functions
    void signUpUser() {
        final String email = activityLoginBinding.bottomSheetSingupLinearlayout.emailSignUp.getText().toString().trim();
        final String name = activityLoginBinding.bottomSheetSingupLinearlayout.nameSignUp.getText().toString().trim();
        final String passwordCreate = activityLoginBinding.bottomSheetSingupLinearlayout.passwordSignUp.getText().toString().trim();


        if (signUpBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            if (email.isEmpty()) {
                activityLoginBinding.bottomSheetSingupLinearlayout.emailSignUp.setError(getText(R.string.error_input_email));
                activityLoginBinding.bottomSheetSingupLinearlayout.emailSignUp.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                activityLoginBinding.bottomSheetSingupLinearlayout.emailSignUp.setError(getText(R.string.error_input_email));
                activityLoginBinding.bottomSheetSingupLinearlayout.emailSignUp.requestFocus();
                return;
            }
            if (passwordValidator(passwordCreate)) {
                activityLoginBinding.bottomSheetSingupLinearlayout.passwordSignUp.setError(getText(R.string.input_error_password));
                activityLoginBinding.bottomSheetSingupLinearlayout.passwordSignUp.requestFocus();
                return;
            }
            if (activityLoginBinding.bottomSheetSingupLinearlayout.passwordSignUp.getText().toString().isEmpty()) {
                activityLoginBinding.bottomSheetSingupLinearlayout.passwordSignUp.setError(getText(R.string.input_error_enterPassword));
                activityLoginBinding.bottomSheetSingupLinearlayout.passwordSignUp.requestFocus();
                return;
            }
            activityLoginBinding.bottomSheetSingupLinearlayout.progressSignUp.setVisibility(View.GONE);
            auth.createUserWithEmailAndPassword(
                    activityLoginBinding.bottomSheetSingupLinearlayout.emailSignUp.getText().toString(),
                    activityLoginBinding.bottomSheetSingupLinearlayout.passwordSignUp.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                user = new User(name, email, null);
                                // add image option in SignUp

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, getText(R.string.success), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                            intent.putExtra("mode", "Login");
                                            hideKeyboard(LoginActivity.this);
                                            signUpBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                            activityLoginBinding.progressCircular.setVisibility(View.GONE);
                                            LoginActivity.this.startActivity(intent);
                                            Toast.makeText(LoginActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                            Log.e(TAG, "onComplete: accoutn created");
                                            finish();
                                        } else {
                                            // TODO add error message
                                        }
                                    }
                                });
                                Log.d(TAG, "createUserWithEmail:success");
                                signUpBottomSheet();
                                activityLoginBinding.progressCircular.setVisibility(View.VISIBLE);

                                // TODO start main activity
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, getResources().getText(R.string.error_failed),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
            activityLoginBinding.bottomSheetSingupLinearlayout.nameSignUp.getText().clear();
            activityLoginBinding.bottomSheetSingupLinearlayout.emailSignUp.getText().clear();
            activityLoginBinding.bottomSheetSingupLinearlayout.passwordSignUp.getText().clear();
            activityLoginBinding.bottomSheetSingupLinearlayout.emailSignUp.getText().clear();
            activityLoginBinding.bottomSheetSingupLinearlayout.progressSignUp.setVisibility(View.GONE);

        }
    }

    private boolean passwordValidator(String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$)$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    void signUpBottomSheet() {
        if (signUpBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            signUpBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            activityLoginBinding.emailLogin.getText().clear();
            activityLoginBinding.passwordLogin.getText().clear();
        } else {
            signUpBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            activityLoginBinding.bottomSheetSingupLinearlayout.emailSignUp.getText().clear();
            activityLoginBinding.bottomSheetSingupLinearlayout.passwordSignUp.getText().clear();
        }
    }

    void pillAction() {
        if (resetBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            resetBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        if (signUpBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            signUpBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    //    @OnClick(R.id.userImage_input)
    void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            } else {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                galleryIntent.setType("image/*");

                Intent chooser = new Intent(Intent.ACTION_CHOOSER);
                chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
                chooser.putExtra(Intent.EXTRA_TITLE, getText(R.string.selectFrom));

                Intent[] intentArray = {cameraIntent};
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooser, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void uploadImageToFirebaseStorage(Bitmap b) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        String uid = "";
        StorageReference userImageReference = FirebaseStorage
                .getInstance()
                .getReference("UserImages/" + auth.getCurrentUser().getUid() + ".jpg");

        if (userImageUri != null) {
            userImageReference.putFile(userImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            userImageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, getText(R.string.input_image_upload_error), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        activityLoginBinding.bottomSheetSingupLinearlayout.passwordSignUp.findFocus().clearFocus();

    }


    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}



