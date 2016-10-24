package farmersfridge.farmersfridge.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kruno on 10.06.16..
 */
public class MenuItemModel implements Parcelable {

    private String title;
    private String imageUrl;
    private String description;
    private String description_long;
    private String infoPath;
    private String category;
    private String price;
    private boolean isFavourite;

    public MenuItemModel(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        description_long = in.readString();
        infoPath = in.readString();
        category = in.readString();
        price = in.readString();
        isFavourite = in.readByte() != 0;
    }

    public MenuItemModel() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(description);
        dest.writeString(description_long);
        dest.writeString(infoPath);
        dest.writeString(category);
        dest.writeString(price);
        dest.writeByte((byte) (isFavourite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuItemModel> CREATOR = new Creator<MenuItemModel>() {
        @Override
        public MenuItemModel createFromParcel(Parcel in) {
            return new MenuItemModel(in);
        }

        @Override
        public MenuItemModel[] newArray(int size) {
            return new MenuItemModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getInfoPath() {
        return infoPath;
    }

    public void setInfoPath(String infoPath) {
        this.infoPath = infoPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription_long() {
        return description_long;
    }

    public void setDescription_long(String description_long) {
        this.description_long = description_long;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
