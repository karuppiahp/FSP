package activities.mswift.info.walaapp.wala.model;

/**
 * Created by karuppiah on 12/3/2015.
 */
public class CountryModel {

    private String countryId, countryName, countryCode;

    public void setId(String countryId) {
        this.countryId = countryId;
    }

    public void setName(String countryName) {
        this.countryName = countryName;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getId() {
        return countryId;
    }

    public String getName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
