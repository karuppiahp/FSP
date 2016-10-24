package activities.mswift.info.walaapp.wala.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 11/30/2015.
 */
public class OnBoardingSignupActivity extends Activity implements View.OnClickListener {

    private ImageView btnForFb, btnForEmail;
    String installationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_boarding);

        installationId = SessionStores.getInstallationId(this);
        if (installationId != null && installationId.length() > 0) {
            Intent intentToLogin = new Intent(OnBoardingSignupActivity.this, LoginActivity.class);
            startActivity(intentToLogin);
            finish();
        } else {
        }

        btnForFb = (ImageView) findViewById(R.id.fbBtn);
        btnForEmail = (ImageView) findViewById(R.id.emailBtn);

        btnForEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.emailBtn) {
            /** Intent to signup activity */
            Intent intentToSignUp = new Intent(OnBoardingSignupActivity.this, SignUpActivity.class);
            startActivity(intentToSignUp);
            finish();
        }
    }
}
