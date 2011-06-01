package net.visus;

import net.visus.constants.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

public class ModusChangeViewer extends Activity implements OnClickListener {
	ToggleButton toggle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moduschange_page);

		// buttons
		toggle = (ToggleButton) findViewById(R.id.ModusPage_toggleBtn);
		Button okBtn = (Button) findViewById(R.id.modusPage_okbtn);
		// button listeners
		okBtn.setOnClickListener(this);
		// set the toggle button on the right side
		toggle.setChecked(Constants.pfleger);

	}

	public void onClick(View v) {

		if (toggle.getText().toString().equals("Pfleger")) {
			// If Chosen:Change the modus to Pfleger (if PIN is false, it will
			// go back to Überwachter)
			Constants.pfleger = true;

			// Show the PIN-Eingabe View
			Intent intent = new Intent();
			intent.setClass(this, PINViewer.class);
			startActivity(intent);
		} else if (toggle.getText().toString().equals("Überwachter")) {
			Constants.pfleger = false;
		}

		finish();
	}

}
