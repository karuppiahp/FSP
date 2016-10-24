package activities.mswift.info.walaapp.wala.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;

/**
 * Created by karuppiah on 12/16/2015.
 */
public class SettingsActivity extends Fragment {

    private View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.settings, container, false);

        return v;
    }
}
