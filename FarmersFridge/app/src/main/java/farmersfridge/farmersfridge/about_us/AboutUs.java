package farmersfridge.farmersfridge.about_us;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.AboutUsTask;
import farmersfridge.farmersfridge.models.AboutUsParaModel;
import farmersfridge.farmersfridge.models.AboutUsSubHeaderModel;
import farmersfridge.farmersfridge.models.SettingsMainModel;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 9/7/2016.
 */
public class AboutUs extends Fragment {
    @BindView(R.id.layForLinear)
    LinearLayout layForLinear;
    @BindView(R.id.layForRelative)
    RelativeLayout layForRelative;
    private TextView subHeaderTxt, descTxt;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View v = inflater.inflate(R.layout.aboutus_layout, container, false);
        ButterKnife.bind(this, v);

        //About us api task calls
        SessionStores.SETTINGS_MAIN_MODEL = new SettingsMainModel();
        FragmentManager fragmentManager = getFragmentManager();
        new AboutUsTask(getActivity(), AboutUs.this, SessionStores.SETTINGS_MAIN_MODEL.aboutUs(), fragmentManager);

        return v;
    }

    /*
     * Dynamic views are generated as per headers, sub-headers and contents
     */
    public void setViews(String headerMain, ArrayList<AboutUsSubHeaderModel> subHeaderArrayList) {
        ArrayList<AboutUsSubHeaderModel> subHeaderArrayListGet = subHeaderArrayList;
        int k = 0;
        //Main header count
        for (int i = 0; i < subHeaderArrayListGet.size(); i++) {
            Typeface headingsTF = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Bold.otf");

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View viewForLay = inflater.inflate(R.layout.about_dynamic_lay, null);
            LinearLayout layForLienarSub = (LinearLayout) viewForLay.findViewById(R.id.layForLinearSub);
            RelativeLayout layForRelativeSub = (RelativeLayout) viewForLay.findViewById(R.id.layForRelativeSub);
            View mainHeaderView = inflater.inflate(R.layout.aboutus_main_header_txt, null);
            TextView mainHeaderTxt = (TextView) mainHeaderView.findViewById(R.id.mainHeader);
            mainHeaderTxt.setTypeface(headingsTF);
            AboutUsSubHeaderModel subHeaderModel = subHeaderArrayListGet.get(i);
            String header = subHeaderModel.getSubHeader();

            /*
             * Background iamge has been set as per 3 counts
             * count 1 - wooden bg
             * count 2 - green bg
             * count 3 - white bg
             */
            if ((i + 1) % 3 == 0) {
                k = 0;
                layForRelativeSub.setBackgroundResource(R.drawable.deals_white_bar);
            } else {
                k++;
                if (k == 1) {
                    layForRelativeSub.setBackgroundResource(R.drawable.woodtexturebg);
                } else {
                    layForRelativeSub.setBackgroundResource(R.drawable.greenbg);
                }
            }

            //Sub header list
            if (header.length() > 0) {
                mainHeaderTxt.setText(header.toUpperCase());
                layForLienarSub.addView(mainHeaderView);
                ArrayList<AboutUsParaModel> paraArrayList = subHeaderModel.getParaList();
                //Content list
                for (int j = 0; j < paraArrayList.size(); j++) {
                    Typeface subHeadingsTF = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicro Pro Bold Italic.otf");
                    Typeface descTF = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Regular.otf");
                    LayoutInflater inflater2 = LayoutInflater.from(getActivity());
                    View subHeaderView = inflater2.inflate(R.layout.aboutus_header_txt, null);
                    subHeaderTxt = (TextView) subHeaderView.findViewById(R.id.subTitle);
                    subHeaderTxt.setTypeface(subHeadingsTF);
                    AboutUsParaModel paraModel = paraArrayList.get(j);
                    String subHeader = paraModel.getHeader();
                    String subContent = paraModel.getContent();

                    //Sub header text color as green for second main header
                    if (k == 2) {
                        subHeaderTxt.setTextColor(Color.parseColor("#8fb774"));
                    }

                    LayoutInflater inflater3 = LayoutInflater.from(getActivity());
                    View descView = inflater3.inflate(R.layout.aboutus_desc_txt, null);
                    descTxt = (TextView) descView.findViewById(R.id.aboutDesc);
                    descTxt.setTypeface(descTF);

                    //Checked sub header is present or not
                    if (subHeader.length() > 0) {
                        subHeaderTxt.setText(subHeader);
                        layForLienarSub.addView(subHeaderView);
                        descTxt.setText(subContent);
                        layForLienarSub.addView(descView);
                    } else {
                        subHeaderTxt.setVisibility(View.GONE);
                        descTxt.setText(subContent);
                        layForLienarSub.addView(descView);
                    }
                }
            } else {
                mainHeaderTxt.setVisibility(View.GONE);
                //Content list
                ArrayList<AboutUsParaModel> paraArrayList = subHeaderModel.getParaList();
                for (int j = 0; j < paraArrayList.size(); j++) {
                    Typeface subHeadingsTF = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicro Pro Bold Italic.otf");
                    Typeface descTF = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Regular.otf");
                    LayoutInflater inflater2 = LayoutInflater.from(getActivity());
                    View subHeaderView = inflater2.inflate(R.layout.aboutus_header_txt, null);
                    subHeaderTxt = (TextView) subHeaderView.findViewById(R.id.subTitle);
                    subHeaderTxt.setTypeface(subHeadingsTF);
                    AboutUsParaModel paraModel = paraArrayList.get(j);
                    String subHeader = paraModel.getHeader();
                    String subContent = paraModel.getContent();
                    //Sub header text color as green for second main header
                    if (k == 2) {
                        subHeaderTxt.setTextColor(Color.parseColor("#8fb774"));
                    }

                    LayoutInflater inflater3 = LayoutInflater.from(getActivity());
                    View descView = inflater3.inflate(R.layout.aboutus_desc_txt, null);
                    descTxt = (TextView) descView.findViewById(R.id.aboutDesc);
                    descTxt.setTypeface(descTF);
                    //Checked sub header is present or not
                    if (subHeader.length() > 0) {
                        subHeaderTxt.setText(subHeader);
                        layForLienarSub.addView(subHeaderView);
                        descTxt.setText(subContent);
                        layForLienarSub.addView(descView);
                    } else {
                        subHeaderTxt.setVisibility(View.GONE);
                        descTxt.setText(subContent);
                        layForLienarSub.addView(descView);
                    }
                }
            }

            layForLinear.addView(viewForLay);
        }
    }
}
