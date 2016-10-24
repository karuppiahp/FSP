package farmersfridge.farmersfridge.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 7/8/2016.
 */
public class SliderFragmentDrawer extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private FragmentDrawerListener drawerListener;
    private RelativeLayout layForDeals, layForMenus, layForLocations, layForAbout, layForMyFridge, layForSettings;
    private ImageView imgForClose;
    private static TextView txtForLogin;
    private static TextView txtForLogout;


    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.navigation_menus, container, false);
        layForDeals = (RelativeLayout) layout.findViewById(R.id.layForDeals);
        layForMenus = (RelativeLayout) layout.findViewById(R.id.layForMenus);
        layForLocations = (RelativeLayout) layout.findViewById(R.id.layForLocations);
        layForAbout = (RelativeLayout) layout.findViewById(R.id.layForAbout);
        layForMyFridge = (RelativeLayout) layout.findViewById(R.id.layForMyFridge);
        layForSettings = (RelativeLayout) layout.findViewById(R.id.layForSettings);
        imgForClose = (ImageView) layout.findViewById(R.id.imgForClose);
        txtForLogin = (TextView) layout.findViewById(R.id.txtForLogin);
        txtForLogout = (TextView) layout.findViewById(R.id.txtForLogout);

        /*
         * Button click listeners for menu options
         */
        //Deals option
        layForDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerListener.onDrawerItemSelected(view, 0);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        //Menu option
        layForMenus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerListener.onDrawerItemSelected(view, 1);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        //Location option
        layForLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerListener.onDrawerItemSelected(view, 2);
            }
        });

        //About Us option
        layForAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerListener.onDrawerItemSelected(view, 3);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        //MyFridge option
        layForMyFridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerListener.onDrawerItemSelected(view, 4);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        //Settings option
        layForSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerListener.onDrawerItemSelected(view, 5);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        //Close icon option
        imgForClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerListener.onDrawerItemSelected(view, 6);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        //Login option
        txtForLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerListener.onDrawerItemSelected(view, 7);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        //Logout option
        txtForLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerListener.onDrawerItemSelected(view, 8);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        //Checks if user logged in or not for login & logout button visibility
        if (new SessionStores(getActivity()).getLoginStatus() != null && new SessionStores(getActivity()).getAccessToken() != null) {
            txtForLogin.setVisibility(View.GONE);
            txtForLogout.setVisibility(View.VISIBLE);
        } else {
            txtForLogout.setVisibility(View.GONE);
            txtForLogin.setVisibility(View.VISIBLE);
        }

        return layout;
    }

    //Slider open
    public void openSlider() {
        mDrawerLayout.openDrawer(containerView);
    }

    //Slider close
    public void closeSlider() {
        mDrawerLayout.closeDrawer(containerView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

    public static void loginLogout(String visibility) {
        if (visibility.equals("logout")) {
            txtForLogin.setVisibility(View.GONE);
            txtForLogout.setVisibility(View.VISIBLE);
        } else {
            txtForLogout.setVisibility(View.GONE);
            txtForLogin.setVisibility(View.VISIBLE);
        }
    }
}
