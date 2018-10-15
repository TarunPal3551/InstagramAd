package appsforyou.tarun.com.instagramad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginScreenActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText emailEditText;
    EditText passwordEditText;
    Button login;
    private static final String TAG = "LoginScreenActivity";
    ProgressBar progressBar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView signUpTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        FirebaseApp.initializeApp(LoginScreenActivity.this);
        FirebaseApp.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        signUpTextView=(TextView)findViewById(R.id.signuptextview);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp=new Intent(LoginScreenActivity.this,SignupActivity.class);
                startActivity(signUp);
            }
        });
        emailEditText = (EditText) findViewById(R.id.editTextemail);
        passwordEditText = (EditText) findViewById(R.id.editTextpassword);
        login = (Button) findViewById(R.id.buttonlogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(LoginScreenActivity.this, "Enter Valid email and password", Toast.LENGTH_LONG).show();

                } else {

                    loginWithEmailandPassword(email, password);
                }


            }
        });
        updateUI(mAuth.getCurrentUser());


    }

    private void loginWithEmailandPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            databaseReference.child("User_Data").child(user.getUid()).child("Name").setValue(user.getDisplayName());
                            // databaseReference.child("User_Data").child(user)
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginScreenActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                        // ...
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            progressBar.setVisibility(View.GONE);

            Intent intent = new Intent(LoginScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginScreenActivity.this, "Login to InstaAds",
                    Toast.LENGTH_SHORT).show();

        }
    }
}
