package farmersfridge.farmersfridge.menu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.adapter.MenuMainAdapter;
import farmersfridge.farmersfridge.asynctasks.MenuDateTask;
import farmersfridge.farmersfridge.database.models.MenuItem;
import farmersfridge.farmersfridge.models.MenuModel;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 6/9/2016.
 */
public class MenuMain extends Fragment {
    private View v;

    private MenuMainAdapter adapter;
    @BindView(R.id.recyclerView)RecyclerView recView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        v = inflater.inflate(R.layout.menu_refactor, container, false);
        ButterKnife.bind(this, v);

        recView = (RecyclerView) v.findViewById(R.id.recyclerView);

    //    initRecyclerViews();
        SessionStores.MENU_MODEL = new MenuModel();
        new MenuDateTask(getActivity(), MenuMain.this, SessionStores.MENU_MODEL.menu());

        return v;
    }

    public void initRecyclerViews(){
        recView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(llm);
        //Adapter set for Menu items
        adapter = new MenuMainAdapter(getActivity(), MenuItem.getAllItems());
        recView.setAdapter(adapter);
    }
}


