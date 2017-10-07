package teamspartnz.greenwhale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
    Button signupx;
    EditText name, email, pass, pass2;
    LinearLayout layout;
    ProgressDialog progressDialog;
    TextInputLayout nx;
    boolean nerror = false;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        preferences = getSharedPreferences("Creden", 0);
        editor = preferences.edit();
        layout = (LinearLayout) findViewById(R.id.layout);
        name = (EditText) findViewById(R.id.name);
        nx = (TextInputLayout) findViewById(R.id.nx);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        pass2 = (EditText) findViewById(R.id.password2);
        signupx = (Button) findViewById(R.id.signup);
        progressDialog = new ProgressDialog(Signup.this);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    nx.setErrorEnabled(true);
                    nerror = true;
                    nx.setError("no space allowed!");
                } else {
                    nx.setErrorEnabled(false);
                    nerror = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signupx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nm, eml, p1, p2;
                nm = name.getText().toString();
                eml = email.getText().toString();
                p1 = pass.getText().toString();
                p2 = pass2.getText().toString();
                Pattern p = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(eml);
                if (nm.contentEquals("") ||
                        eml.contentEquals("") ||
                        p1.contentEquals("") ||
                        p2.contentEquals("") ||
                        (!m.find()) ||
                        nerror)
                    Snackbar.make(layout, "something wrong!!", Snackbar.LENGTH_SHORT).show();
                else {
                    progressDialog.setMessage("Signing you up..");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    JsonObject ob = new JsonObject();
                    ob.addProperty("name", nm);
                    ob.addProperty("email", eml);
                    ob.addProperty("password", p1);
                    ob.addProperty("password2", p2);
                    Ion.with(Signup.this)
                            .load(getResources().getString(R.string.Signup))
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
                                            editor.putBoolean("FIRST_LAUNCH", false);
                                            editor.apply();
                                            try {
                                                editor.putString("user", eml);
                                                editor.apply();
                                                startActivity(new Intent(Signup.this, MainActivity.class));
                                                finish();
                                            } catch (Exception x) {
                                                x.printStackTrace();
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
