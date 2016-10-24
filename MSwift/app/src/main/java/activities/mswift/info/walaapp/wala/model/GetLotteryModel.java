package activities.mswift.info.walaapp.wala.model;

/**
 * Created by karuppiah on 4/6/2016.
 */
public class GetLotteryModel {

    private String id, points, tcktsCount, pointsLeft, participants;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getPoints() {
        return points;
    }

    public void setTcktsCount(String tcktsCount) {
        this.tcktsCount = tcktsCount;
    }

    public String getTcktsCount() {
        return tcktsCount;
    }

    public void setPointsLeft(String pointsLeft) {
        this.pointsLeft = pointsLeft;
    }

    public String getPointsLeft() {
        return pointsLeft;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getParticipants() {
        return participants;
    }
}
