package edu.wm.werewolf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	private EditText usernameText;
	private EditText passwordText;
	private Button registerButton;
	private Button loginButton;

	private String username;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);

		usernameText = (EditText) findViewById(R.id.username);
		passwordText = (EditText) findViewById(R.id.password);

		registerButton = (Button) findViewById(R.id.createButton);
		loginButton = (Button) findViewById (R.id.loginButton);

		if (savedInstanceState == null) {
			username = "";
		} else {
			username = savedInstanceState.getString("username");
		}

		registerButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("register");
				Intent intent = new Intent (getApplicationContext(), RegisterActivity.class);
				finish();
			    startActivity(intent);
			}
		});
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("login");
				// TODO
				// login call post
				// validate 
				// either error msg or go to home screen

				Intent intent = new Intent (getApplicationContext(), HomeScreenActivity.class);
			    startActivity(intent);
			}
		});
	}

}
