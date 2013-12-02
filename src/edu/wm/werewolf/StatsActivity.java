package edu.wm.werewolf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StatsActivity extends Activity {
	
	private String username = "userbase";
	private String password = "password";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_screen);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    username = extras.getString("username");
		    password = extras.getString("password");
		}
		
		
	}

}
