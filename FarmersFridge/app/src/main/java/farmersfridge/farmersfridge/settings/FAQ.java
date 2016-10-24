package farmersfridge.farmersfridge.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.adapter.FaqAdapter;
import farmersfridge.farmersfridge.asynctasks.FaqTask;
import farmersfridge.farmersfridge.models.SettingsMainModel;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 6/21/2016.
 */
public class FAQ extends Fragment {

    @BindView(R.id.listViewFaq)
    ListView listView;
    public static ArrayList<SettingsMainModel> faqArray = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        View v = inflater.inflate(R.layout.faq, container, false);
        ButterKnife.bind(this, v);
        FragmentManager fragmentManager = getFragmentManager();
        SessionStores.SETTINGS_MAIN_MODEL = new SettingsMainModel();
        //FAQ api task calls
        new FaqTask(getActivity(), FAQ.this, SessionStores.SETTINGS_MAIN_MODEL.faq(), fragmentManager);
        return v;
    }

    //Listview items set with adapter
    public void setListItems() {
        listView.setAdapter(new FaqAdapter(getActivity(), faqArray));
    }

    @OnClick(R.id.backbtn)
    public void backButton() {
        //Fragment replaces with settings screen
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Settings fragment = new Settings();
        fragmentTransaction.replace(R.id.realtabcontent, fragment);
        fragmentTransaction.commit();
    }
}
