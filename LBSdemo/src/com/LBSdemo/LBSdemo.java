package com.LBSdemo;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapActivity;

//import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LBSdemo extends MapActivity {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button gpsButton = (Button) findViewById(R.id.gpsButton);
        gpsButton.setOnClickListener(new Button.OnClickListener(){
        	@Override
        	public void onClick(View v){
        		switch(v.getId()){
        		case R.id.gpsButton:
        			LoadCoords();
        		}
        	}
        }
        
        );
    }

    public void LoadCoords(){
    	LocationListener myLocationListener;
    	Double latPoint;
    	Double lngPoint;
    	TextView latText = (TextView) findViewById(R.id.latText);
    	TextView lngText = (TextView) findViewById(R.id.lngText);
    	
    	
    	myLocationListener = new LocationListener(){
    		public void onLocationChanged(Location location) { 
				// TODO Auto-generated method stub 		                  
				NoteDebug("Location Chanaged");
                 Log.i("MapView","onLocationChanged: lat="+location.getLatitude()); 
                 Log.i("MapView","onLocationChanged: lat="+location.getLongitude());          
            } 		
            @Override 
            public void onProviderDisabled(String provider) { 
                 // TODO Auto-generated method stub 
            	NoteDebug("1--Disabled Provider");
                 Log.i("MapView","onProviderDisabled: " +provider); 		                  
            } 		
            @Override 
            public void onProviderEnabled(String provider) { 
                 // TODO Auto-generated method stub 
            	NoteDebug("2--Disabled Provider");
                 Log.i("MapView","onProviderEnabled: " +provider); 		                  
            } 
            @Override 
            public void onStatusChanged(String provider, int status, 
                      Bundle extras) { 
                 // TODO Auto-generated method stub 		            	 
            	NoteDebug("3--Disabled Provider");
                 Log.i("MapView","onStatusChanged: " +provider + status); 		                  
            }		
    	};
    	
    	LocationManager myManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    	try{
    		myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
    	}
    	catch(IllegalArgumentException e1){
			NoteDebug("Error--1");
		}
		catch(SecurityException e2){
			NoteDebug("Error--2");
		}
		Location loc = myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		latPoint = loc.getLatitude();
		lngPoint = loc.getLongitude();
		
		latText.setText(latPoint.toString());
		lngText.setText(lngPoint.toString());			
		
		final MapView myMap = (MapView) findViewById(R.id.myMap);
    	final Button zoomIn = (Button) findViewById(R.id.buttonZoomIn);
    	final Button zoomOut = (Button) findViewById(R.id.buttonZoomOut);
		
		GeoPoint myLocation = new GeoPoint(latPoint.intValue()*1000000,lngPoint.intValue()*1000000);
		
		final MapController myMapController = myMap.getController();
		//myMapController.animateTo(g);
		//myMapController.setZoom(17);
		myMapController.setCenter(myLocation);
		myMapController.setZoom(10);
		
		
		zoomIn.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				ZoomIn(myMap, myMapController);
			}
		});
		zoomOut.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				ZoomOut(myMap, myMapController);
			}
		});
    }
    
    public void ZoomIn(MapView mv, MapController mc){
    	if(mv.getZoomLevel() < 21){
    		int zoomLevel = mv.getZoomLevel() + 1;
    		mc.setZoom(zoomLevel);
    	}
    }
    
    public void ZoomOut(MapView mv, MapController mc){
    	if(mv.getZoomLevel() > 2){
    		int zoomLevel = mv.getZoomLevel() - 1;
    		mc.setZoom(zoomLevel);
    	}
    }
    
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	private void NoteDebug(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}
}