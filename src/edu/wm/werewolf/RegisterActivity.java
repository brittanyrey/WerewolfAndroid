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
	private String isAdmin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_screen);

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
				//AsyncTaskRunner runner = new AsyncTaskRunner();
				//runner.execute();
				finish();
				Intent intent = new Intent (getApplicationContext(), HomeScreenActivity.class);
			    startActivity(intent);
				// saveUser(v);
			}
		});
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("username", username);
	}

	private class AsyncTaskRunner extends AsyncTask<String, String, String> {

		private String resp;

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			// try {
			// // Do your long operations here and return the result
			// int time = Integer.parseInt(params[0]);
			// // Sleeping for given time period
			// Thread.sleep(time);
			// resp = "Slept for " + time + " milliseconds";
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// resp = e.getMessage();
			// } catch (Exception e) {
			// e.printStackTrace();
			// resp = e.getMessage();
			// }

			// HttpClient client = new DefaultHttpClient();
			// HttpPost post = new
			// HttpPost("http://mighty-sea-1005.herokuapp.com/addUser/");

			// List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			// pairs.add(new BasicNameValuePair("id",
			// usernameText.getText().toString()));
			// pairs.add(new BasicNameValuePair("firstName",
			// firstNameText.getText().toString()));
			// pairs.add(new BasicNameValuePair("lastName",
			// lastNameText.getText().toString()));
			// pairs.add(new BasicNameValuePair("username",
			// usernameText.getText().toString()));
			// pairs.add(new BasicNameValuePair("hashedPassword",
			// passwordText.getText().toString()));
			// pairs.add(new BasicNameValuePair("imageURL",
			// "http://i44.tinypic.com/9u3j4o.jpg"));
			// pairs.add(new BasicNameValuePair("isAdmin", "true"));
			//
			// try {
			// HttpClient httpclient = createHttpClient();
			// HttpPost httppost = new
			// HttpPost("http://mighty-sea-1005.herokuapp.com/addUser/");
			// httppost.getParams().setBooleanParameter(
			// "http.protocol.expect-continue", false );
			// httppost.setEntity(new UrlEncodedFormEntity(pairs));
			// HttpResponse response = httpclient.execute(httppost);
			//
			// // post.setEntity(new UrlEncodedFormEntity(pairs));
			// // HttpResponse response = client.execute(post);
			// System.out.println(response.getStatusLine().toString());
			// } catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			// } catch (ClientProtocolException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// catch (Exception e)
			// {
			// System.out.println("last exception");
			// e.printStackTrace();
			// }

			username = usernameText.getText().toString();
			firstName = firstNameText.getText().toString();
			lastName = lastNameText.getText().toString();
			id = usernameText.getText().toString();
			hashedPassword = passwordText.getText().toString();
			imageURL = "http://i44.tinypic.com/9u3j4o.jpg";
			isAdmin = "true";

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
						+ URLEncoder.encode(isAdmin, "UTF-8");

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
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

		private HttpClient createHttpClient() {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params,
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);

			return new DefaultHttpClient(conMgr, params);
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
