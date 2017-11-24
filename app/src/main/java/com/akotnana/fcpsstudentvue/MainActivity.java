package com.akotnana.fcpsstudentvue;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.akotnana.fcpsstudentvue.utils.AccountManager;
import com.akotnana.fcpsstudentvue.utils.BackendUtils;
import com.akotnana.fcpsstudentvue.utils.DataStorage;
import com.akotnana.fcpsstudentvue.utils.VolleyCallback;

import java.util.HashMap;

import com.akotnana.fcpsstudentvue.utils.gson.Quarter;
import com.akotnana.fcpsstudentvue.utils.gson.User;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static android.R.attr.bitmap;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainActivity";

    TextView textView;
    EditText username;
    EditText password;

    ImageView imageView;

    Button login;
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.sample_output);
        username = (EditText) findViewById(R.id.username_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);

        imageView = (ImageView) findViewById(R.id.image_view);

        login = (Button) findViewById(R.id.login_button);
        clear = (Button) findViewById(R.id.clear_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = MainActivity.this.getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if(!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    new AccountManager(getApplicationContext()).saveCredentials(username.getText().toString(), password.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    BackendUtils.doPostRequest("/user", new HashMap<String, String>() {{
                    }}, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            progressDialog.dismiss();
                            //Log.d(TAG, result);
                            Gson gson = null;
                            try {
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gson = gsonBuilder.create();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            User user = gson.fromJson(result, User.class);
                            textView.setText(user.toString());
                            imageView.setImageDrawable(new BitmapDrawable(getResources(), bitmapSizeByScale(user.getPhoto(), 7)));
                            imageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(VolleyError error) {
                            progressDialog.dismiss();
                        }
                    }, getApplicationContext());
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("No Data");
                username.setText("");
                password.setText("");
                username.requestFocus();
                password.clearFocus();
                imageView.setVisibility(View.GONE);
            }
        });

    }

    public Bitmap bitmapSizeByScale(Bitmap bitmapIn, float scall_zero_to_one_f) {

        Bitmap bitmapOut = Bitmap.createScaledBitmap(bitmapIn,
                Math.round(bitmapIn.getWidth() * scall_zero_to_one_f),
                Math.round(bitmapIn.getHeight() * scall_zero_to_one_f), false);

        return bitmapOut;
    }

}
