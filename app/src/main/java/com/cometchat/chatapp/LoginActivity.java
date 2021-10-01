package com.cometchat.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

  private EditText emailEdt;
  private EditText passwordEdt;
  private Button loginBtn;
  private TextView registerTxt;

  private static final String EMPTY_STRING = "";

  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    this.initViews();
    this.initEvents();
    this.initFirebase();
  }

  @Override
  protected void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null) {
      this.goToMainScreen();
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

  private void goToMainScreen() {
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
  }

  private void callFirebaseAuthService(String email, String password) {
    this.mAuth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
            goToMainScreen();
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