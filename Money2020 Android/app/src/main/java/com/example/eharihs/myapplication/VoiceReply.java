package com.example.eharihs.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;


public class VoiceReply extends Activity implements
		TextToSpeech.OnInitListener, OnUtteranceCompletedListener {
	/** Called when the activity is first created. */

	private TextToSpeech tts;
	private String word, out;
	private TextView output;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);

		tts = new TextToSpeech(this, this);

		output = (TextView) findViewById(R.id.output);
		word = getIntent().getExtras().getString("key");
		
		out = word;
        Log.d("asd",out);

		if(out.contains("1") || out.contains("one")){
            Intent serviceIntent = new Intent(this,MainActivityNew.class);
            //serviceIntent.setAction(".SmsReceiver");
            startService(serviceIntent);
            out = "Starting Transaction";
		}
        else if(out.contains("2") || out.contains("two")){
//            Intent serviceIntent = new Intent(this,SmsReceiver.class);
//            //serviceIntent.setAction(".SmsReceiver");
//            startService(serviceIntent);
            out = "Fetching Previous Balance";
        } else {
            out="Retry again";
        }
		output.setText(out);
		speakOut();
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
			int result = tts.setLanguage(new Locale("en","IN"));

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
		finish();
	}

}