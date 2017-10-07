package teamspartnz.greenwhale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import teamspartnz.greenwhale.fragment.FragClaimed;
import teamspartnz.greenwhale.fragment.FragTagCare;
import teamspartnz.greenwhale.fragment.Rewards;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            JsonObject ob = new JsonObject();
            ob.addProperty("email", getSharedPreferences("Creden", 0).getString("user","error"));
            Ion.with(MainActivity.this)
                    .load(getResources().getString(R.string.karma))
                    .setJsonObjectBody(ob)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (result != null)
                                ((TextView)findViewById(R.id.karma)).setText(result.get("karma").toString());
                        }
                    });

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragTagCare()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragClaimed()).commit();
                    return true;
                case R.id.rewards:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new Rewards()).commit();

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        Toolbar tb = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);
*/
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragTagCare()).commit();

        JsonObject ob = new JsonObject();
        ob.addProperty("email", getSharedPreferences("Creden", 0).getString("user","error"));
        Ion.with(MainActivity.this)
                .load(getResources().getString(R.string.karma))
                .setJsonObjectBody(ob)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null)
                            ((TextView)findViewById(R.id.karma)).setText(result.get("karma").toString());
                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
