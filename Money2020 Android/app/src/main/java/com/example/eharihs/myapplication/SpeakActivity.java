package com.example.eharihs.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;
import android.os.Handler;

/**
 * Created by eharihs on 10/24/2015.
 */
public class SpeakActivity extends Activity implements TextToSpeech.OnInitListener {
    private TextToSpeech mTts;
    String message;
    protected MainView mView = null;
    private static final int REQUEST_CODE_VERIFY_LOCK_PATTERN = 10002;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mView = new MainView(this);

        Intent i=getIntent();
        message=i.getType();
        mTts = new TextToSpeech(this, this);

    }

    private void speak(String textToSpeak){
        mTts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {

        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            speak(message);
            int result = mTts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Lanuage data is missing or the language is not supported.
                Log.e("404", "Language is not available.");
            }

            Intent intent = new Intent(this, VerifyLockPatternActivity.class);
            startActivityForResult(intent, REQUEST_CODE_VERIFY_LOCK_PATTERN);

        } else {
            // Initialization failed.
            Log.e("404", "Could not initialize TextToSpeech.");
            // May be its not installed so we prompt it to be installed
            Intent installIntent = new Intent();
            installIntent.setAction(
                    TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installIntent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_VERIFY_LOCK_PATTERN:
                if (resultCode == Activity.RESULT_OK) {
                    mView.updateView(MainView.STATUS_LOCK_PATTERN_VERFIED);
                    speak("Authenticated and Completed");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 5000);
                } else {
                    speak("Failed");
                    mView.updateView(MainView.STATUS_LOCK_PATTERN_VERFIED_FAILED);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
            mView = null;
        }
        super.onDestroy();
    }
}
