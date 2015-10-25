package com.galacticos.jarvis;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.voicedemo.R;

public class Brightness extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brightness);

		SeekBar BackLightControl = (SeekBar) findViewById(R.id.backlightcontrol);
		final TextView BackLightSetting = (TextView) findViewById(R.id.backlightsetting);

		BackLightControl
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar arg0, int arg1,
							boolean arg2) {
						// TODO Auto-generated method stub
						int BackLightValue = arg1;
						BackLightSetting.setText(String.valueOf(BackLightValue));

						android.provider.Settings.System
								.putInt(getContentResolver(),
										android.provider.Settings.System.SCREEN_BRIGHTNESS,
										BackLightValue);
					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStopTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub

					}
				});
	}
}