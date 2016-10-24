package activities.mswift.info.walaapp.wala.model;

/**
 * Created by karuppiah on 4/26/2016.
 */
public class CurrentGoalModel {

    private String mode, name, days_left, type, current_value, goal_value;

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDaysLeft(String days_left) {
        this.days_left = days_left;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCurrentValue(String current_value) {
        this.current_value = current_value;
    }

    public void setGoalValue(String goal_value) {
        this.goal_value = goal_value;
    }

    public String getMode() {
        return mode;
    }

    public String getName() {
        return name;
    }

    public String getDaysLeft() {
        return days_left;
    }

    public String getType() {
        return type;
    }

    public String getCurrentValue() {
        return current_value;
    }

    public String getGoalValue() {
        return goal_value;
    }
}
