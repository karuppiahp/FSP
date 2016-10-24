package farmersfridge.farmersfridge.my_fridge;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.adapter.FavouriteMenuListAdapter;
import farmersfridge.farmersfridge.asynctasks.DeleteFavInfoMyFridgeTask;
import farmersfridge.farmersfridge.map.MapMain;
import farmersfridge.farmersfridge.models.FavApiModel;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 8/18/2016.
 */
public class MyFridgeList extends android.support.v4.app.Fragment {

    private View v;
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

        //Typefaces
        Typeface headerFnt = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-SemiBoldExpanded.otf");
        Typeface headerInfoFnt = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicroProSemibold-Italic.otf");
        Typeface priceFnt = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Medium.otf");
        Typeface infoDescFnt = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Light.otf");
        txtForCategory.setTypeface(headerFnt);
        txtForHeader.setTypeface(headerInfoFnt);
        txtForPts.setTypeface(priceFnt);
        txtForSubTitle.setTypeface(priceFnt);
        txtForDesc.setTypeface(infoDescFnt);
        txtForCategory.setText(category);

        //Close icon click listener
        imgForClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layForInfo.setVisibility(View.GONE);
            }
        });

        //Favourite icon click listener
        imgForInfoFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = txtForHeader.getText().toString();
                RecyclerView.Adapter adapter = new FavouriteMenuListAdapter(getActivity());
                SessionStores.FAV_MODEL = new FavApiModel(title);
                //Delete api task calls
                new DeleteFavInfoMyFridgeTask(getActivity(), SessionStores.FAV_MODEL.deleteFavourites(), MyFridgeList.this, title);
            }
        });

        //find button click listener
        btnForFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.LOC_FROM = "Info";
                Constants.VEND_NAME = txtForHeader.getText().toString();
                //Fragment replaces with locations map screen
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MapMain fragment = new MapMain();
                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                fragmentTransaction.commit();
            }
        });

        //initialise recyclerview and atach adapter
        initRecyclerView();

        return v;
    }

    public void initRecyclerView() {
        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        llm1.setOrientation(LinearLayoutManager.VERTICAL);
        rec_menu_detail.setLayoutManager(llm1);
        //Adapter for recyclerview items
        rec_menu_detail.setAdapter(new FavouriteMenuListAdapter(getActivity()));
    }

    @OnClick(R.id.layForInfoDetail)
    void layClicks() {
    }

    public void backButton() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStackImmediate();
    }
}
