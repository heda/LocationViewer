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

public class EmailNsmsViewer extends Activity implements OnClickListener {
	EditText email;
	EditText sms;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_n_sms_page);

		// button
		Button okBtn = (Button) findViewById(R.id.emailNsms_ok);
		// pins
		email = (EditText) findViewById(R.id.emailNsms_emailEdit);
		sms = (EditText) findViewById(R.id.emailNsms_smsEdit);

		email.setText(Constants.email);
		sms.setText(Constants.sms);

		// button listeners
		okBtn.setOnClickListener(this);

	}

	public void onClick(View v) {

		// save the Strings
		Constants.email = email.getText().toString();
		Constants.sms = sms.getText().toString();
		// notify
		AlertDialog alertDialog = new AlertDialog.Builder(EmailNsmsViewer.this)
				.create();
		alertDialog.setTitle("Speichern erfolgreich!");
		alertDialog
				.setMessage("Ihre neuen Benachrichtigungsdaten wurden gespeichert.");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// close window
				finish();
			}
		});
		alertDialog.show();

	}
}