package com.hackharvard.smartmusicshuffle.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.hackharvard.smartmusicshuffle.AudioPlayer;
import com.hackharvard.smartmusicshuffle.R;
import com.hackharvard.smartmusicshuffle.fragment.CameraFragment;
import com.hackharvard.smartmusicshuffle.fragment.RecommendationFragment;
import com.hackharvard.smartmusicshuffle.fragment.SmileFragment;
import com.microsoft.projectoxford.emotion.contract.Scores;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements SmileFragment.OnSmileFragmentInteractionListener, CameraFragment.OnCameraFragmentInteractionListener, RecommendationFragment.OnRecommendationFragmentInteractionListener, Player.NotificationCallback, ConnectionStateCallback {

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    public SpotifyPlayer mPlayer;

    public Scores scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_smile) {
                    SmileFragment smileFragment = SmileFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentContainer, smileFragment).commit();
                } else if (tabId == R.id.tab_camera) {
                    CameraFragment cameraFragment = CameraFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentContainer, cameraFragment).commit();
                } else if (tabId == R.id.tab_recommendation) {
                    RecommendationFragment recommendationFragment = RecommendationFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, recommendationFragment).commit();
                }
            }
        });

        setup();
    }

    private void setup() {
        if(mPlayer == null) {
            openLoginWindow();
        }

        RxPermissions.getInstance(this)
                .request(Manifest.permission.ACCESS_NETWORK_STATE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M

                    } else {
                        finish();
                    }
                });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private static final int REQUEST_CODE = 1337;
    @SuppressWarnings("SpellCheckingInspection")
    private static final String CLIENT_ID = "ca214c176e65473b9ed0975bad8820ff";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String REDIRECT_URI = "http://localhost:8888/callback";

    public final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Log.d(HomeActivity.class.getName(), "OK!");
        }

        @Override
        public void onError(Error error) {
            Log.d(HomeActivity.class.getName(), "ERROR:" + error);
        }
    };


    private void openLoginWindow() {
        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    onAuthenticationComplete(response);
                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.e(HomeActivity.class.getName(), "Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.e(HomeActivity.class.getName(), "Auth result: " + response.getType());
            }
        }
    }

    private void onAuthenticationComplete(AuthenticationResponse authResponse) {
        if (mPlayer == null) {
            Config playerConfig = new Config(getApplicationContext(), authResponse.getAccessToken(), CLIENT_ID);

            mPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(SpotifyPlayer player) {
                    mPlayer.setConnectivityStatus(mOperationCallback, getNetworkConnectivity(HomeActivity.this));
                    mPlayer.addNotificationCallback(HomeActivity.this);
                    mPlayer.addConnectionStateCallback(HomeActivity.this);
                }

                @Override
                public void onError(Throwable error) {
                    Log.e(HomeActivity.class.getName(), "Error in initialization: " + error.getMessage());
                }
            });
        } else {
            mPlayer.login(authResponse.getAccessToken());
        }
    }

    private Connectivity getNetworkConnectivity(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return Connectivity.fromNetworkType(activeNetwork.getType());
        } else {
            return Connectivity.OFFLINE;
        }
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

    }

    @Override
    public void onPlaybackError(Error error) {

    }

    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(int i) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

}
