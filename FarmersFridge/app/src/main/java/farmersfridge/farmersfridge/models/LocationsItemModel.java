package farmersfridge.farmersfridge.models;

/**
 * Created by karuppiah on 8/5/2016.
 */
public class LocationsItemModel {

    private String id;
    private String prettyName;
    private String address;
    private String hours;
    private String status;
    private String latitude;
    private String longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrettyName(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getHours() {
        return hours;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setLat(String latitude) {
        this.latitude = latitude;
    }

    public String getLat() {
        return latitude;
    }

    public void setLong(String longitude) {
        this.longitude = longitude;
    }

    public String getLong() {
        return longitude;
    }
}
