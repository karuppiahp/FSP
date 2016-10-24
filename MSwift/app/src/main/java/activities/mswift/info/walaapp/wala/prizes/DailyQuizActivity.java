package activities.mswift.info.walaapp.wala.prizes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.UpdateAnswerTask;
import activities.mswift.info.walaapp.wala.model.QuizQuestionModel;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 12/28/2015.
 */
public class DailyQuizActivity extends Fragment {

    private View v;
    private TextView txtForQues, txtForAnsA, txtForAnsB, txtForAnsC;
    private RelativeLayout layForOptionA, layForOptionB, layForOptionC;
    private QuizQuestionModel quesModel;
    Integer[] options = new Integer[]{1, 2, 3};
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ((TabHostFragments) getActivity()).tabColorMerun();

        v = inflater.inflate(R.layout.daily_quiz, container, false);
        txtForQues = (TextView) v.findViewById(R.id.dailyquizquestion);
        txtForAnsA = (TextView) v.findViewById(R.id.txtForOptionA);
        txtForAnsB = (TextView) v.findViewById(R.id.txtForOptionB);
        txtForAnsC = (TextView) v.findViewById(R.id.txtForOptionC);
        layForOptionA = (RelativeLayout) v.findViewById(R.id.layForOptionA);
        layForOptionB = (RelativeLayout) v.findViewById(R.id.layForOptionB);
        layForOptionC = (RelativeLayout) v.findViewById(R.id.layForOptionC);

        List<Integer> listOptions = Arrays.asList(options);
        // Shuffling list elements
        Collections.shuffle(listOptions);

        quesModel = Constants.QUES_ARRAY.get(0);
        txtForQues.setText(quesModel.getQues());

        for (int i = 0; i < listOptions.size(); i++) {
            if (listOptions.get(0) == 1) {
                txtForAnsA.setText(quesModel.getCorrectAns());
                txtForAnsB.setText(quesModel.getWrongAns1());
                txtForAnsC.setText(quesModel.getWrongAns2());
                break;
            }

            if (listOptions.get(1) == 1) {
                txtForAnsB.setText(quesModel.getCorrectAns());
                txtForAnsA.setText(quesModel.getWrongAns1());
                txtForAnsC.setText(quesModel.getWrongAns2());
                break;
            }

            if (listOptions.get(2) == 1) {
                txtForAnsC.setText(quesModel.getCorrectAns());
                txtForAnsA.setText(quesModel.getWrongAns1());
                txtForAnsB.setText(quesModel.getWrongAns2());
                break;
            }
        }

        layForOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quesModel.getCorrectAns().equals(txtForAnsA.getText().toString())) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_email", SessionStores.getUserEmail(getActivity()));
                    params.put("question_id", quesModel.getQuesId());
                    params.put("answer", "1");
                    new UpdateAnswerTask(getActivity(), params, "1");
                } else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_email", SessionStores.getUserEmail(getActivity()));
                    params.put("question_id", quesModel.getQuesId());
                    params.put("answer", "0");
                    new UpdateAnswerTask(getActivity(), params, "0");
                }
            }
        });

        layForOptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quesModel.getCorrectAns().equals(txtForAnsB.getText().toString())) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_email", SessionStores.getUserEmail(getActivity()));
                    params.put("question_id", quesModel.getQuesId());
                    params.put("answer", "1");
                    new UpdateAnswerTask(getActivity(), params, "1");
                } else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_email", SessionStores.getUserEmail(getActivity()));
                    params.put("question_id", quesModel.getQuesId());
                    params.put("answer", "0");
                    new UpdateAnswerTask(getActivity(), params, "0");
                }
            }
        });

        layForOptionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quesModel.getCorrectAns().equals(txtForAnsC.getText().toString())) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_email", SessionStores.getUserEmail(getActivity()));
                    params.put("question_id", quesModel.getQuesId());
                    params.put("answer", "1");
                    new UpdateAnswerTask(getActivity(), params, "1");
                } else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_email", SessionStores.getUserEmail(getActivity()));
                    params.put("question_id", quesModel.getQuesId());
                    params.put("answer", "0");
                    new UpdateAnswerTask(getActivity(), params, "0");
                }
            }
        });

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // mixpanel screen tracking
            mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
            mixpanelAPI.track(getString(R.string.mixpanel_games_daily_quiz));
        }
    }
}
