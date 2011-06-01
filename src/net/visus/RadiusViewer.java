package net.visus;

import net.visus.constants.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class RadiusViewer extends Activity implements OnClickListener {

	Spinner s;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.radius_page);

		// button
		Button okButton = (Button) findViewById(R.id.radiusPage_okbtn);

		// spinner
		this.s = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this,
				R.array.radius, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);

		// aktueller Radius
		TextView aktuell = (TextView) findViewById(R.id.aktuell);
		aktuell.setText("Aktuell eingesteller Radius: "
				+ Constants.radiusInMeter + " meter");

		// button listener
		okButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		String res = s.getSelectedItem().toString();
		net.visus.constants.Constants.radiusInMeter = Integer.parseInt(res);
		finish();

	}
}
