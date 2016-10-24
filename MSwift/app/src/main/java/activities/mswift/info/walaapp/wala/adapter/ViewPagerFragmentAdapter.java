package activities.mswift.info.walaapp.wala.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.model.AboutUsModel;
import activities.mswift.info.walaapp.wala.model.AboutUsOurTeamModel;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/18/2015.
 */
@SuppressLint("ValidFragment")
public class ViewPagerFragmentAdapter extends Fragment {

    private String mCurrentPage;
    private FragmentManager fm;
    private ArrayList<AboutUsModel> headerArray = new ArrayList<>();
    private ArrayList<AboutUsOurTeamModel> ourTeamArray = new ArrayList<>();
    private RecyclerView recyclerview;
    private RecyclerView.LayoutManager mLayoutManager;

    @SuppressLint("ValidFragment")
    public ViewPagerFragmentAdapter(FragmentManager fm, ArrayList<AboutUsModel> headerArray) {
        this.fm = fm;
        this.headerArray = headerArray;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        /** Getting integer data of the key current_page from the bundle */
        mCurrentPage = data.getString("titleName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;

        for (int i = 0; i < headerArray.size(); i++) {
            /* view has been set */
            if (mCurrentPage.equals(headerArray.get(0).getName())) {
                v = inflater.inflate(R.layout.about_us_child, container, false);
            } else {
                v = inflater.inflate(R.layout.our_team, container, false);
                recyclerview = (RecyclerView) v.findViewById(R.id.recyclerViewForOurTeam);

                ourTeamArray = Utils.getOurTeamList(getActivity());
                mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerview.setLayoutManager(mLayoutManager);
                AboutUsTeamAdapter mAdapter = new AboutUsTeamAdapter(ourTeamArray);
                recyclerview.setAdapter(mAdapter);
            }
        }

        return v;
    }
}
