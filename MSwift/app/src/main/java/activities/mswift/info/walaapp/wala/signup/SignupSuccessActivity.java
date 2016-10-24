package activities.mswift.info.walaapp.wala.signup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.FbLoginTask;
import activities.mswift.info.walaapp.wala.asyntasks.LoginTask;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 3/14/2016.
 */
public class SignupSuccessActivity extends Activity implements View.OnClickListener {

    private ImageView btnForFinancial;
    private TextView txtForTakeToApp;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_success);

        btnForFinancial = (ImageView) findViewById(R.id.btnForFinancial);
        txtForTakeToApp = (TextView) findViewById(R.id.movetoApp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnForFinancial.setOnClickListener(this);
        txtForTakeToApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnForFinancial) {
            //user coming from sign up
            Constants.userRegister = true;
            progressBar.setVisibility(View.VISIBLE);
            if(SessionStores.getRegViaFb(this) == null || !(SessionStores.getRegViaFb(this).equals("true"))) {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "password");
                params.put("username", SessionStores.getUserName(this));
                params.put("password", SessionStores.getUserPwd(this));
                params.put("scope", "PRODUCTION");
                /** Login task executes */
                new LoginTask(this, params, progressBar);
            } else {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", SessionStores.getFbInstallationId(this));
                params.put("ExternalAccessToken", SessionStores.getFbAccessToken(this));
                params.put("LoginProvider", "FACEBOOK");
                params.put("BasicKey", Constants.BASIC_KEY_FB);
                new FbLoginTask(this, params, progressBar);
            }
        }

        if(v.getId() == R.id.movetoApp) {
            //user coming from sign up
            Constants.userRegister = false;
            progressBar.setVisibility(View.VISIBLE);
            if(SessionStores.getRegViaFb(this) == null || !(SessionStores.getRegViaFb(this).equals("true"))) {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "password");
                params.put("username", SessionStores.getUserName(this));
                params.put("password", SessionStores.getUserPwd(this));
                params.put("scope", "PRODUCTION");
                /** Login task executes */
                new LoginTask(this, params, progressBar);
            } else {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", SessionStores.getInstallationId(this));
                params.put("ExternalAccessToken", SessionStores.getFbAccessToken(this));
                params.put("LoginProvider", "FACEBOOK");
                params.put("BasicKey", Constants.BASIC_KEY_FB);
                new FbLoginTask(this, params, progressBar);
            }
        }
    }
}
