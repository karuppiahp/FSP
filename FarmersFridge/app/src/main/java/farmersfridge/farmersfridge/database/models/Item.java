package farmersfridge.farmersfridge.database.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by karuppiah on 6/11/2016.
 */
@Table(name = "Items")
public class Item extends Model {
    // This is the unique id given by the server
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;
    // This is a regular field
    @Column(name = "Name")
    public String name;
    @Column(name = "Link")
    public String link;

    // Make sure to have a default constructor for every ActiveAndroid model
    public Item(){
        super();
    }

    public Item(int remoteId, String name, String link){
        super();
        this.remoteId = remoteId;
        this.name = name;
        this.link = link;
    }

    //List all items from Items table
    public static List<Item> getRowCount() {
        return new Select().from(Item.class).execute();
    }
}
