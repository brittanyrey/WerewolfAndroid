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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PlayerListActivity extends Activity {

	private ListView lv;
	private List <String> playerList;
	private ArrayList<String> finalList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_list_screen);


		finalList = new ArrayList<String>();
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

		        	String content = EntityUtils.toString(responseEntity);
		        	
		        	playerList = Arrays.asList(content.split("\\s*,\\s*"));
		        	
		    		for (int x = 0; x < playerList.size(); x ++){
		    			if (playerList.get(x).startsWith("\"userId\":")){
		    					System.out.println(playerList.get(x));
		    					finalList.add(playerList.get(x).substring(10, playerList.get(x).length()-1));
		    			}
		    			else{
		    				System.out.println("not " + playerList.get(x));
		    			}
		    		}
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
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		arrayAdapter();
		
		lv.setOnItemClickListener(        
	            new OnItemClickListener()
	            {
	                @Override
	                public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {
	                    // TODO Auto-generated method stub
	                    Toast.makeText(getApplicationContext(),
	                    		"You Selected Item "+Integer.toString(position) +" "+ lv.getItemAtPosition(position), 
	                    		Toast.LENGTH_LONG).show();          
	                }       
	            }
	    );
		
	}
		
	public void arrayAdapter (){

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, finalList);
		lv.setAdapter(arrayAdapter);
        
	}

}
