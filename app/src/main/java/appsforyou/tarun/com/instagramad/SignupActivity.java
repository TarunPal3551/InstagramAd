package appsforyou.tarun.com.instagramad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignupActivity";
    EditText namedittext, passwordeditText, emailEditText, confirmPasswordEditext;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView loginTextView;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        loginTextView = (TextView) findViewById(R.id.logintextview);
        signup = (Button) findViewById(R.id.button);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
        namedittext = (EditText) findViewById(R.id.editTextname);
        emailEditText = (EditText) findViewById(R.id.editTextemail);
        passwordeditText = (EditText) findViewById(R.id.editTextsignuppassword);
        confirmPasswordEditext = (EditText) findViewById(R.id.editTextconfirmpass);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordeditText.getText().toString();
                String confirmPassword = confirmPasswordEditext.getText().toString();
                String name = namedittext.getText().toString();
                String email = emailEditText.getText().toString();

                if (password.equals("") || email.equals("") || name.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(SignupActivity.this, "Enter all correct details", Toast.LENGTH_LONG).show();

                } else {
                    if (password.equals(confirmPassword)) {
                        signUp(email, password, name);

                    } else {
                        Toast.makeText(SignupActivity.this, "Password not matched", Toast.LENGTH_LONG).show();
                    }



                }

            }
        });

    }

    public void signUp(String emailsignup, String signuppassword, final String username) {
        mAuth.createUserWithEmailAndPassword(emailsignup, signuppassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignupActivity.this, "Sign Up success", Toast.LENGTH_LONG).show();
                            databaseReference.child("User_Detail").child(mAuth.getCurrentUser().getUid()).child("User_Name").setValue(username);
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
