package farmersfridge.farmersfridge.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.LocationsMenuTask;
import farmersfridge.farmersfridge.map.MapMain;
import farmersfridge.farmersfridge.models.LocationsItemModel;
import farmersfridge.farmersfridge.models.LocationsModel;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 8/10/2016.
 */

/*
 * Adapter for Locations listview it contains
 * Locations item title
 * Miles away text
 */
public class LocationsListAdapter extends RecyclerView.Adapter<LocationsListAdapter.locationsViewHolder> {
    private List<LocationsItemModel> locationsList;
    private Context mContext;
    private FragmentManager fragmentManager;

    public LocationsListAdapter(Context context, List<LocationsItemModel> locationsList, FragmentManager fragmentManager) {
        this.locationsList = locationsList;
        this.mContext = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public locationsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //View initialize
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.locations_list_item, viewGroup, false);
        return new locationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final locationsViewHolder holder, int i) {
        Typeface titleTf = Typeface.createFromAsset(mContext.getAssets(), "fonts/GrottoIronic-SemiBold.otf");
        Typeface milesTf = Typeface.createFromAsset(mContext.getAssets(), "fonts/GarageFonts - FreightMicroProSemibold-Italic.otf");
        holder.title.setTypeface(titleTf);
        holder.description.setTypeface(milesTf);
        final LocationsItemModel locations = locationsList.get(i);
        holder.title.setText(locations.getId()); //tittle of listview item
        holder.description.setText(MapMain.locationsMilesArray.get(i).getMiles() + " Miles Away"); //Miles away text
        //background of view items updated with white and white bar image.
        if (i % 2 == 0) {
            holder.layForLocationsItmes.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.layForLocationsItmes.setBackgroundResource(R.drawable.deals_white_bar);
        }

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //API for single location menu is called.
                Constants.LOC_HEADER = holder.title.getText().toString();
                SessionStores.LOC_MODEL = new LocationsModel(holder.title.getText().toString());
                new LocationsMenuTask(mContext, SessionStores.LOC_MODEL.locationsMenu(), fragmentManager);
            }
        });
    }

    public int getItemCount() {
        return locationsList.size();
    }


    public static class locationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView title;
        protected TextView description;
        protected NetworkImageView imageview;
        protected RelativeLayout layForLocationsItmes;


        public locationsViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.textForLocTitle);
            description = (TextView) v.findViewById(R.id.textForLocDesc);
            layForLocationsItmes = (RelativeLayout) v.findViewById(R.id.layForLocationsItems);
        }

        @Override
        public void onClick(View v) {
            Context c = v.getContext();

        }
    }
}
