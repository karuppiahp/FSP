package com.eliqxir;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.eliqxir.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

public class Terms extends Activity {

	WebView webview;
	TextView textForHeader;
	ImageButton backImg, cartBtn;
	ProgressDialog dialog;
	

	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.terms);
		 CopyReadAssets();
		
	//	 finish();
		/*backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		cartBtn.setVisibility(View.GONE);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		backImg.setOnClickListener(this);
		dialog = new ProgressDialog(Terms.this);
		textForHeader.setText("Terms and Conditions");

		webview = (WebView) findViewById(R.id.webView1);
		webview.getSettings().setJavaScriptEnabled(true);

		webview.loadUrl("https://docs.google.com/gview?embedded=true&url=http://162.209.127.121/terms.pdf");
	
		String url="https://docs.google.com/gview?embedded=true&url=http://162.209.127.121/terms.pdf";
		webview.loadUrl(url+="&exportFormat=html&format=html");
		
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				Log.e("shouldOverrideUrlLoading",""+url);
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if(!dialog.isShowing())
				{		
				dialog.setMessage("Loading..");
				dialog.show();
				dialog.setCancelable(false);
				Log.e("onPageStarted",""+url);
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				dialog.dismiss();
				Log.e("onPageFinished",""+url);
				super.onPageFinished(view, url);
			}
		});*/
	}

	@SuppressWarnings("deprecation")
	private void CopyReadAssets()
    {
        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        File file = new File(getFilesDir(), "terms.pdf");
        try
        {
            in = assetManager.open("terms.pdf");
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e)
        {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + getFilesDir() + "/terms.pdf"),"application/pdf");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // No application to view, ask to download one
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No PDF Reader Application Found");
            builder.setMessage("Download one from Android Market?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent
                                    .setData(Uri
                                            .parse("market://details?id=com.adobe.reader"));
                            startActivity(marketIntent);
                        }
                    });
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           finish();
                           dialog.dismiss();
                        }
                    });
           
            builder.create().show();
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }
	/*@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backBtn:
			finish();
			break;
		default:
			break;
		}

	}*/
    
    @Override
    protected void onStop() {
     super.onStop();
     Log.e("Inside Terms","OnStop Method");
     finish();
    }
   
}
