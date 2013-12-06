package edu.wm.werewolf;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
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

import edu.wm.werewolf.HomeScreenActivity.CustomSSLSocketFactory;
import edu.wm.werewolf.HomeScreenActivity.CustomX509TrustManager;
import edu.wm.werewolf.HomeScreenActivity.DataLoader;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StatsActivity extends Activity {
	
	private String username = "userbase";
	private String password = "password";
	private boolean isAdmin = false;
	
	private int numKills = 5;
	private int score = 5;
	private boolean isWerewolf = false;
	private boolean isDead = false;
	private String image = "5";
	
	private TextView usernameText;
	private TextView playerTypeText;
	private TextView playerStatusText;
	private TextView scoreText;
	private TextView numKillsText;
	private ImageView img;
	
	private boolean success = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_screen);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    username = extras.getString("username");
		    password = extras.getString("password");
		    isAdmin = extras.getBoolean("isAdmin");		    
		}
		
		usernameText = (TextView) findViewById(R.id.username);
		playerTypeText = (TextView) findViewById(R.id.playerType);
		playerStatusText = (TextView) findViewById(R.id.playerStatus);
		scoreText = (TextView) findViewById(R.id.score);
		numKillsText = (TextView) findViewById(R.id.numberOfKills);
		img = (ImageView) findViewById(R.id.imageView1);
		
		if (getStats()){
			setData();
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(StatsActivity.this);
			builder.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Error")
					.setMessage(
							"Because you are not currently in a game, stats could not be collected.")
							
					.setNeutralButton("Close",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
								finish();
						}
					})
					//.setNegativeButton("No", null).
					.show();
			// finish();
		}
	}
	
	private void setData(){
		usernameText.setText(username);
		playerTypeText.setText(isWerewolf ? " Werewolf" : " Townsperson");
		playerStatusText.setText(isDead ? " Dead" : " Alive");
		scoreText.setText(" "+score);
		numKillsText.setText(" "+numKills);
		img.setImageResource(image.startsWith("http") ? R.drawable.cube :  Integer.parseInt(image));
	}
	
	private boolean getStats() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				
				   try {
			            DataLoader dl = new DataLoader();
			            String url = "http://mighty-sea-1005.herokuapp.com/players/stats?"+
			            		URLEncoder.encode("user", "UTF-8") + "="
								+ URLEncoder.encode(username, "UTF-8");
			            HttpResponse response = dl.secureLoadData(url); 

			            HttpEntity responseEntity = response.getEntity();
						String content = EntityUtils.toString(responseEntity);
						List <String> responseInfo = Arrays.asList(content.split("\\s*,\\s*"));
						
						System.out.println(content);

						String scoreStr = responseInfo.get(0);
						String numOfKills = responseInfo.get(1);
						String imageS = responseInfo.get(2);
						String werewolf = responseInfo.get(3);
						String dead = responseInfo.get(4);
						
						score = Integer.parseInt(scoreStr.substring(9));
						isWerewolf = Boolean.parseBoolean(werewolf.substring(11));
						isDead = Boolean.parseBoolean(dead.substring(7, dead.length()-1));
						numKills = Integer.parseInt(numOfKills.substring(11));
						image = imageS.substring(9,imageS.length()-1);
						System.out.println(score + " " + isWerewolf + " " + isDead + " " + numKills + " " +
									image);
						success = true;
			        } catch (Exception e) {
			            e.printStackTrace();
			            success = false;
			        }
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return success;
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


}
