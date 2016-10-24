package farmersfridge.farmersfridge.menu;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.adapter.MenuDetailAdapter;
import farmersfridge.farmersfridge.asynctasks.DeleteFavInfoTask;
import farmersfridge.farmersfridge.asynctasks.UpdateFavInfoTask;
import farmersfridge.farmersfridge.database.models.MenuItem;
import farmersfridge.farmersfridge.map.MapMain;
import farmersfridge.farmersfridge.models.MenuItemModel;
import farmersfridge.farmersfridge.models.MenuModel;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.TempData;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by kruno on 13.06.16..
 */
public class MenuDetail extends android.support.v4.app.Fragment {

    private View v;
    private Toolbar toolbar;
    public static int adapterPossition = 0, arrayPos = 0;
    private String category;
    @BindView(R.id.rec_menu_detail)
    RecyclerView rec_menu_detail;
    @BindView(R.id.txtForHeader)
    TextView txtForCategory;
    @BindView(R.id.imgForClose)
    ImageView imgForClose;
    public static RelativeLayout layForInfo;
    public static TextView txtForHeader, txtForPts, txtForSubTitle, txtForDesc;
    public static NetworkImageView imgForChart;
    public static ImageView imgForInfoFav, btnForFind;
    public static boolean infoFavPresent = true;
    public static View viewItem;
    public static ImageView imgViewItem;
    List<MenuItem> menuListRec = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        v = inflater.inflate(R.layout.menu_detail, container, false);
        ButterKnife.bind(this, v);
        layForInfo = (RelativeLayout) v.findViewById(R.id.layForInfo);
        txtForHeader = (TextView) v.findViewById(R.id.txtForInfoHeader);
        txtForPts = (TextView) v.findViewById(R.id.txtForInfoPts);
        txtForSubTitle = (TextView) v.findViewById(R.id.txtForInfoSubTitle);
        txtForDesc = (TextView) v.findViewById(R.id.txtForInfoDesc);
        imgForChart = (NetworkImageView) v.findViewById(R.id.imgForChart);
        imgForInfoFav = (ImageView) v.findViewById(R.id.imgForInfoFav);
        btnForFind = (ImageView) v.findViewById(R.id.imgForInfoFind);

        //get bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            adapterPossition = bundle.getInt("menuItem");
            category = bundle.getString("category");
            menuListRec = MenuItem.getAllItemsByCat(category);
        }

        //Typefaces
        Typeface headerFnt = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Bold.otf");
        Typeface headerInfoFnt = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicroProSemibold-Italic.otf");
        Typeface priceFnt = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Medium.otf");
        Typeface infoDescFnt = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Light.otf");
        txtForCategory.setTypeface(headerFnt);
        txtForHeader.setTypeface(headerInfoFnt);
        txtForPts.setTypeface(priceFnt);
        txtForSubTitle.setTypeface(priceFnt);
        txtForDesc.setTypeface(infoDescFnt);
        txtForCategory.setText(category.toUpperCase());

        //Close icon click listener
        imgForClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layForInfo.setVisibility(View.GONE);
            }
        });

        //Find button click listener
        btnForFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.LOC_FROM = "Info";
                Constants.VEND_NAME = txtForHeader.getText().toString();
                //Fragment replaces with locations Map screen
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MapMain fragment = new MapMain();
                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                fragmentTransaction.commit();
            }
        });

        //Favourite icon click listener
        imgForInfoFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new SessionStores(getActivity()).getLoginStatus() != null && new SessionStores(getActivity()).getAccessToken() != null) {
                    String favName = txtForHeader.getText().toString();
                    if (infoFavPresent == true) { //If favourite presents then delete task calls
                        SessionStores.MENU_MODEL = new MenuModel(favName);
                        new DeleteFavInfoTask(getActivity(), SessionStores.MENU_MODEL.deleteFavourites(), rec_menu_detail, category, favName);
                    } else { //else update task
                        SessionStores.MENU_MODEL = new MenuModel(favName);
                        new UpdateFavInfoTask(getActivity(), SessionStores.MENU_MODEL.updateFavourites(), rec_menu_detail, category, favName);
                    }
                } else {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    Utils.ShowAlertLogin(getActivity(), "You need to be logged into use favorites", fragmentManager);
                }
            }
        });

        //initialise recyclerview and atach adapter
        initRecyclerView();

        return v;
    }

    public void initRecyclerView(){
        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        llm1.setOrientation(LinearLayoutManager.VERTICAL);
        rec_menu_detail.setLayoutManager(llm1);
        //Adapter for menu items
        rec_menu_detail.setAdapter(new MenuDetailAdapter(getActivity(), menuListRec));
        llm1.scrollToPosition(adapterPossition);
    }

    public List<MenuItemModel> getMenu(){

        List<String> myTitle = Arrays.asList(TempData.salads);
        List<String> myDescription = Arrays.asList(TempData.salads_description);
        List<String> myDescriptionLong = Arrays.asList(TempData.salads_description1);
        List<String> myImageUrl = Arrays.asList(TempData.salads_image);
        List<Boolean> myFavourite = Arrays.asList(TempData.salads_isFavourite);

        List<MenuItemModel> menuList = new ArrayList<MenuItemModel>();
        for (int i = 0; i < myTitle.size(); i++) {

            MenuItemModel menu = new MenuItemModel();
            menu.setTitle(myTitle.get(i));
            menu.setDescription(myDescription.get(i));
            menu.setDescription_long(myDescriptionLong.get(i));
            menu.setImageUrl(myImageUrl.get(i));
            menu.setIsFavourite(myFavourite.get(i));

            menuList.add(menu);
        }

        return menuList;
    }

    @OnClick(R.id.layForInfoDetail) void layClicks() {
    }

    public void backButton(){
        FragmentManager fm = getFragmentManager();
        fm.popBackStackImmediate();
    }
}
