package edu.wm.werewolf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {

	private EditText usernameText;
	private EditText passwordText;
	private EditText verifyPasswordText;
	private EditText firstNameText;
	private EditText lastNameText;
	private Button registerButton;

	private String username;
	private String password;
	private String hashedPassword;
	private String firstName;
	private String lastName;
	private String id;
	private String imageURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_screen);
		
		//TODO MAKE IMG DO SOMEHING.... 

		usernameText = (EditText) findViewById(R.id.username);
		passwordText = (EditText) findViewById(R.id.password);
		verifyPasswordText = (EditText) findViewById(R.id.verifyPassword);
		firstNameText = (EditText) findViewById(R.id.firstName);
		lastNameText = (EditText) findViewById(R.id.lastName);

		registerButton = (Button) findViewById(R.id.createButton);

		if (savedInstanceState == null) {
			username = "";
		} else {
			username = savedInstanceState.getString("username");
		}
		
		registerButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("set up");
				AsyncTaskRunner runner = new AsyncTaskRunner();
				runner.execute();
				//saveUser(v);
				Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
				finish();
			    startActivity(intent);
				
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
			finish();
		    startActivity(intent);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}


	private void saveUser(View v) {
		// this is for error handling
		/*
		 * if ( usernameText.getText().toString() == null ||
		 * passwordText.getText().toString() == "" ||
		 * verifyPasswordText.getText().toString() == "" ||
		 * firstNameText.getText().toString() == "" ||
		 * lastNameText.getText().toString() == "") {
		 * System.out.println("a field is blank");
		 * usernameText.setError("Your error message"); } else if
		 * (passwordText.getText() != verifyPasswordText.getText()) {
		 * System.out.println("passwords do not match ");
		 * System.out.println(lastNameText.getText().toString() == "");
		 * System.out.println(lastNameText.getText().toString() == null);
		 * System.out.println(lastNameText.getText() == null);
		 * 
		 * verifyPasswordText.setError("Passwords do not match"); } else{ }
		 */
	}

	private class AsyncTaskRunner extends AsyncTask<String, String, String> {

		private String resp;

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()

			username = usernameText.getText().toString();
			firstName = firstNameText.getText().toString();
			lastName = lastNameText.getText().toString();
			id = usernameText.getText().toString();
			try {
				hashedPassword = Hasher.md5(passwordText.getText().toString());
				System.out.println(hashedPassword);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			imageURL = "http://i44.tinypic.com/9u3j4o.jpg"; //TODO FIX THIS

			String data = "";
			try {
				data = URLEncoder.encode("username", "UTF-8") + "="
						+ URLEncoder.encode(username, "UTF-8");
				data += "&" + URLEncoder.encode("firstName", "UTF-8") + "="
						+ URLEncoder.encode(firstName, "UTF-8");

				data += "&" + URLEncoder.encode("lastName", "UTF-8") + "="
						+ URLEncoder.encode(lastName, "UTF-8");

				data += "&" + URLEncoder.encode("id", "UTF-8") + "="
						+ URLEncoder.encode(id, "UTF-8");
				
				data += "&" + URLEncoder.encode("hashedPassword", "UTF-8") + "="
						+ URLEncoder.encode(hashedPassword, "UTF-8");

				data += "&" + URLEncoder.encode("imageURL", "UTF-8") + "="
						+ URLEncoder.encode(imageURL, "UTF-8");

				data += "&" + URLEncoder.encode("isAdmin", "UTF-8") + "="
						+ URLEncoder.encode("false", "UTF-8");
				
				System.out.println(data);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			
			String text = "";
			BufferedReader reader = null;

			try {

				URL url = new URL(
						"http://mighty-sea-1005.herokuapp.com/addUser/");

				// Send POST data request

				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				wr.write(data);
				wr.flush();

				// Get the server response

				reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;

				// Read Server Response
				while ((line = reader.readLine()) != null) {
					// Append server response in string
					sb.append(line + "\n");
				}

				text = sb.toString();
			} catch (Exception ex) {

			} finally {
				try {

					reader.close();
				}

				catch (Exception ex) {
				}
			}

			// Show response on activity
			System.out.println(text);

			return resp;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			// finalResult.setText(result);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(String... text) {
			// finalResult.setText(text[0]);
			// Things to be done while execution of long running operation is in
			// progress. For example updating ProgessDialog
		}
	}

}
