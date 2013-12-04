package edu.wm.werewolf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerListActivity extends Activity {

	private ListView lv;
	private List<String> playerList;
	private ArrayList<String> finalList;
	private TextView instructions;
	private boolean werewolf = true;
	boolean isNight;
	private int pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		setContentView(R.layout.player_list_screen);

		finalList = new ArrayList<String>();
		lv = (ListView) findViewById(R.id.listView1);
		instructions = (TextView) findViewById(R.id.instructions);
		
		if (isNight()) {
			if (werewolf) {
				// TODO IF NIGHT SET KILLABLE FOR WEREWOLFS
				instructions
						.setText("All players in red are close enough to be killed. Click a player below to execute a kill.");
			} else {
				instructions.setText("Below is a list of all players.");
			}
		} else {
			instructions.setText("To cast your vote choose a player below.");
		}

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

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
					new AlertDialog.Builder(getParent())
					.setTitle("Titel")
					.setMessage("Do you really want to whatever?")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

					    public void onClick(DialogInterface dialog, int whichButton) {
					        Toast.makeText(PlayerListActivity.this, "Yaay", Toast.LENGTH_SHORT).show();
					    }})
					 .setNegativeButton(android.R.string.no, null).show();
//					AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
//					builder.setIcon(android.R.drawable.ic_dialog_alert)
//							.setTitle("Confirmation")
//							.setMessage(
//									"Are you sure you want to vote for "
//											+ lv.getItemAtPosition(position)
//													.toString() + "?")
//							.setPositiveButton("Yes,Vote!",
//									new DialogInterface.OnClickListener() {
//										@Override
//										public void onClick(DialogInterface dialog, int which) {
//											voteForPlayer(lv.getItemAtPosition(pos).toString());finish();
//											Toast.makeText(
//													getApplicationContext(),
//													"Vote Cast for " + Integer.toString(pos) + " "
//															+ lv.getItemAtPosition(pos),
//													Toast.LENGTH_LONG).show();
//										}
//									})
//							.setNegativeButton("No", null).show();
				} else {
					if (werewolf) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
						builder.setIcon(android.R.drawable.ic_dialog_alert)
								.setTitle("Confirmation")
								.setMessage(
										"Are you sure you want to kill "
												+ lv.getItemAtPosition(position)
														.toString() + "?")
								.setPositiveButton("Yes, Kill!",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												killPlayer(lv.getItemAtPosition(pos).toString());
												finish();
												Toast.makeText(
														getApplicationContext(),
														"Killing player " + Integer.toString(pos)
																+ " " + lv.getItemAtPosition(pos),
														Toast.LENGTH_LONG).show();
											}
										})
								.setNegativeButton("No", null).show();
					}
				}
			}
		});
	}

	private void voteForPlayer(String user) {
		// TODO MAKE POST
	}

	private void killPlayer(String user) {
		// TODO MAKE POST
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
							"http://mighty-sea-1005.herokuapp.com/isNight"));
					request.addHeader(BasicScheme.authenticate(
							new UsernamePasswordCredentials("brittany", "yes"),
							"UTF-8", false));
					response = client.execute(request);
					responseEntity = response.getEntity();

					String content = EntityUtils.toString(responseEntity);

					isNight = Boolean.parseBoolean(content);
					System.out.println(isNight);
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
}
