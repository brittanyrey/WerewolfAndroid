package edu.wm.werewolf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlayerListActivity extends Activity {

	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_list_screen);

		setContentView(R.layout.player_list_screen);
		lv = (ListView) findViewById(R.id.listView1);
		// Instanciating an array list (you don't need to do this, you already
		// have yours)
		
		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		    	HttpResponse response = null;
				HttpEntity responseEntity = null;
		    	try {        
			        HttpClient client = new DefaultHttpClient();
			        HttpGet request = new HttpGet();
			        request.setURI(new URI("http://mighty-sea-1005.herokuapp.com/players/alive"));
			        request.addHeader(BasicScheme.authenticate(
			       		 new UsernamePasswordCredentials("brittany", "yes"),
			       		 "UTF-8", false));
			        response = client.execute(request);
			        responseEntity = response.getEntity();
			        System.out.println(response.toString());
			        
			    } catch (URISyntaxException e) {
			        e.printStackTrace();
			    } catch (ClientProtocolException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    } catch (IOException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    }  
		    }
		});

		thread.start(); 
		
		
		HttpEntity responseEntity = null;
		
		ArrayList<String> your_array_list = new ArrayList<String>();
		your_array_list.add("foo");
		your_array_list.add("bar");
		// This is the array adapter, it takes the context of the activity as a
		// first // parameter, the type of list view as a second parameter and
		// your array as a third parameter
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, your_array_list);
		lv.setAdapter(arrayAdapter);
	}

}
