package farmersfridge.farmersfridge.map;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.adapter.LocationsMenuAdapter;
import farmersfridge.farmersfridge.adapter.MenuMainAdapter;
import farmersfridge.farmersfridge.utils.Constants;

/**
 * Created by karuppiah on 8/11/2016.
 */
public class LocationsMenu extends Fragment {
    private View v;

    private MenuMainAdapter adapter;
    @BindView(R.id.recyclerView)RecyclerView recView;
    @BindView(R.id.layForHeader)RelativeLayout layForHeader;
    @BindView(R.id.txtForHeader)TextView txtForHeader;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        v = inflater.inflate(R.layout.menu_refactor, container, false);
        ButterKnife.bind(this, v);

        Typeface headerFnt = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Bold.otf");
        txtForHeader.setTypeface(headerFnt);
        layForHeader.setVisibility(View.VISIBLE);
        txtForHeader.setText(Constants.LOC_HEADER);

        recView = (RecyclerView) v.findViewById(R.id.recyclerView);

        //Recyclerview items are set
        initRecyclerViews();

        return v;
    }

    public void initRecyclerViews(){
        recView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(llm);
        //Adapter for recyclerview
        recView.setAdapter(new LocationsMenuAdapter(getActivity(), Constants.locCategoryArray, Constants.locMenuArray));
    }
}
