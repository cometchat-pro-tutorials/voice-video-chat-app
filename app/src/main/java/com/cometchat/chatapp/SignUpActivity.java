package com.cometchat.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
  private EditText usernameEdt;
  private EditText emailEdt;
  private EditText passwordEdt;
  private EditText confirmPasswordEdt;
  private Button registerBtn;

  private static final String EMPTY_STRING = "";

  private FirebaseAuth mAuth;
  private DatabaseReference mDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
    this.initViews();
    this.initEvents();
    this.initFirebase();
    this.initFirebaseDatabase();
    this.initCometChat();
  }

  private void initViews() {
    this.usernameEdt = findViewById(R.id.usernameEdt);
    this.emailEdt = findViewById(R.id.emailEdt);
    this.passwordEdt = findViewById(R.id.passwordEdt);
    this.confirmPasswordEdt = findViewById(R.id.confirmPasswordEdt);
    this.registerBtn = findViewById(R.id.registerBtn);
  }

  private void initEvents() {
    this.registerBtn.setOnClickListener(this);
  }

  private void initFirebase() {
    this.mAuth = FirebaseAuth.getInstance();
  }

  private void initFirebaseDatabase() {
    this.mDatabase = FirebaseDatabase.getInstance(Constants.FIREBASE_REALTIME_DATABASE_URL).getReference();
  }

  private void initCometChat() {
    AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(Constants.COMETCHAT_REGION).build();

    CometChat.init(this, Constants.COMETCHAT_APP_ID, appSettings, new CometChat.CallbackListener<String>() {
      @Override
      public void onSuccess(String successMessage) {
      }
      @Override
      public void onError(CometChatException e) {
        Toast.makeText(SignUpActivity.this, "Failed to initialize the CometChat", Toast.LENGTH_SHORT).show();
      }
    });
  }

  private String generateAvatar() {
    int avatarPosition = ThreadLocalRandom.current().nextInt(0, 5);
    String[] avatars = new String[]{
      "https://data-us.cometchat.io/assets/images/avatars/captainamerica.png",
      "https://data-us.cometchat.io/assets/images/avatars/cyclops.png",
      "https://data-us.cometchat.io/assets/images/avatars/ironman.png",
      "https://data-us.cometchat.io/assets/images/avatars/spiderman.png",
      "https://data-us.cometchat.io/assets/images/avatars/wolverine.png"
    };
    return avatars[avatarPosition];
  }

  private void goToLogin() {
    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
  }

  private void insertFirebaseDatabase(UserModel userModel) {
    // Write a message to the database
    if (userModel != null) {
      mDatabase.child(Constants.FIREBASE_USERS).child(userModel.getUid()).setValue(userModel);
    }
  }

  private void registerCometChatAccount(String username, String email) {
    if (username == null) {
      return;
    }
    String uid = UUID.randomUUID().toString();
    String avatar = this.generateAvatar();
    User user = new User();
    user.setUid(uid);
    user.setName(username);
    user.setAvatar(avatar);

    CometChat.createUser(user, Constants.COMETCHAT_AUTH_KEY, new CometChat.CallbackListener<User>() {
      @Override
      public void onSuccess(User user) {
        Toast.makeText(SignUpActivity.this, user.getName() + " has been created successfully", Toast.LENGTH_SHORT).show();
        UserModel userModel = new UserModel(uid, username, email, avatar);
        insertFirebaseDatabase(userModel);
        goToLogin();
      }

      @Override
      public void onError(CometChatException e) {
        Toast.makeText(SignUpActivity.this, "Failed to create your account. Please try again", Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void callFirebaseAuthService(String username, String email, String password) {
    this.mAuth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            // register to the CometChat.
            registerCometChatAccount(username, email);
          }
        }
      });
  }

  private boolean validateAccount(String username, String email, String password, String confirmPassword) {
    if (username == null || username.equals(EMPTY_STRING)) {
      Toast.makeText(SignUpActivity.this, "Please input your username", Toast.LENGTH_SHORT).show();
      return false;
    }
    if (email == null || email.equals(EMPTY_STRING)) {
      Toast.makeText(SignUpActivity.this, "Please input your email", Toast.LENGTH_SHORT).show();
      return false;
    }
    if (password == null || password.equals(EMPTY_STRING)) {
      Toast.makeText(SignUpActivity.this, "Please input your password", Toast.LENGTH_SHORT).show();
      return false;
    }
    if (confirmPassword == null || confirmPassword.equals(EMPTY_STRING)) {
      Toast.makeText(SignUpActivity.this, "Please input your confirm password", Toast.LENGTH_SHORT).show();
      return false;
    }
    if (!password.equals(confirmPassword)) {
      Toast.makeText(SignUpActivity.this, "The confirmed password and the password must be matched", Toast.LENGTH_SHORT).show();
      return false;
    }
    return true;
  }

  private void registerAccount() {
    String username = this.usernameEdt.getText().toString().trim();
    String email = this.emailEdt.getText().toString().trim();
    String password = this.passwordEdt.getText().toString().trim();
    String confirmPassword = this.confirmPasswordEdt.getText().toString().trim();
    if (this.validateAccount(username, email, password, confirmPassword)) {
      this.callFirebaseAuthService(username, email, password);
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.registerBtn:
        this.registerAccount();
        break;
    }
  }
}