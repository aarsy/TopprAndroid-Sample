package arsy.com.topperandroid;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import arsy.com.topperandroid.common.CommonGlobalVariable;


public class SplashScreen extends Activity implements Animation.AnimationListener {


    private TextView tv;
    private Animation fadein, animOvershoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_splash_screen);
        animOvershoot = AnimationUtils.loadAnimation(this, R.anim.overshoot_anim);

        if (!CommonGlobalVariable.checkInternetConnection(this)) {
            alertDailog(this);
            return;
        }

        tv = (TextView) findViewById(R.id.splash_logo);

        fadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        // set animation listener

        fadein.setAnimationListener(this);
        animOvershoot.setAnimationListener(this);

        // button click event
        tv.startAnimation(animOvershoot);

    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animOvershoot) {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
    }


    @Override
    public void onAnimationRepeat(Animation animation) {

    }



    public void alertDailog(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("No Internet Connection");

        // set dialog message
        alertDialogBuilder
                .setMessage("Please check your internet Connection !")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        SplashScreen.this.finish();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

