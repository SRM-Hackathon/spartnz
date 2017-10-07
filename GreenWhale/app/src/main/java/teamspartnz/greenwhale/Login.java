package teamspartnz.greenwhale;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    LinearLayout layout;
    EditText username, password;
    Button login;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences("Creden", 0);
        editor = preferences.edit();
        progressDialog = new ProgressDialog(Login.this);
        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        layout = (LinearLayout) findViewById(R.id.layout);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = username.getText().toString();
                final String pass = password.getText().toString();
                Pattern p = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(user);
                if (user.contentEquals("") || pass.contentEquals("") || !m.find())
                    Snackbar.make(layout, "error in username or password", Snackbar.LENGTH_SHORT).show();
                else {
                    progressDialog.setMessage("Logging you in..");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    JsonObject ob = new JsonObject();
                    ob.addProperty("mail", user);
                    ob.addProperty("password", pass);
                    Ion.with(Login.this)
                            .load(getResources().getString(R.string.login))
                            .setJsonObjectBody(ob)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    progressDialog.cancel();
                                    if (result == null)
                                        Snackbar.make(layout, "Connection error!", Snackbar.LENGTH_SHORT).show();
                                    else {
                                        if (!result.get("error").getAsBoolean()) {
                                            try {
                                                editor.putString("user", user);
                                                editor.putBoolean("FIRST_LAUNCH", false);
                                                editor.apply();
                                                startActivity(new Intent(Login.this, MainActivity.class));
                                                finish();
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        } else
                                            Snackbar.make(layout, result.get("details").getAsString(), Snackbar.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
    }

}
