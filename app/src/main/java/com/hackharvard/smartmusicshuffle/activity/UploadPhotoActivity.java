package com.hackharvard.smartmusicshuffle.activity;

import android.app.Activity;
import android.os.Bundle;

import com.hackharvard.smartmusicshuffle.AndroidApplication;
import com.hackharvard.smartmusicshuffle.R;
import com.hackharvard.smartmusicshuffle.response.EmotionResponse;
import com.hackharvard.smartmusicshuffle.service.EmotionService;

import retrofit2.Call;

public class UploadPhotoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
    }
}
