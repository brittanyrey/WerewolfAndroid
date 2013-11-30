package edu.wm.werewolf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreenActivity extends Activity {
	

	private Button logOutButton;
	private Button newGameButton;
	private Button statsButton;
	private Button playerListButton;
	
	private TextView usernameText;
	private TextView numDaysText;
	private TextView timeOfDayText;
	
	private ImageView dayNightPhoto;

	private String username = "userbase";
	private String numDays = "numbase";
	private String timeOfDays = "timebase";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		
		setContentView(R.layout.home_screen);
		
		usernameText = (TextView) findViewById(R.id.username);
		numDaysText = (TextView) findViewById(R.id.numDaysAlive);
		timeOfDayText = (TextView) findViewById(R.id.timeOfDay);
		dayNightPhoto = (ImageView) findViewById(R.id.dayNightCycle);
		logOutButton = (Button) findViewById(R.id.logout);
		newGameButton = (Button) findViewById(R.id.newGame); //TODO make invisible for non admins
		statsButton = (Button) findViewById(R.id.stats);
		playerListButton = (Button) findViewById(R.id.playerList);
		
		//TODO make a call to update time of day & numDaysAlive
		usernameText.setText(username);
		numDaysText.setText(numDays);
		timeOfDayText.setText(timeOfDays);
		
		setPhoto();
		
		logOutButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("log out");
				Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
				finish();
			    startActivity(intent);
			}
		});
		newGameButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("new game");

				if (true)//TODO admin
				{
//					Intent intent = new Intent (getApplicationContext(), Activity.class);
//				    startActivity(intent);
				}
				
			}
		});
		statsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("stats");
				Intent intent = new Intent (getApplicationContext(), StatsActivity.class);
			    startActivity(intent);
			}
		});
		playerListButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("player list");
				Intent intent = new Intent (getApplicationContext(), PlayerListActivity.class);
			    startActivity(intent);
			}
		});
	}
	
	public void setPhoto(){
		if (true){//TODO if is night
			dayNightPhoto.setImageResource(R.drawable.nighttime);
		}
		else{
			dayNightPhoto.setImageResource(R.drawable.daytime);
		}
	}

}