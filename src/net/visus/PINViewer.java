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

public class PINViewer extends Activity implements OnClickListener {
	EditText editText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin_page);

		// button
		Button okBtn = (Button) findViewById(R.id.PinPage_okbtn);
		// edittext
		editText = (EditText) findViewById(R.id.pin_editText);

		// button listeners
		okBtn.setOnClickListener(this);

	}

	public void onClick(View v) {
		// Check if the PIN is matching
		if (editText.getText().toString().equals(Constants.pin)) {
			// if yes: notify
			AlertDialog alertDialog = new AlertDialog.Builder(PINViewer.this)
					.create();
			alertDialog.setTitle("Hallo Pfleger!");
			alertDialog.setMessage("Ihr Status ist nun Pfleger.");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// and terminate
					finish();
				}
			});
			alertDialog.show();
		} else {
			// if not: show ErrorDialog:
			AlertDialog alertDialog = new AlertDialog.Builder(PINViewer.this)
					.create();
			alertDialog.setTitle("PIN falsch!");
			alertDialog.setMessage("Sie sind kein Pfleger...");
			alertDialog.setButton("Zurück",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// set the modus back to überwachter
							Constants.pfleger = false;
							// and terminate
							finish();
						}
					});
			alertDialog.show();
		}

	}
}