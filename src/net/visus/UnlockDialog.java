package net.visus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class UnlockDialog extends BaseDialog implements OnFocusChangeListener {
	private EditText m_EditText;


	public String Text = "";

	public UnlockDialog(Activity activity, int requestCode,
			OnDismissListener dismissListener) {
		super(activity, requestCode, dismissListener);
	}

	@Override
	protected void onCreate(AlertDialog dialog) {
		LayoutInflater inflator = (LayoutInflater) this.m_Context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflator.inflate(R.layout.unlock_dialog, null);

		this.m_EditText = (EditText) view
				.findViewById(R.id.Dialog_EditText_Text);
		this.m_EditText.setText("");
		this.m_EditText.setOnFocusChangeListener(this);

		dialog.setView(view, 0, 0, 0, 0);
	}

	@Override
	public void onClick(DialogInterface dialog, int buttonId) {
		this.Text = this.m_EditText.getText().toString();
		super.onClick(dialog, buttonId);
	}

	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}
}