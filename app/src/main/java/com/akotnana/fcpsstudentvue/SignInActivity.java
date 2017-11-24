package com.akotnana.fcpsstudentvue;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akotnana.fcpsstudentvue.utils.AccountManager;
import com.akotnana.fcpsstudentvue.utils.BackendUtils;
import com.akotnana.fcpsstudentvue.utils.DataStorage;
import com.akotnana.fcpsstudentvue.utils.VolleyCallback;
import com.akotnana.fcpsstudentvue.utils.gson.User;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;;

/**
 * Created by anees on 11/23/2017.
 */

public class SignInActivity extends AppCompatActivity {
    public String TAG = "SignInActivity";

    ImageView logo;
    EditText username;
    EditText password;
    Button signIn;
    CheckBox staySignedIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        logo = (ImageView) findViewById(R.id.logo_view);
        username = (EditText) findViewById(R.id.input_username);
        password = (EditText) findViewById(R.id.input_password);
        signIn = (Button) findViewById(R.id.login_button);
        staySignedIn = (CheckBox) findViewById(R.id.stay_signed_in);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    new AccountManager(getApplicationContext()).saveCredentials(username.getText().toString(), password.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Signing in...");
                    progressDialog.show();
                    mAuth.signInAnonymously()
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInAnonymously:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        new DataStorage(getApplicationContext()).storeData("stayLoggedIn", String.valueOf(staySignedIn.isChecked()));
                                        Log.d(TAG, String.valueOf(staySignedIn.isChecked()));
                                        BackendUtils.doPostRequest("/grades", new HashMap<String, String>() {{
                                        }}, new VolleyCallback() {
                                            @Override
                                            public void onSuccess(String result) {
                                                progressDialog.dismiss();
                                                Log.d(TAG, result);
                                                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                                                intent.putExtra("grades", result);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onError(VolleyError error) {
                                                progressDialog.dismiss();
                                            }
                                        }, getApplicationContext());
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInAnonymously:failure", task.getException());
                                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && new DataStorage(getApplicationContext()).getData("stayLoggedIn").equals("true")) {
            updateUser();
        }
    }

    public void updateUser() {
        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
        startActivity(intent);
        finish();
    }

}
