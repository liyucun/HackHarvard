package com.hackharvard.smartmusicshuffle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent cameraIntent = new Intent(getApplicationContext(), AudioPlayer.class);
        startActivityForResult(cameraIntent, 0);



    }

//    @Override
//    public void onLoggedIn() {
//        Log.d("MainActivity", "User logged in");
//
//
//
//        StringBuilder sb = new StringBuilder();
//
//        int count = 1;
//
//
//        for(String s : SpottyFi.selectRandom(10, 1)) {
//
//            String regex = "spotify:track:(.*)";
//
//            Pattern pattern = Pattern.compile(regex);
//            Matcher matcher = pattern.matcher(s);
//
//            mPlayer.queue(mOperationCallback, s);
//
//            while (matcher.find()) {
//
//                if (count < 20) {
//                    sb.append(matcher.group(1)).append(",");
//
//                } else {
//                    sb.append(matcher.group(1));
//                }
//                System.out.println(s);
//
//            }
//
//            count++;
//
//        }
//
//        System.out.println(sb.toString());
//
//        mPlayer.skipToNext(mOperationCallback);
//
//
//
//    }
}