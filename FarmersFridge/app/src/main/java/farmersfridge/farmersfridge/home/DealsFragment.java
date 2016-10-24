package farmersfridge.farmersfridge.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmersfridge.farmersfridge.R;

/**
 * Created by karuppiah on 7/12/2016.
 */
public class DealsFragment extends Fragment {

    @BindView(R.id.txtForHHMain)
    TextView txtForHH;
    @BindView(R.id.txtForHHMainSub)
    TextView txtForHHSub;
    @BindView(R.id.txtForOffMain)
    TextView txtForOff;
    @BindView(R.id.txtForOffMainSub)
    TextView txtForOffSub;
    @BindView(R.id.txtForBonusMain)
    TextView txtForBonus;
    @BindView(R.id.txtForBonusMainSub)
    TextView txtForBonusSub;
    @BindView(R.id.imgForArrowHelp)
    ImageView imgForArrowRight;
    @BindView(R.id.imgForArrowHelpLeft)
    ImageView imgForArrowLeft;
    @BindView(R.id.layForHelpContent)
    LinearLayout layForHHMain;
    @BindView(R.id.layForHelpDetail)
    LinearLayout layForHHDetail;
    @BindView(R.id.imgForArrowOff)
    ImageView imgForArrowOffRight;
    @BindView(R.id.imgForArrowOffLeft)
    ImageView imgForArrowOffLeft;
    @BindView(R.id.layForOffContent)
    LinearLayout layForOffMain;
    @BindView(R.id.layForOffDetail)
    LinearLayout layForOffDetail;
    @BindView(R.id.txtForHHHeader)
    TextView txtForHHHeadr;
    @BindView(R.id.txtForHHHeaderSub)
    TextView txtForHHHeaderSub;
    @BindView(R.id.txtForHHDesc)
    TextView txtForHHDesc;
    @BindView(R.id.txtForOffSub)
    TextView txtForOffSubHead;
    @BindView(R.id.txtForOffSubTxt)
    TextView txtForOffSubTxt;
    @BindView(R.id.txtForOffQr)
    TextView txtForOffQr;
    @BindView(R.id.txtForOffQrCode)
    TextView txtForOffQrCode;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        View v = inflater.inflate(R.layout.deals, container, false);
        ButterKnife.bind(this, v);

        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicro Pro Black Italic.otf");
        Typeface desc = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Light.otf");
        Typeface font3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Bold.otf");
        Typeface font4 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-SemiBold.otf");
        Typeface font5 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Regular.otf");
        Typeface fontHHHeader = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-MediumExpanded.otf");
        Typeface fontHHSub = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-RegularExpanded.otf");
        Typeface fontOffSubHead = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-SemiBold.otf");
        txtForHH.setTypeface(font1);
        txtForHHSub.setTypeface(desc);
        txtForOff.setTypeface(font3);
        txtForOffSub.setTypeface(font3);
        txtForBonus.setTypeface(font3);
        txtForBonusSub.setTypeface(font5);
        txtForHHHeadr.setTypeface(fontHHHeader);
        txtForHHHeaderSub.setTypeface(fontHHSub);
        txtForOffSubHead.setTypeface(fontOffSubHead);
        txtForOffSubTxt.setTypeface(font4);
        txtForOffQr.setTypeface(font4);
        txtForOffQrCode.setTypeface(font4);
        txtForHHDesc.setTypeface(desc);

        //Right arrow click listener for Helping Hand Detail view
        imgForArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgForArrowRight.setVisibility(View.GONE);
                layForHHMain.setVisibility(View.GONE);
                imgForArrowLeft.setVisibility(View.VISIBLE);
                layForHHDetail.setVisibility(View.VISIBLE);
            }
        });

        //Left arrow click listener for Helping Hand Short view
        imgForArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgForArrowRight.setVisibility(View.VISIBLE);
                layForHHMain.setVisibility(View.VISIBLE);
                imgForArrowLeft.setVisibility(View.GONE);
                layForHHDetail.setVisibility(View.GONE);
            }
        });

        //Right arrow click listener for Offer Detail view
        imgForArrowOffRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgForArrowOffRight.setVisibility(View.GONE);
                layForOffMain.setVisibility(View.GONE);
                imgForArrowOffLeft.setVisibility(View.VISIBLE);
                layForOffDetail.setVisibility(View.VISIBLE);
            }
        });

        //Left arrow click listener for Offer Short view
        imgForArrowOffLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgForArrowOffRight.setVisibility(View.VISIBLE);
                layForOffMain.setVisibility(View.VISIBLE);
                imgForArrowOffLeft.setVisibility(View.GONE);
                layForOffDetail.setVisibility(View.GONE);
            }
        });
        return v;
    }
}
