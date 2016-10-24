package farmersfridge.farmersfridge.my_fridge;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.adapter.FavouriteMenuAdapter;
import farmersfridge.farmersfridge.asynctasks.FetchFavouritesTask;
import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.models.MenuModel;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.MenuItemFetches;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 7/25/2016.
 */
public class MyFridge extends Fragment {

    @BindView(R.id.yourrewardprogress)
    TextView yourrewardprogress;
    @BindView(R.id.youroffers)
    TextView youroffers;
    @BindView(R.id.txtForOffMainSub)
    TextView txtForOffMainSub;
    @BindView(R.id.yourrewardsDesc)
    TextView yourrewardsDesc;
    RatingBar ratingBar;

    @BindView(R.id.recyclerForFavrts)
    RecyclerView recyclerVwFavrts;
    @BindView(R.id.arrowLeft)
    ImageView arrowLeft;
    @BindView(R.id.arrowRight)
    ImageView arrowRight;

    private LinearLayoutManager linearLayRecView;
    private int i = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        View v = inflater.inflate(R.layout.my_fridge, container, false);
        ButterKnife.bind(this, v);

        //Header text updates
        ((FragmentMain) getActivity()).setHeaderText("My Fridge");

        //Favourites are fetched from DB
        Favourites favourites = new Favourites();
        List<Favourites> allFavRow = favourites.getAllRow();
        if (allFavRow.size() > 0) { //if favourites presents in db then updateUi() calls
            updateUi();
        } else { //else favourite task calls
            SessionStores.MENU_MODEL = new MenuModel();
            new FetchFavouritesTask(getActivity(), MyFridge.this, SessionStores.MENU_MODEL.getFavouritesRes());
        }

        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar1);
        ratingBar.setRating(3);
        ratingBar.setStepSize((float) 0.3);

        //Typefaces
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicro Pro Bold Italic.otf");
        Typeface signupfont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-SemiBold.otf");
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Bold.otf");
        Typeface font3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Regular.otf");

        txtForOffMainSub.setTypeface(font2);
        yourrewardprogress.setTypeface(font2);
        yourrewardsDesc.setTypeface(font3);
        youroffers.setTypeface(font2);

        //Left arrow click listener
        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i--;
                if (i >= 0) {
                    recyclerVwFavrts.scrollToPosition(i);
                } else {
                    i++;
                }
            }
        });

        //Right arrow click listener
        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i < Constants.favArray.size()) {
                    recyclerVwFavrts.scrollToPosition(i);
                } else {
                    i--;
                }
            }
        });
        return v;
    }

    public void updateUi() {
        //Menu items fetched according to the favourites
        MenuItemFetches.getFavourites();
        if (!(Constants.favArray.size() > 0)) {
            arrowLeft.setVisibility(View.GONE);
            arrowRight.setVisibility(View.GONE);
        } else {
            arrowLeft.setVisibility(View.VISIBLE);
            arrowRight.setVisibility(View.VISIBLE);
        }
        linearLayRecView = new LinearLayoutManager(getActivity());
        linearLayRecView.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerVwFavrts.setLayoutManager(linearLayRecView);
        //Adapter for recyclerview items
        recyclerVwFavrts.setAdapter(new FavouriteMenuAdapter(getActivity()));
    }
}
