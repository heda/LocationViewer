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

public class InformationViewer extends Activity implements OnClickListener {
	EditText vorname;
	EditText nachname;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patienteninfo_page);

		// button
		Button okBtn = (Button) findViewById(R.id.patientenInfo_ok);
		// pins
		vorname = (EditText) findViewById(R.id.patientenInfo_vornameEdit);
		nachname = (EditText) findViewById(R.id.patientenInfo_nameEdit);

		vorname.setText(Constants.oldFolkPrename);
		nachname.setText(Constants.oldFolkName);

		// button listeners
		okBtn.setOnClickListener(this);

	}

	public void onClick(View v) {

		// save the Strings
		Constants.oldFolkPrename = vorname.getText().toString();
		Constants.oldFolkName = nachname.getText().toString();
		// notify
		AlertDialog alertDialog = new AlertDialog.Builder(
				InformationViewer.this).create();
		alertDialog.setTitle("Speichern erfolgreich!");
		alertDialog.setMessage("Die neuen Patientendaten wurden gespeichert.");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// close window
				finish();
			}
		});
		alertDialog.show();

	}
}