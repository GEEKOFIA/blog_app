package in.geekofia.blog.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import in.geekofia.blog.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, SPLASH_TIME_OUT);

//        EasySplashScreen config = new EasySplashScreen(this)
//                .withFullScreen()
//                .withTargetActivity(MainActivity.class)
//                .withSplashTimeOut(2000)
//                .withBackgroundColor(Color.parseColor("#1a1b29"))
//                .withAfterLogoText("geekofia blog")
//                .withLogo(R.mipmap.ic_launcher_round);
//
//        config.getAfterLogoTextView().setTextColor(Color.WHITE);
//        config.getAfterLogoTextView().setPadding(0, 8,0,0);
//        config.getAfterLogoTextView().setTextSize(32);
//
//        View easySplashScreen = config.create();
    }
}
