package farmersfridge.farmersfridge.database.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by karuppiah on 7/2/2016.
 */
@Table(name = "Favourites")
public class Favourites extends Model {
    // This is the unique id given by the server
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;
    // This is a regular field
    @Column(name = "favName")
    public String favName;

    // Make sure to have a default constructor for every ActiveAndroid model
    public Favourites(){
        super();
    }

    public Favourites(int remoteId, String favName) {
        super();
        this.remoteId = remoteId;
        this.favName = favName;
    }

    //List all items from Items table
    public static List<Favourites> getAllRow() {
        return new Select().from(Favourites.class).execute();
    }

    //Fetch single category from Items table
    public static List<Favourites> isFavPresent(String favName) {
        return new Select().from(Favourites.class).where("favName = ?", favName).execute();
    }
}
