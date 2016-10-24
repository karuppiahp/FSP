package activities.mswift.info.walaapp.wala.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activities.mswift.info.walaapp.R;

/**
 * Created by karuppiah on 11/23/2015.
 */
public class MainActivity extends Fragment {

    private View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        v = inflater.inflate(R.layout.splash_screen, container, false);

        return v;
    }
}