package edu.wm.werewolf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HomeScreenActivity extends Activity {

	private Button logOutButton;
	private Button newGameButton;
	private Button statsButton;
	private Button playerListButton;

	private TextView usernameText;
	private TextView numDaysText;
	private TextView timeOfDayText;
	private TextView dayLengthText;
	
	private ImageView dayNightPhoto;
	
	private ProgressBar progressbar;

	private String username;
	private String password;
	private String numDays;
	private String dayLength;
	private boolean isAdmin = false;
	private boolean isNight;
	private int numAlivePlayers = 12;
	private int numWerewolves = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			username = extras.getString("username");
			password = extras.getString("password");
			isAdmin = extras.getBoolean("isAdmin");
		}

		usernameText = (TextView) findViewById(R.id.username);
		numDaysText = (TextView) findViewById(R.id.numDaysAlive);
		timeOfDayText = (TextView) findViewById(R.id.timeOfDay);
		dayNightPhoto = (ImageView) findViewById(R.id.dayNightCycle);
		logOutButton = (Button) findViewById(R.id.logout);
		newGameButton = (Button) findViewById(R.id.newGame);
		statsButton = (Button) findViewById(R.id.stats);
		playerListButton = (Button) findViewById(R.id.playerList);
		dayLengthText = (TextView) findViewById(R.id.dayLength);
		progressbar = (ProgressBar) findViewById(R.id.progressBar1);
		
		setPhotoAndStatus();
		
		timeOfDayText.setText(isNight? "Nighttime":"Daytime");
		usernameText.setText(username);
		numDaysText.setText(numDays);
		dayLengthText.setText(dayLength);
		progressbar.setMax(numAlivePlayers);
		progressbar.setProgress(numWerewolves);

		if (!isAdmin) { // if user is not admin they do not have access to
						// newGameButton
			newGameButton.setVisibility(8);
		}

		logOutButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("log out");
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				finish();
				startActivity(intent);
			}
		});
		newGameButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("new game");
				Intent intent = new Intent(getApplicationContext(),
						NewGameActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("isAdmin", isAdmin);
				startActivity(intent);
			}
		});
		statsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("stats");
				Intent intent = new Intent(getApplicationContext(),
						StatsActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("isAdmin", isAdmin);
				startActivity(intent);
			}
		});
		playerListButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("player list");
				Intent intent = new Intent(getApplicationContext(),
						PlayerListActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				intent.putExtra("isAdmin", isAdmin);
				startActivity(intent);
			}
		});
	}

	private void setPhotoAndStatus() {
		getStatus();
		if (isNight) {
			dayNightPhoto.setImageResource(R.drawable.nighttime);
		} else {
			dayNightPhoto.setImageResource(R.drawable.daytime);
		}
	}

	private void getStatus() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				
				   try {
			            DataLoader dl = new DataLoader();
			            String url = "http://mighty-sea-1005.herokuapp.com/getStatus";
			            HttpResponse response = dl.secureLoadData(url); 

			            HttpEntity responseEntity = response.getEntity();
						String content = EntityUtils.toString(responseEntity);
						List <String> responseInfo = Arrays.asList(content.split("\\s*,\\s*"));
						// error: alivePlayers == werewolves
						
						System.out.println(content);

						String numOfDaysString = responseInfo.get(0);
						String numOfWerewolves = responseInfo.get(1);
						String numAlive = responseInfo.get(2);
						String dayLengthString = responseInfo.get(3);
						String isNighttime = responseInfo.get(4);
						
						numDays = numOfDaysString.substring(11, numOfDaysString.length()-2);
						numWerewolves = Integer.parseInt(numOfWerewolves.substring(15,numOfWerewolves.length()-2));
						numAlivePlayers = Integer.parseInt(numAlive.substring(13, numAlive.length()-2));
						dayLength = String.valueOf(Integer.parseInt(dayLengthString.substring(16, dayLengthString.length()-2))/60000) 
								+ " minute(s)";
						isNight = Boolean.parseBoolean(isNighttime.substring(8, isNighttime.length()-1));
						System.out.println(numDays + " " + isNight + " " + numWerewolves + " " + numAlivePlayers + " " +
									dayLength);
			        } catch (Exception e) {
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
	}

	/**
	 * Taken from:
	 * http://janis.peisenieks.lv/en/76/english-making-an-ssl-connection
	 * -via-android/
	 * 
	 */
	public class CustomSSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public CustomSSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new CustomX509TrustManager();

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		public CustomSSLSocketFactory(SSLContext context)
				throws KeyManagementException, NoSuchAlgorithmException,
				KeyStoreException, UnrecoverableKeyException {
			super(null);
			sslContext = context;
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	public class CustomX509TrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	}

	public class DataLoader {

		public HttpResponse secureLoadData(String url)
				throws ClientProtocolException, IOException,
				NoSuchAlgorithmException, KeyManagementException,
				URISyntaxException, KeyStoreException,
				UnrecoverableKeyException {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { new CustomX509TrustManager() },
					new SecureRandom());

			HttpClient client = new DefaultHttpClient();

			SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = client.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			DefaultHttpClient sslClient = new DefaultHttpClient(ccm,
					client.getParams());

			HttpGet get = new HttpGet(new URI(url));
			get.addHeader(BasicScheme.authenticate(
					 new UsernamePasswordCredentials("brittany", "yes"),
					 "UTF-8", false));
			HttpResponse response = sslClient.execute(get);

			return response;
		}

	}
}