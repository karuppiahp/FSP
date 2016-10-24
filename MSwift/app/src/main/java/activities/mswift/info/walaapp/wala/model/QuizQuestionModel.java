package activities.mswift.info.walaapp.wala.model;

/**
 * Created by karuppiah on 3/3/2016.
 */
public class QuizQuestionModel {

    private String quesId, ques, correctAns, wrongAns1, wrongAns2, tips;

    public void setQues(String ques) {
        this.ques = ques;
    }

    public void setQuesId(String quesId) {
        this.quesId = quesId;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }

    public void setWrongAns1(String wrongAns1) {
        this.wrongAns1 = wrongAns1;
    }

    public void setWrongAns2(String wrongAns2) {
        this.wrongAns2 = wrongAns2;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getQues() {
        return ques;
    }

    public String getQuesId() {
        return quesId;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public String getWrongAns1() {
        return wrongAns1;
    }

    public String getWrongAns2() {
        return wrongAns2;
    }

    public String getTips() {
        return tips;
    }
}
