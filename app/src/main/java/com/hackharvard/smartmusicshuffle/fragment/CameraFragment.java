package com.hackharvard.smartmusicshuffle.fragment;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hackharvard.smartmusicshuffle.R;
import com.hackharvard.smartmusicshuffle.helper.ImageHelper;
import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.rest.EmotionServiceException;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gun0912.tedbottompicker.TedBottomPicker;

public class CameraFragment extends Fragment {

    private OnCameraFragmentInteractionListener mListener;

    @BindView(R.id.photo)
    ImageView photo;

    TedBottomPicker tedBottomPicker;

    private EmotionServiceClient client;

    private Bitmap mBitmap;

    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        ButterKnife.bind(this, view);

        setup();

        return view;
    }

    private void setup() {

        RxPermissions.getInstance(getActivity())
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M

                    } else {
                        getActivity().finish();
                    }
                });

        tedBottomPicker = new TedBottomPicker.Builder(getActivity())
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        Picasso.with(getActivity()).load(uri).into(photo);
                        request(uri);
                    }
                })
                .create();

        if (client == null) {
            client = new EmotionServiceRestClient("2676248dabb34c87b114c06c06e77d8a");
        }

    }

    private void request(Uri uri) {
        mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                uri, getActivity().getContentResolver());
        new doRequest().execute();
    }

    @OnClick(R.id.take_photo)
    public void showPhotoPicker() {
        tedBottomPicker.show(getActivity().getSupportFragmentManager());
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCameraFragmentInteractionListener) {
            mListener = (OnCameraFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCameraFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private List<RecognizeResult> processWithAutoFaceDetection() throws EmotionServiceException, IOException {
        Log.d("emotion", "Start emotion detection with auto-face detection");

        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        List<RecognizeResult> result = null;

        result = this.client.recognizeImage(inputStream);

        String json = gson.toJson(result);
        Log.d("result", json);
        return result;
    }

    private class doRequest extends AsyncTask<String, String, List<RecognizeResult>> {
        // Store error message
        private Exception e = null;

        @Override
        protected List<RecognizeResult> doInBackground(String... args) {
            try {
                return processWithAutoFaceDetection();
            } catch (Exception e) {
                this.e = e;    // Store error
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<RecognizeResult> result) {
            super.onPostExecute(result);

            if (e != null) {
                this.e = null;
            } else {
                if (result.size() == 0) {
                } else {
                    for (RecognizeResult recognizeResult : result) {
                        Log.d("Recognizition result", recognizeResult.scores.toString());
                    }
                }
            }
        }
    }
}
