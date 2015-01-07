package com.kit.shade;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.kit.extend.shady.R;

/**
 * This class has the controls to set the color of the filter
 * 
 * @author Hathibelagal
 */
public class ShadyActivity extends Activity implements OnSeekBarChangeListener {

	SharedMemory shared;

	SeekBar alphaSeek;
	SeekBar redSeek;
	SeekBar greenSeek;
	SeekBar blueSeek;

	int alpha, red, green, blue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shady_activity);

		initialize();
	}

	private void initialize() {

		stopServiceIfActive();

		shared = new SharedMemory(this);
		alphaSeek = (SeekBar) findViewById(R.id.alphaControl);
		redSeek = (SeekBar) findViewById(R.id.redControl);
		greenSeek = (SeekBar) findViewById(R.id.greenControl);
		blueSeek = (SeekBar) findViewById(R.id.blueControl);

		alphaSeek.setOnSeekBarChangeListener(this);
		redSeek.setOnSeekBarChangeListener(this);
		greenSeek.setOnSeekBarChangeListener(this);
		blueSeek.setOnSeekBarChangeListener(this);

		alpha = shared.getAlpha();
		red = shared.getRed();
		green = shared.getGreen();
		blue = shared.getBlue();

		alphaSeek.setProgress(alpha);
		redSeek.setProgress(red);
		greenSeek.setProgress(green);
		blueSeek.setProgress(blue);

		updateColor();
	}

	private void stopServiceIfActive() {
		if (ShadyService.STATE == ShadyService.ACTIVE) {
			Intent i = new Intent(ShadyActivity.this, ShadyService.class);
			stopService(i);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar == alphaSeek) {
			alpha = seekBar.getProgress();
		}
		if (seekBar == redSeek) {
			red = seekBar.getProgress();
		}
		if (seekBar == greenSeek) {
			green = seekBar.getProgress();
		}
		if (seekBar == blueSeek) {
			blue = seekBar.getProgress();
		}
		updateColor();
	}

	private void updateColor() {
		int color = SharedMemory.getColor(alpha, red, green, blue);
		ColorDrawable cd = new ColorDrawable(color);
		getWindow().setBackgroundDrawable(cd);
	}

	@Override
	public void onStartTrackingTouch(SeekBar sb) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar sb) {
	}

	public void cancelClick(View v) {
		finish();
	}

	public void applyClick(View v) {
		shared.setAlpha(alpha);
		shared.setRed(red);
		shared.setGreen(green);
		shared.setBlue(blue);

		Intent i = new Intent(ShadyActivity.this, ShadyService.class);
		startService(i);
		// TODO 添加通知
		// makeNotification();
		finish();
	}

	// private void makeNotification() {
	// NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
	// // nb.setSmallIcon(R.drawable.blurred_imageview_image);
	// //
	// nb.setLargeIcon(BitmapUtils.drawable2Bitmap(getResources().getDrawable(
	// // R.drawable.blurred_imageview_image)));
	// nb.setContentTitle(getString(R.string.shady_title));
	// nb.setContentText(getString(R.string.shady_active));
	// nb.setAutoCancel(true);
	// Intent resultIntent = new Intent(this, ShadyActivity.class);
	// TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	// stackBuilder.addParentStack(ShadyActivity.class);
	// stackBuilder.addNextIntent(resultIntent);
	// PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
	// PendingIntent.FLAG_UPDATE_CURRENT);
	// nb.setContentIntent(resultPendingIntent);
	// NotificationManager mNotificationManager = (NotificationManager)
	// getSystemService(Context.NOTIFICATION_SERVICE);
	//
	// mNotificationManager.notify(R.string.shady_title, nb.build());
	// }
}
