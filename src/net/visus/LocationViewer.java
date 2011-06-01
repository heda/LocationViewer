package net.visus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import net.visus.constants.Constants;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class LocationViewer extends MapActivity implements LocationListener {
	private LocationManager locationManager;
	private Button save_button;
	private ToggleButton toggleButton;
	MapView mapView;
	MapController mc;
	GeoPoint p;
	GeoPoint p_new;
	private Button alertButton;
	private static final String TAG = "TGLBTN";

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle menu item selection
		switch (item.getItemId()) {
		case R.id.menu_alert:
			// Seite für Benachritigungsoptionen aufrufen
			showEmailNsmsPage();
			return true;
		case R.id.menu_modus:
			// Seite zeigen fürs Umschalten Pfleger/gepflegter bzw.
			// PIN-Eingabe
			showModusChangePage();
			return true;
		case R.id.menu_radius:
			// Seite zeigen für Radius-Angabe
			showRadiusPage();
			return true;
		case R.id.menu_pin:
			// Seite für PIN eingabe öffnen
			showPINChangePage();
			return true;
		case R.id.informations:
			// Seite für Informationene vom Patienten öffnen
			showInformationsPage();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showInformationsPage() {
		if (Constants.pfleger) {
			Intent intent = new Intent();
			intent.setClass(this, InformationViewer.class);
			startActivity(intent);
		} else {
			showFalscherStatusToast();
		}

	}

	private void showEmailNsmsPage() {
		if (Constants.pfleger) {
			Intent intent = new Intent();
			intent.setClass(this, EmailNsmsViewer.class);
			startActivity(intent);
		} else {
			showFalscherStatusToast();
		}
	}

	private void showRadiusPage() {
		if (Constants.pfleger) {
			Intent intent = new Intent();
			intent.setClass(this, RadiusViewer.class);
			startActivity(intent);
		} else {
			showFalscherStatusToast();
		}

	}

	private void showFalscherStatusToast() {
		Context context = getApplicationContext();
		CharSequence text = "Falscher Status! Bitte ändern Sie erst Ihren Status auf Pfleger!";
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}

	private void showModusChangePage() {
		Intent intent = new Intent();
		intent.setClass(this, ModusChangeViewer.class);
		startActivity(intent);

	}

	private void showLockscreen() {
		// zeige den "Lockscreen" der App
		Intent intent = new Intent();
		intent.setClass(this, LockscreenViewer.class);
		startActivity(intent);
	}

	private void showPINChangePage() {
		if (Constants.pfleger) {
			Intent intent = new Intent();
			intent.setClass(this, ChangePinViewer.class);
			startActivity(intent);
		} else {
			showFalscherStatusToast();
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mapView = (MapView) findViewById(R.id.mapView);
		LinearLayout zoomLayout = (LinearLayout) findViewById(R.id.zoom);
		@SuppressWarnings("deprecation")
		View zoomView = mapView.getZoomControls();

		zoomLayout.addView(zoomView, new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		mapView.displayZoomControls(true);

		mapView.setStreetView(true);

		mc = mapView.getController();
		String coordinates[] = { "49.011809", "8.404198" };
		double lat = Double.parseDouble(coordinates[0]);
		double lng = Double.parseDouble(coordinates[1]);

		p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

		mc.animateTo(p);
		mc.setZoom(15);
		mapView.invalidate();

		MapOverlay mapOverlay = new MapOverlay();
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);

		mapView.invalidate();

		this.save_button = (Button) this.findViewById(R.id.Button01);
		this.alertButton = (Button) this.findViewById(R.id.lockBtn);
		this.toggleButton = (ToggleButton) this
				.findViewById(R.id.ToggleButton01);
		this.toggleButton.setOnClickListener(toggleButtonListener);
		this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		for (String provider : this.locationManager.getAllProviders()) {
			this.locationManager
					.requestLocationUpdates(provider, 1000, 0, this);
		}

		alertButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showLockscreen();

			}
		});

		this.save_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				p = new GeoPoint((int) p.getLatitudeE6(), (int) p
						.getLongitudeE6());

				mc.animateTo(p);
				mc.setZoom(25);
				mapView.invalidate();

				MapOverlay mapOverlay = new MapOverlay();
				List<Overlay> listOfOverlays = mapView.getOverlays();
				listOfOverlays.clear();
				listOfOverlays.add(mapOverlay);
				return;
			}
		});

	}

	private OnClickListener toggleButtonListener = new OnClickListener() {
		public void onClick(View v) {

			if (toggleButton.isChecked())
				Log.v(TAG, "KNIFE TOGGLE: checked state is true,"
						+ " showing red (up) position.");
			else
				Log.v(TAG, "KNIFE TOGGLE: checked state is false,"
						+ " showing blue (down) position.");
		}
	};

	class MapOverlay extends com.google.android.maps.Overlay {
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			Point screenPts = new Point();
			mapView.getProjection().toPixels(p, screenPts);

			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.pushpin);
			canvas.drawBitmap(bmp, screenPts.x, screenPts.y, null);
			return true;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			// ---when user lifts his finger---
			if (event.getAction() == 1) {
				p = mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());

				Geocoder geoCoder = new Geocoder(getBaseContext(),
						Locale.getDefault());
				try {
					List<Address> addresses = geoCoder.getFromLocation(
							p.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6,
							1);

					String add = "";
					if (addresses.size() > 0) {
						for (int i = 0; i < addresses.get(0)
								.getMaxAddressLineIndex(); i++)
							add += addresses.get(0).getAddressLine(i) + "\n";
					}

					Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT)
							.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			} else
				return false;
		}

	}

	@SuppressWarnings("static-access")
	public synchronized void onLocationChanged(Location location) {

		float[] distance = new float[2];
		location.distanceBetween((p.getLatitudeE6() / 1E6),
				(p.getLongitudeE6() / 1E6), location.getLatitude(),
				location.getLongitude(), distance);

		if (toggleButton.isChecked() && distance[0] > Constants.radiusInMeter) {
			sendMessages(location.getLatitude(), location.getLongitude(),
					location.getTime(), distance[0]);
		} else
			return;

	}

	public void sendMessages(double lat, double lon, double time, float distance) {

		// Wenn Pfleger am Handy naürlich keine Mail/SMS verschicken
		if (!Constants.pfleger) {

			// Check
			Boolean smssend = false;
			Boolean mailsend = false;

			// Falls Nummer angegeben und größer Null SMS senden
			if (Constants.sms != null && Constants.sms.length() > 0) {

				SmsManager smsmanager = SmsManager.getDefault();

				// SMS Text zusammenbauen
				String smstext = "Patient " + Constants.oldFolkPrename + " "
						+ Constants.oldFolkName + " flieht! Link: "
						+ Constants.actualPositionLink;

				// SMS muss kleiner 160 Zeichen sein, sonst Null Pointer
				if (smstext.length() < 160) {
					smsmanager.sendTextMessage(Constants.sms, null, smstext,
							null, null);
					smssend = true;
				} else {
					smssend = false;
				}

			}

			// Falls EMail angegeben
			if (Constants.email != null && Constants.email.length() > 0) {

				Intent email = new Intent(Intent.ACTION_SEND);

				// Email im HTML Format wg. Link
				email.setType("plain/text");

				// Email Subject
				String subject = "Fluchtversuch !! Patient: "
						+ Constants.oldFolkName + ", "
						+ Constants.oldFolkPrename;

				// Email Text
				String text = "Ihr Patient hat seinen Bereich verlassen. Folgender Link zeigt den derzeitigen Aufenthaltsort.\n"
						+ Constants.actualPositionLink;

				email.putExtra(Intent.EXTRA_EMAIL,
						new String[] { Constants.email });
				email.putExtra(Intent.EXTRA_SUBJECT, subject);
				email.putExtra(Intent.EXTRA_TEXT, text);

				// Email versenden
				try {
					startActivity(Intent.createChooser(email, "Send mail..."));
					mailsend = true;
				} catch (android.content.ActivityNotFoundException ex) {
					mailsend = false;
				}

			}

			// Alert, dass zugewiesener Bereich verlassen wurde
			if (smssend || mailsend) {

				AlertDialog alertDialog = new AlertDialog.Builder(
						LocationViewer.this).create();
				alertDialog.setTitle("Achtung!");
				alertDialog
						.setMessage("Sie verlassen Ihren zugewiesenen Bereich. Ihr Pfleger wurde informiert.");
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// close window
								finish();
							}
						});
				alertDialog.show();
			}

		}
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}