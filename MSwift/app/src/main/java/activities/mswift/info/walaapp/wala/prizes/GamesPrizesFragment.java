package activities.mswift.info.walaapp.wala.prizes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activities.mswift.info.walaapp.R;

/**
 * Created by karuppiah on 2/11/2016.
 */
public class GamesPrizesFragment extends Fragment {

    private View balsavings;

    public static GamesPrizesFragment newInstance() {
        GamesPrizesFragment fragment = new GamesPrizesFragment();

        return fragment;
    }

    public GamesPrizesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        balsavings = inflater.inflate(R.layout.prizes_fragment, container, false);

        return balsavings;
    }

}