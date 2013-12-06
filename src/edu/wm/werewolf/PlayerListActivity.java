package edu.wm.werewolf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.PorterDuff; 

public class PlayerListActivity extends Activity {

	private ListView lv;
	private TextView instructions;
	private Button killButton;
	
	private List<String> playerList;
	private ArrayList<String> finalList;
	private boolean werewolf = true;
	boolean isNight;
	private int pos;
	
	private String username;
	private String password;
	private boolean isAdmin = false;
	private boolean isWerewolf = true;
	private String playerToActOn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		setContentView(R.layout.player_list_screen);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			username = extras.getString("username");
			password = extras.getString("password");
			isAdmin = extras.getBoolean("isAdmin");
			isWerewolf = extras.getBoolean("isWerewolf");
		}
		
		finalList = new ArrayList<String>();
		lv = (ListView) findViewById(R.id.listView1);
		instructions = (TextView) findViewById(R.id.instructions);
		killButton = (Button) findViewById(R.id.killButton);
		
		killButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
		
		killButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("kill buton");
				Intent intent = new Intent(getApplicationContext(),
						KillListActivity.class);
				startActivity(intent);
			}
		});
		
		if (isNight()) {
				instructions.setText("Below is a list of all players.");
				if (isWerewolf){
					killButton.setVisibility(0);//visible
					System.out.println("button visible");
				}
				else{
					killButton.setVisibility(8);//invisible
				}
		} else {
			instructions.setText("To cast your vote choose a player below.");
			killButton.setVisibility(8);//invisible
		}

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				loadLV();
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, finalList);
		lv.setAdapter(arrayAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				pos = position;
				if (!isNight()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(PlayerListActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle("Confirmation")
							.setMessage(
									"Are you sure you want to vote for "
											+ lv.getItemAtPosition(position)
													.toString() + "?")
							.setPositiveButton("Yes, Vote!",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											voteForPlayer(lv.getItemAtPosition(pos).toString());
											Toast.makeText(
													getApplicationContext(),
													"Vote Cast for " + Integer.toString(pos) + " "
															+ lv.getItemAtPosition(pos),
													Toast.LENGTH_LONG).show();
										}
									})
							.setNegativeButton("No", null).show();
				} 
				}
			
		});
	}

	private void voteForPlayer(String user) {
		playerToActOn = user;
		AsyncVoteRunner runner = new AsyncVoteRunner();
		runner.execute();
	}

	private boolean isNight() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				HttpResponse response = null;
				HttpEntity responseEntity = null;
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet();
					request.setURI(new URI(
							"http://mighty-sea-1005.herokuapp.com/getStatus"));
					request.addHeader(BasicScheme.authenticate(
							new UsernamePasswordCredentials("brittany", "yes"),
							"UTF-8", false));
					response = client.execute(request);
					responseEntity = response.getEntity();

					String content = EntityUtils.toString(responseEntity);
					List <String> responseInfo = Arrays.asList(content.split("\\s*,\\s*"));

					String isNighttime = responseInfo.get(4);
					isNight = Boolean.parseBoolean(isNighttime.substring(8, isNighttime.length()-1));
					
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return isNight;
	}
	
	private void loadLV (){
		HttpResponse response = null;
		HttpEntity responseEntity = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(
					"http://mighty-sea-1005.herokuapp.com/players/alive"));
			request.addHeader(BasicScheme.authenticate(
					new UsernamePasswordCredentials("brittany", "yes"),
					"UTF-8", false));
			response = client.execute(request);
			responseEntity = response.getEntity();

			String content = EntityUtils.toString(responseEntity);

			playerList = Arrays.asList(content.split("\\s*,\\s*"));

			for (int x = 0; x < playerList.size(); x++) {
				if (playerList.get(x).startsWith("\"userId\":")) {
					System.out.println(playerList.get(x));
					finalList.add(playerList.get(x).substring(10,
							playerList.get(x).length() - 1));
				} else {
					System.out.println("not " + playerList.get(x));
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class AsyncVoteRunner extends AsyncTask<String, String, String> {	

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()

			HttpResponse response = null;
			String responseSTR = null;
			
			try {
				HttpClient httpClient = new DefaultHttpClient();
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				HttpPost postMethod = new HttpPost("http://mighty-sea-1005.herokuapp.com/players/vote");   
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("voterID", username));
				nameValuePairs.add(new BasicNameValuePair("suspectID", playerToActOn));
				postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
						"brittany", "yes");
				BasicScheme scheme = new BasicScheme();
				Header authorizationHeader;
					authorizationHeader = scheme.authenticate(credentials, postMethod);
					postMethod.addHeader(authorizationHeader);
				response = httpClient.execute(postMethod);

				responseSTR = response.getEntity().toString();

			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (AuthenticationException e) {
				e.printStackTrace();
			}
			System.out.println("response :" + responseSTR);
			return responseSTR;
		}

		@Override
		protected void onPostExecute(String result) {
		}

		@Override
		protected void onPreExecute() {
		}
		
		@Override
		protected void onProgressUpdate(String... text) {
		}
	}

	
}
