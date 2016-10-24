package farmersfridge.farmersfridge;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ProgressBar;

import farmersfridge.farmersfridge.asynctasks.GetFavouritesTask;
import farmersfridge.farmersfridge.asynctasks.MenuTask;
import farmersfridge.farmersfridge.models.MenuModel;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;


public class SplashActivity extends Activity {

    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.progressBarMain);

        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#90b775"), PorterDuff.Mode.MULTIPLY);

        if(Constants.ACCESS_TOKEN_KEY.length() > 0) {
            new SessionStores(this).saveAccessToken(Constants.ACCESS_TOKEN_KEY);
        }

        SessionStores.MENU_MODEL = new MenuModel();
    //    new MenuTask(this, SessionStores.MENU_MODEL.menu());
        if(new SessionStores(this).getLoginStatus() != null && new SessionStores(this).getAccessToken() != null) {
            new GetFavouritesTask(this, SessionStores.MENU_MODEL.getFavouritesRes());
        } else {
            new MenuTask(this, SessionStores.MENU_MODEL.menu());
        }
    }
}
