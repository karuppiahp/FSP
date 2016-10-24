package activities.mswift.info.walaapp.wala.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;
import java.util.List;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.FaqListAdapter;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/18/2015.
 */
public class FaqActivity extends Fragment {

    private View v;
    private List<FaqListAdapter.Item> faqArray = new ArrayList<>();
    private RecyclerView recyclerview;
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        ((TabHostFragments) getActivity()).tabColorWhite();
        ((TabHostFragments) getActivity()).removeChild(); // remove child view

        v = inflater.inflate(R.layout.faq, container, false);

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_more_screen_faq_screen));

        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerViewForFaq);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        faqArray = Utils.getFaqList(getActivity());
        recyclerview.setAdapter(new FaqListAdapter(faqArray));

        return v;
    }
}
