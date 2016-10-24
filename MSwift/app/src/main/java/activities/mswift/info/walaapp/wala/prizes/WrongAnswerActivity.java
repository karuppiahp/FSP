package activities.mswift.info.walaapp.wala.prizes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.model.QuizQuestionModel;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 3/5/2016.
 */
public class WrongAnswerActivity extends Fragment {

    private View v;
    private TextView txtForQandA;
    private String ques = "", ans = "";
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ((TabHostFragments) getActivity()).tabColorMerun();

        v = inflater.inflate(R.layout.wrong_answer, container, false);
        txtForQandA = (TextView) v.findViewById(R.id.wronganswerresp);

        if (Constants.QUES_ARRAY.size() > 0) {
            QuizQuestionModel quesModel = Constants.QUES_ARRAY.get(0);
            ques = quesModel.getQues();
            ans = quesModel.getCorrectAns();

            SessionStores.setQuizQues(ques, getActivity());
            SessionStores.setQuizAns(ans, getActivity());
        } else {
            ques = SessionStores.getQuizQues(getActivity());
            ans = SessionStores.getQuizAns(getActivity());
        }

        String quesTxt = "<font>" + ques + "</font>";
        String ansTxt = "<font color=" + getResources().getColor(R.color.black) + ">" + ans + "</font>";

        txtForQandA.setText(Html.fromHtml(quesTxt + " " + ansTxt));

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // mixpanel screen tracking
            mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
            mixpanelAPI.track(getString(R.string.mixpanel_games_incorrect_answer));
        }
    }
}
