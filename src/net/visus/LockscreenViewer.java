package net.visus;

import net.visus.constants.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class LockscreenViewer extends Activity implements OnClickListener,
		net.visus.BaseDialog.OnDismissListener {
	ImageView background;
	Button alertbtn;
	Button unlockBtn;
	private static final int DIALOG_EDITTEXT = 0x0000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lockscreen_page);

		// Image
		background = (ImageView) findViewById(R.id.imageView1);
		background.setImageResource(R.drawable.back);
		// button and seekbar
		alertbtn = (Button) findViewById(R.id.lockscreen_alert_btn);
		unlockBtn = (Button) findViewById(R.id.lockscreen_unlock_btn);
		unlockBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showUnlockPage();
			}
		});
		// button listeners
		alertbtn.setOnClickListener(this);

	}

	public void onClick(View v) {
		// Alert Email oder SMS absenden und Alret zeigen
		alarmPressed();

	}

	private void showUnlockPage() {
		// Shows the unlock dialog
		UnlockDialog unlock_dialog = new UnlockDialog(LockscreenViewer.this,
				DIALOG_EDITTEXT, this);
		unlock_dialog.TitleText = "PIN Eingeben";
		unlock_dialog.show();

	}

	/** Called when Dialog is dismissed */
	public void onDismiss(BaseDialog dialog) {
		String enteredPin = ((UnlockDialog) dialog).Text;
		if (enteredPin.equals(Constants.pin)) {
			finish();
		} else {
			AlertDialog alertDialog = new AlertDialog.Builder(
					LockscreenViewer.this).create();
			alertDialog.setTitle("Falscher Status!");
			alertDialog.setMessage("Falsche PIN !");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// and terminate
					// finish();
				}
			});
			alertDialog.show();
		}

	}

	/** Called when Dialog is dismissed */
	public void onDialogDismissed(BaseDialog dialog) {
		if (!dialog.DidAccept) {
			return;
		} else {
			String enteredPin = ((UnlockDialog) dialog).Text;
			if (enteredPin.equals(Constants.pin)) {
				finish();
			} else {
				AlertDialog alertDialog = new AlertDialog.Builder(
						LockscreenViewer.this).create();
				alertDialog.setTitle("Falscher Status!");
				alertDialog.setMessage("Falsche PIN !");
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				alertDialog.show();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// show unlock dialog
			showUnlockPage();
			return true;
		} else {
			// do nothing, stay on lockscreen
			return true;
		}
	}

	private void alarmPressed() {

		// Check
		Boolean smssend = false;
		Boolean mailsend = false;

		if (Constants.sms != null && Constants.sms.length() > 0) {

			SmsManager smsmanager = SmsManager.getDefault();

			// Text zusammenbauen
			String smstext = "ALARM von " + Constants.oldFolkName + ", "
					+ Constants.oldFolkPrename + " gedrückt! Link: "
					+ Constants.actualPositionLink;

			// SMS muss kleiner 160 Zeichen sein, sonst Null Pointer
			if (smstext.length() < 160) {
				smsmanager.sendTextMessage(Constants.sms, null, smstext, null,
						null);
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
			String subject = "AlarmKnopf gedrückt !! Patient: "
					+ Constants.oldFolkName + ", " + Constants.oldFolkPrename;

			// Email Text
			String text = "Ihr Patient hat den Alarm-Knopf gedrückt. Folgender Link zeigt den derzeitigen Aufenthaltsort.\n"
					+ Constants.actualPositionLink;

			email.putExtra(Intent.EXTRA_EMAIL, new String[] { Constants.email });
			email.putExtra(Intent.EXTRA_SUBJECT, subject);
			email.putExtra(Intent.EXTRA_TEXT, text);

			// Email versenden
			try {
				startActivity(Intent.createChooser(email, "Send mail..."));
				mailsend = true;
			} catch (android.content.ActivityNotFoundException ex) {
				mailsend = false;
			}

			// Alert, dass Alarm rausgesendet wurde.
			if (smssend || mailsend) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						LockscreenViewer.this).create();
				alertDialog.setTitle("Hilferuf erfolg");
				alertDialog
						.setMessage("Es wurde erfolgreich eine Email und eine SMS an Ihren Pfleger geschickt. Bitte bewegen Sie sich nicht vom Fleck !!!");
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// and terminate
								// finish();
							}
						});
				alertDialog.show();
			}
		}
	}
}