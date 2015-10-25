package com.galacticos.jarvis;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.widget.TextView;

import com.example.voicedemo.R;


public class JarvisReply extends Activity implements
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
		Random g = new Random();
		int k = g.nextInt(6);
		if (out.contains("hello")) {
			switch (k) {
			case 1:
				out = "Hi!, I am still under construction....";
				break;
			case 2:
				out = "Welcome Back!, I am still under construction....";
				break;
			case 3:
				out = "Hey!, I am still under construction....";
				break;
			case 4:
				out = "Hi!!, I am still under construction....";
				break;
			case 5:
				out = "Welcome!, I am still under construction....";
				break;
			default:
				out = "Hello, I am still under construction....";
			}
		}
		if (out.contains("morning")) {
			out="A very good morning to you too....";
		}
		if (out.contains("name")) {
			out="Are you asking my name? Well, I am Jarvis. Nice to meet you!";
		}
		if (out.contains("time")) {
			out="The time is "+Calendar.getInstance().getTime();
		}
		if (out.contains("brightness")) {
			out="You may now set the brightness by moving the slider";
			Intent i=new Intent(this,Brightness.class);
			startActivity(i);
		}
		if (out.contains("sms")||out.contains("SMS")) {
			out="You may now send an sms";
			Intent i=new Intent(this,SMS.class);
			startActivity(i);
		}
		if (out.contains("down music")) {
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			out = "I have decreased music volume";
		}
		if (out.contains("increase music")) {
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			out = "I have increased music volume";
		}

		if (out.contains("down ring")) {
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.adjustStreamVolume(AudioManager.STREAM_RING,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			out = "I have decreased ring volume";
		}
		if (out.contains("increase ring")) {
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.adjustStreamVolume(AudioManager.STREAM_RING,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			out = "I have increased ring volume";
		}

		if (out.contains("down notify")) {
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			out = "I have decreased notification volume";
		}
		if (out.contains("increase notify")) {
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			out = "I have increased notification volume";
		}

		if (out.contains("silent") || out.contains("Thailand")) {
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			out = "I have muted the volume";
		}

		if (out.contains("ring") || out.contains("pinger")) {
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			int maxVolume = audioManager
					.getStreamMaxVolume(AudioManager.STREAM_RING);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume,
					AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
			out = "I set ringing mode";
		}

		if (out.contains("battery")) {
			out = "Displaying the current battery details";
			Intent i = new Intent(this, BatteryStat.class);
			startActivity(i);
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