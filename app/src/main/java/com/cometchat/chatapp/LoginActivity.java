package com.cometchat.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

  private EditText emailEdt;
  private EditText passwordEdt;
  private Button loginBtn;
  private TextView registerTxt;

  private static final String EMPTY_STRING = "";

  private FirebaseAuth mAuth;
  private DatabaseReference mDatabase;

  private UserModel loggedInUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    this.initViews();
    this.initEvents();
    this.initFirebase();
    this.initFirebaseDatabase();
    this.initCometChat();
  }

  @Override
  protected void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null) {
       this.goToCometChatUI();
    }
  }

  private void initViews() {
    this.emailEdt = findViewById(R.id.emailEdt);
    this.passwordEdt = findViewById(R.id.passwordEdt);
    this.loginBtn = findViewById(R.id.loginBtn);
    this.registerTxt = findViewById(R.id.registerTxt);
  }

  private void initEvents() {
    this.loginBtn.setOnClickListener(this);
    this.registerTxt.setOnClickListener(this);
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
        Toast.makeText(LoginActivity.this, "Failed to initialize the CometChat", Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void goToCometChatUI() {
    startActivity(new Intent(LoginActivity.this, CometChatUI.class));
  }

  private void loginCometChat() {
    if (this.loggedInUser != null && this.loggedInUser.getUid() != null) {
      CometChat.login(this.loggedInUser.getUid(), Constants.COMETCHAT_AUTH_KEY, new CometChat.CallbackListener<User>() {

        @Override
        public void onSuccess(User user) {
          Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
          goToCometChatUI();
        }

        @Override
        public void onError(CometChatException e) {
        }
      });
    }
  }

  private void getUserDetails(String email) {
    if (email == null) {
      return;
    }
    this.mDatabase.child(Constants.FIREBASE_USERS).orderByChild(Constants.FIREBASE_EMAIL_KEY).equalTo(email).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
          loggedInUser = postSnapshot.getValue(UserModel.class);
          if (loggedInUser != null) {
            loginCometChat();
          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void callFirebaseAuthService(String email, String password) {
    this.mAuth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            getUserDetails(email);
          } else {
            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
          }
        }
      });
  }

  private boolean validateUserCredentials(String email, String password) {
    if (email != null && email.equals(EMPTY_STRING)) {
      Toast.makeText(LoginActivity.this, "Please input your email", Toast.LENGTH_LONG).show();
      return false;
    }
    if (password != null && password.equals(EMPTY_STRING)) {
      Toast.makeText(LoginActivity.this, "Please input your password", Toast.LENGTH_LONG).show();
      return false;
    }
    return true;
  }

  private void login() {
    String email = this.emailEdt.getText().toString().trim();
    String password = this.passwordEdt.getText().toString().trim();
    if (this.validateUserCredentials(email, password)) {
      // call firebase authentication service.
      this.callFirebaseAuthService(email, password);
    }
  }

  private void goToSignUpScreen() {
    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
    startActivity(intent);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.loginBtn:
        this.login();
        break;
      case R.id.registerTxt:
        this.goToSignUpScreen();
      default:
        break;
    }
  }
}