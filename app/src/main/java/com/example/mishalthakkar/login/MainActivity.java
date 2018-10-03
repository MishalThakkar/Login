package com.example.mishalthakkar.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText Uname ;
    EditText Password;
    Button Sign_in;
    TextView mTextView,Register;
    int count = 5;
    FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uname = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        Sign_in = findViewById(R.id.button);
        mTextView = findViewById(R.id.textView);
        Register = findViewById(R.id.textView3);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        if (user != null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }

        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Uname.getText().toString(),Password.getText().toString());
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Registration.class);
                startActivity(intent);
            }
        });
    }

    public void validate(String uname,String password)
    {
        mProgressDialog.setMessage("Please wait while we verify your details");
        mProgressDialog.show();
        mFirebaseAuth.signInWithEmailAndPassword(uname,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.dismiss();
                if (task.isSuccessful())
                {
                    //Toast.makeText(MainActivity.this,"Login Sucessful",Toast.LENGTH_SHORT);
                    //startActivity(new Intent(MainActivity.this,SecondActivity.class));
                    checkEmailVerification();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Login Failed",Toast.LENGTH_SHORT);
                    count--;
                    mTextView.setText("No of attempts remaining : " + count);
                    if (count == 0)
                    {
                        Sign_in.setEnabled(false);
                    }
                }
            }
        });
    }

    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Boolean flag = firebaseUser.isEmailVerified();

        if (flag)
        {
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }
        else
        {
            Toast.makeText(this,"Please Verify your Email address",Toast.LENGTH_SHORT).show();
            mFirebaseAuth.signOut();
        }
    }
}
