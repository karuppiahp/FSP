package farmersfridge.farmersfridge.map;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.adapter.LocationsListAdapter;
import farmersfridge.farmersfridge.utils.Constants;

/**
 * Created by karuppiah on 8/10/2016.
 */
public class LocationsList extends Fragment {

    private View v;
    @BindView(R.id.recyclerForMenuLocs)
    RecyclerView recyclerView;
    @BindView(R.id.itemlocation)
    TextView txtForItemLoc;
    @BindView(R.id.findyournapa)
    TextView txtForFindNapa;
    @BindView(R.id.subTitleFridges)
    TextView txtForFridgesTitle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        v = inflater.inflate(R.layout.locations_listview, container, false);
        ButterKnife.bind(this, v);

        Typeface itemHeaderTf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-SemiBold.otf");
        Typeface findNapaTf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicroProSemibold-Italic.otf");
        txtForItemLoc.setTypeface(itemHeaderTf);
        txtForFindNapa.setTypeface(findNapaTf);
        txtForFridgesTitle.setTypeface(findNapaTf);

        if (Constants.VEND_NAME.length() > 0) {
            txtForFindNapa.setText("Find Your " + Constants.VEND_NAME);
        } else {
            txtForFindNapa.setText("Find Your Fridges");
        }

        initRecyclerView();

        return v;
    }

    public void initRecyclerView() {
        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        llm1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm1);
        FragmentManager fragmentManager = getFragmentManager();
        recyclerView.setAdapter(new LocationsListAdapter(getActivity(), MapMain.locationsArray, fragmentManager));
    }
}
