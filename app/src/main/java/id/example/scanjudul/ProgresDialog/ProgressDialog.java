package id.example.scanjudul.ProgresDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import id.example.scanjudul.R;


public class ProgressDialog extends AlertDialog {

    protected View progressBar;
    protected TextView message;
    protected String messageText = "Please Wait..";

    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context context, String message) {
        super(context);
        this.messageText = message;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        message = (TextView) findViewById(R.id.message);
        progressBar = findViewById(R.id.progress);
        if (message != null && !TextUtils.isEmpty(messageText)) {
            message.setText(messageText);
        }
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public void setMessage(String message) {
        this.messageText = message;
    }

    protected TextView getMessageView() {
        return message;
    }
    protected View getProgressView() {
        return progressBar;
    }

    @Override
    public void show() {
        super.show();
        if (progressBar instanceof ProgressView) {
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    ((ProgressView) progressBar).start();
                }
            });
        }
    }
}
