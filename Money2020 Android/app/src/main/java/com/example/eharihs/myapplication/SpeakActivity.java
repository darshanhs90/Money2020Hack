package com.example.eharihs.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
                    HttpClient httpclient = new DefaultHttpClient();
                    try{
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .detectAll()
                                .penaltyLog()
                                .build();
                        StrictMode.setThreadPolicy(policy);
                        HttpResponse response = httpclient.execute(new HttpGet("http://localhost:1337/createPayment"));
                    } catch(IOException e){
                        //
                    }
                    /*StatusLine statusLine = response.getStatusLine();
                    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        response.getEntity().writeTo(out);
                        String responseString = out.toString();
                        out.close();
                        //..more logic
                    } else{
                        //Closes the connection.
                        response.getEntity().getContent().close();
                        throw new IOException(statusLine.getReasonPhrase());
                    }*/

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
