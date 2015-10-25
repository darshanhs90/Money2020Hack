package com.example.eharihs.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class VoiceRead extends Activity implements
		TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

	private static final int REQUEST_CODE = 1234;
    private String out;
	private TextView word;
	private TextToSpeech tts;
	/**
	 * Called with the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_recog);
		tts = new TextToSpeech(this, this);
		Button speakButton = (Button) findViewById(R.id.speakButton);

		word = (TextView) findViewById(R.id.one);

        String savedData = LockPatternUtils.loadFromPreferences(this);
        if (savedData == null) {
            Intent intent = new Intent(this, SetLockPatternActivity.class);
            startActivity(intent);
        }

		// Disable button if no recognition service is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			speakButton.setEnabled(false);
			speakButton.setText("Recognizer not present");
		}
        out = "Hi!, Welcome to V Z PAY!";
        speakOut();
	}

	/**
	 * Handle the action of the button being clicked
	 */
	public void speakButtonClicked(View v) {
		startVoiceRecognitionActivity();
	}

	/**
	 * Fire an intent to start the voice recognition activity.
	 */
	private void startVoiceRecognitionActivity() {
        out="Say 1 to initiate new transaction Say 2 for Previous Transactions";
        speakOut();
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition");
		startActivityForResult(intent, REQUEST_CODE);
	}

	/**
	 * Handle the results from the voice recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			// Populate the wordsList with the String values the recognition
			// engine thought it heard

			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			
			
			String jout = matches.get(0);
			word.setText(jout);
			Intent i = new Intent(this, VoiceReply.class);
			i.putExtra("key", jout);
			startActivity(i);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {
			tts.setOnUtteranceCompletedListener(this);
			int result = tts.setLanguage(new Locale("en","US"));

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			} else {
				speakOut();
			}

		} else {
			Log.e("TTS", "Initilization Failed");
		}

	}

	private void speakOut() {

		HashMap<String, String> myHashAlarm = new HashMap<String, String>();
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
				String.valueOf(AudioManager.STREAM_ALARM));
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
				"SOME MESSAGE");
		tts.speak(out, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
	}

	public void onUtteranceCompleted(String utteranceId) {
		Log.i("", utteranceId);
		//finish();
	}
}