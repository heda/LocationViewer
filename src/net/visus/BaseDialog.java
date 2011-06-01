package net.visus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public abstract class BaseDialog implements DialogInterface.OnClickListener
{
	public interface OnDismissListener
	{
		public void onDialogDismissed(BaseDialog dialog);
	}

	protected AlertDialog.Builder m_Builder;
	protected Context m_Context;
	protected OnDismissListener m_DismissListener; 

	public int RequestCode = 0;
	public String TitleText = "Dialog";
	public String AcceptText = "Accept";
	public String CancelText = "Cancel";
	public boolean DidAccept = false;

	protected BaseDialog(Context context, int requestCode, OnDismissListener dismissListener)
	{
		this.m_Builder = new AlertDialog.Builder(context);
		this.m_Context = context;
		this.m_DismissListener = dismissListener;
		this.RequestCode = requestCode;
	}

	protected void prepareDialog()
	{
		this.m_Builder.setTitle(this.TitleText);
		this.m_Builder.setPositiveButton(AcceptText, this);
		this.m_Builder.setNegativeButton(CancelText, this);
	}

	public void show()
	{
		this.prepareDialog();
		AlertDialog dialog = this.m_Builder.create();
		this.onCreate(dialog);
		dialog.show();
	}

	protected void onCreate(AlertDialog dialog)
	{
	}

	public void onClick(DialogInterface dialog, int buttonId)
	{
		switch(buttonId)
		{
			case DialogInterface.BUTTON_POSITIVE:
				this.DidAccept = true;
			case DialogInterface.BUTTON_NEGATIVE:
				{
					dialog.dismiss();
					if (this.m_DismissListener != null)
					{
						this.m_DismissListener.onDialogDismissed(this);
					}
				} break;
			default:
				break;
		}
	}
}
