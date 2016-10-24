package farmersfridge.farmersfridge.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.DeleteFavLocationTask;
import farmersfridge.farmersfridge.asynctasks.UpdateFavLocationTask;
import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.map.LocationsMenuDetail;
import farmersfridge.farmersfridge.models.MenuItemModel;
import farmersfridge.farmersfridge.models.MenuModel;
import farmersfridge.farmersfridge.support.CustomVolleyRequest;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 8/25/2016.
 */
/*
 * Adapter class is for Locations Menu items horizontal contains
 * Item name
 * Horizontal scroll items
 * Menu image
 * Favourite icons
 */
public class LocationsMenuHoriztlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MenuItemModel> mDataList;
    private ImageLoader imageLoader;
    private Context context;
    private int mRowIndex = -1;
    public static String favName;

    public LocationsMenuHoriztlAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MenuItemModel> data) {
        if (mDataList != data) {
            mDataList = data;
            notifyDataSetChanged();
        }
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView text;
        private TextView textForItemName;
        private NetworkImageView item_image;
        private ImageView imgForFav;

        public ItemViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.txtForHeader);
            textForItemName = (TextView) itemView.findViewById(R.id.txtForItemName);
            item_image = (NetworkImageView) itemView.findViewById(R.id.imgForDish);
            imgForFav = (ImageView) itemView.findViewById(R.id.imgForFav);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View initialized
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.menu_items, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, final int position) {
        final ItemViewHolder holder = (ItemViewHolder) rawHolder;

        final MenuItemModel menuItemModel = mDataList.get(position);

        Typeface titleFnt = Typeface.createFromAsset(context.getAssets(), "fonts/GarageFonts - FreightMicro Pro Bold Italic.otf");
        holder.textForItemName.setTypeface(titleFnt);
        holder.textForItemName.setText(menuItemModel.getTitle());

        //Imageview loads using volley custom request
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(menuItemModel.getImageUrl(), ImageLoader.getImageListener(holder.item_image, R.drawable.placeholder, R.drawable.placeholder));
        holder.item_image.setImageUrl(menuItemModel.getImageUrl(), imageLoader);

        //tags set for get position
        holder.itemView.setTag(position);
        holder.imgForFav.setTag(position);
        holder.imgForFav.setId(position);
        holder.item_image.setTag(position);

        //Condition checked for favourite selected or not selected.
        if (new SessionStores(context).getLoginStatus() != null && new SessionStores(context).getAccessToken() != null) {
            if (Favourites.isFavPresent(menuItemModel.getTitle()).size() > 0) {
                holder.imgForFav.setImageResource(R.drawable.favorite_selected);
            } else {
                holder.imgForFav.setImageResource(R.drawable.favorite_unselected);
            }
        } else {
            holder.imgForFav.setImageResource(R.drawable.favorite_unselected);
        }

        //Menu image click listener for detail screen
        holder.item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag(); // position get from tag
                FragmentMain fragmentMain = (FragmentMain) context;
                FragmentManager fragmentManager = fragmentMain.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LocationsMenuDetail fragment = new LocationsMenuDetail();
                //Category and position send in bundle to detail screen
                Bundle bundle = new Bundle();
                bundle.putInt("menuItem", position);
                bundle.putInt("categoryPos", position);
                bundle.putString("category", menuItemModel.getCategory());
                bundle.putParcelableArrayList("arrayList", (ArrayList<? extends Parcelable>) mDataList);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.realtabcontent, fragment); //Fragment replaced to detail screen
                fragmentTransaction.commit();
            }
        });

        //favourite button click listener
        holder.imgForFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks if user logged in or not
                if (new SessionStores(context).getLoginStatus() != null && new SessionStores(context).getAccessToken() != null) {
                    holder.imgForFav.setClickable(false);
                    holder.imgForFav.setEnabled(false);
                    holder.item_image.setClickable(false);
                    holder.item_image.setEnabled(false);
                    favName = menuItemModel.getTitle();
                    int arrayPosition = v.getId();
                    int position = (Integer) v.getTag(); // position get from tag
                    List<MenuItemModel> menuList = Constants.locMenuArray.get(arrayPosition);
                    //According to favourite condition the api calls
                    if (Favourites.isFavPresent(favName).size() > 0) { //if fav is selected delete api calls
                        SessionStores.MENU_MODEL = new MenuModel(favName);
                        new DeleteFavLocationTask(context, SessionStores.MENU_MODEL.deleteFavourites(), v, holder.item_image, menuList, position, arrayPosition, favName, "list");
                    } else { // else update api calls
                        SessionStores.MENU_MODEL = new MenuModel(favName);
                        new UpdateFavLocationTask(context, SessionStores.MENU_MODEL.updateFavourites(), v, holder.item_image, menuList, position, arrayPosition, favName, "list");
                    }
                } else {
                    Utils.ShowAlert(context, "Please login to favourite this item");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /*
     * Favourite icon is updated after the api call is finished
     */
    public static void favImgUpdated(View v, View imageV, String name) {
        v.setClickable(true);
        v.setEnabled(true);
        imageV.setClickable(true);
        imageV.setEnabled(true);
        if (Favourites.isFavPresent(name).size() > 0) {
            ImageView imageView = (ImageView) v;
            imageView.setImageResource(R.drawable.favorite_unselected);
        } else {
            ImageView imageView = (ImageView) v;
            imageView.setImageResource(R.drawable.favorite_selected);
        }
    }
}
