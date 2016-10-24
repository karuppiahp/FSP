package farmersfridge.farmersfridge.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.about_us.AboutUs;
import farmersfridge.farmersfridge.home.DealsFragment;
import farmersfridge.farmersfridge.login_register.Login;
import farmersfridge.farmersfridge.map.MapMain;
import farmersfridge.farmersfridge.menu.MenuMain;
import farmersfridge.farmersfridge.my_fridge.MyFridge;
import farmersfridge.farmersfridge.settings.Settings;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 7/8/2016.
 */
public class FragmentMain extends AppCompatActivity implements SliderFragmentDrawer.FragmentDrawerListener, View.OnClickListener {

    private Toolbar mToolbar;
    private SliderFragmentDrawer drawerFragment;
    private TextView txtForHeader, txtForLoginReg, txtForLogout;
    private ImageView btnForHamburger;
    private ProgressBar progressBar;
    private boolean doubleBackToExitPressedOnce = false;
    private static final int REQUEST_FINE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txtForHeader = (TextView) findViewById(R.id.toolbar_title);
        txtForLoginReg = (TextView) findViewById(R.id.txtForLogin);
        txtForLogout = (TextView) findViewById(R.id.txtForLogout);
        btnForHamburger = (ImageView) findViewById(R.id.toolbar_back_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMain);

        //Toolbar set
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Typeface headerFont = Typeface.createFromAsset(this.getAssets(), "fonts/GarageFonts - FreightMicro Pro Bold Italic.otf");
        txtForHeader.setTypeface(headerFont);

        //Navigation drawer fragment is called
        drawerFragment = (SliderFragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);

        btnForHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(FragmentMain.this);
                drawerFragment.openSlider();
            }
        });

        //Login text set
        setLoginRegText("LOG IN");

        //checks if user logged in or not
        if (new SessionStores(this).getLoginStatus() != null && new SessionStores(this).getAccessToken() != null) {
            setLoginBtnGone();
        } else {
            setLoginBtnVisibile();
        }
    }

    // Back button visibility
    public void setBtnVisibility() {
        btnForHamburger.setVisibility(View.GONE);
    }

    //Menu button visibility
    public void setMenuVisibility() {
        btnForHamburger.setVisibility(View.VISIBLE);
    }

    //Text for header set
    public void setHeaderText(String title) {
        txtForHeader.setText(title);
    }

    //Text for login/reg set
    public void setLoginRegText(String title) {
        txtForLoginReg.setText(title);
    }

    //set login btn visibility
    public void setLoginBtnVisibile() {
        txtForLogout.setVisibility(View.GONE);
        txtForLoginReg.setVisibility(View.VISIBLE);
        txtForLoginReg.setOnClickListener(this);
        SliderFragmentDrawer.loginLogout("login");
    }

    //set login btn visibility
    public void setLoginBtnGone() {
        txtForLoginReg.setVisibility(View.GONE);
        txtForLogout.setVisibility(View.VISIBLE);
        txtForLogout.setOnClickListener(this);
        SliderFragmentDrawer.loginLogout("logout");
    }

    //Progress bar in fragment activity visibile
    public void setProgressBarVisibile() {
        progressBar.setVisibility(View.VISIBLE);
    }

    //Progress bar in fragment activity gone
    public void setProgressBarGone() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Utils.hideKeyboard(this);
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0: //Deals screen
                fragment = new DealsFragment();
                title = "Deals";
                setHeaderText(title); // text for header set
                break;
            case 1: //Menu screen
                drawerFragment.closeSlider();
                fragment = new MenuMain();
                title = "Menu";
                setHeaderText(title); // text for header set
                break;
            case 2: //Locations screen
                drawerFragment.closeSlider();
                //permission for map
                loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
                Constants.LOC_FROM = "";
                Constants.VEND_NAME = "";
                fragment = new MapMain();
                title = "Locations";
                setHeaderText(title); // text for header set
                break;
            case 3: //About Us screen
                fragment = new AboutUs();
                title = "About";
                setHeaderText(title); // text for header set
                break;
            case 4: //My Fridge screen
                fragment = new MyFridge();
                title = "My Fridge";
                break;
            case 5: //Settings screen
                fragment = new Settings();
                title = "Settings";
                setHeaderText(title); // text for header set
                break;
            case 6: //close the slider
                drawerFragment.closeSlider();
                break;
            case 7: //Login screen
                fragment = new Login();
                title = "";
                setHeaderText(title); // text for header set
                break;
            case 8: //Logout
                setLoginBtnVisibile();
                SliderFragmentDrawer.loginLogout("login");
                new SessionStores(this).saveLoginStatus(null);
                new SessionStores(this).saveAccessToken(null);
                break;
            default:
                break;
        }

        if (fragment != null) {
            if (title.equals("My Fridge")) { //checks if screen is for My Fridge
                //Checks if user logged in or not
                if (new SessionStores(this).getLoginStatus() != null && new SessionStores(this).getAccessToken() != null) {
                    setMenuVisibility();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                    fragmentTransaction.commit();
                } else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Utils.ShowAlertLogin(this, "You need to be logged into use favorites", fragmentManager);
                }
            } else { //else normal fragments replaced
                setMenuVisibility();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                fragmentTransaction.commit();
            }
        }
    }

    //permission for map
    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm}, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                } else {
                    // no granted
                }
                return;
            }

        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        //to exit double tap back button
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, FragmentMain.this.getResources().getString(R.string.exit_msg), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txtForLogin) {
            Fragment fragment = new Login();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.realtabcontent, fragment);
            fragmentTransaction.commit();
        }

        if (view.getId() == R.id.txtForLogout) {
            setLoginBtnVisibile();
            new SessionStores(this).saveLoginStatus(null);
            new SessionStores(this).saveAccessToken(null);
        }
    }
}
