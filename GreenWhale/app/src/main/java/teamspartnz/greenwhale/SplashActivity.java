package teamspartnz.greenwhale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences preferences = getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = preferences.edit();
        if (preferences.getBoolean("FIRST_LAUNCH", true)) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(new Intent(SplashActivity.this, FirstScreen.class));
                        finish();
                    }
                }
            };
            t.start();
        } else {
            Thread tx = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(new Intent(SplashActivity.this, Login.class));
                        finish();
                    }
                }
            };
            tx.start();
        }
    }
}
