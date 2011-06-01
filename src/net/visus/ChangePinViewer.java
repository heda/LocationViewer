package net.visus;

import net.visus.constants.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChangePinViewer extends Activity implements OnClickListener {
	EditText oldPin;
	EditText newPin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepin_page);

		// button
		Button okBtn = (Button) findViewById(R.id.changepin_okbtn);
		// pins
		oldPin = (EditText) findViewById(R.id.newpin_oldeditText);
		newPin = (EditText) findViewById(R.id.newPin_neuEditText);

		// button listeners
		okBtn.setOnClickListener(this);

	}

	public void onClick(View v) {
		// Check if the old PIN is matching
		if (oldPin.getText().toString().equals(Constants.pin)) {
			// if yes: change the PIN and notify
			Constants.pin = newPin.getText().toString();
			AlertDialog okDialog = new AlertDialog.Builder(ChangePinViewer.this)
					.create();
			okDialog.setTitle("PIN geändert!");
			okDialog.setMessage("Ihre Pin ist jetzt "
					+ newPin.getText().toString());
			okDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// close
					finish();
				}
			});
			okDialog.show();

		} else {
			// if not: show Dialog:
			AlertDialog alertDialog = new AlertDialog.Builder(
					ChangePinViewer.this).create();
			alertDialog.setTitle("PIN falsch!");
			alertDialog.setMessage("Ihre aktuelle PIN ist ein andere...");
			alertDialog.setButton("Nochmal",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// reset the edittextboxes
							oldPin.setText("");
							newPin.setText("");
						}
					});
			alertDialog.setButton2("Abbrechen",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// close
							finish();
						}
					});
			alertDialog.show();
		}

	}
}