package farmersfridge.farmersfridge.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.DeleteFavLocationTask;
import farmersfridge.farmersfridge.asynctasks.UpdateFavLocationTask;
import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.map.LocationsMenuDetail;
import farmersfridge.farmersfridge.models.MenuItemModel;
import farmersfridge.farmersfridge.models.MenuModel;
import farmersfridge.farmersfridge.support.CustomVolleyRequest;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 8/12/2016.
 */
/*
 * Adapter class is for Locations Menu item detail contains
 * Category title
 * Vertical scroll items
 * Favourite icons
 */
public class LocationsMenuDetailAdapter extends RecyclerView.Adapter<LocationsMenuDetailAdapter.ContactViewHolder> {

    private List<MenuItemModel> menuList;
    private ImageLoader imageLoader;
    private Context context;
    private static Boolean isFavourite;
    public static String favName;

    public LocationsMenuDetailAdapter(Context context, List<MenuItemModel> menuList) {
        this.menuList = menuList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, int i) {
        Typeface titleFnt = Typeface.createFromAsset(context.getAssets(), "fonts/GarageFonts - FreightMicro Pro Bold Italic.otf");
        contactViewHolder.title.setTypeface(titleFnt);
        final MenuItemModel menuItemModel = menuList.get(i);
        contactViewHolder.title.setText(menuItemModel.getTitle());

        //tags set for get position
        contactViewHolder.menu_image.setTag(i);
        contactViewHolder.favourite_image.setTag(i);

        //Imageview loads using volley custom request
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(menuItemModel.getImageUrl(), ImageLoader.getImageListener(contactViewHolder.menu_image, R.drawable.placeholder, R.drawable.placeholder));
        contactViewHolder.menu_image.setImageUrl(menuItemModel.getImageUrl(), imageLoader);

        //Condition checked for favourite selected or not selected.
        if (new SessionStores(context).getLoginStatus() != null && new SessionStores(context).getAccessToken() != null) {
            isFavourite = menuItemModel.isFavourite();
            if (isFavourite) {
                contactViewHolder.favourite_image.setImageResource(R.drawable.favorite_selected);
            } else {
                contactViewHolder.favourite_image.setImageResource(R.drawable.favorite_unselected);
            }
        } else {
            contactViewHolder.favourite_image.setImageResource(R.drawable.favorite_unselected);
        }

        if (i % 2 != 0) {
            contactViewHolder.layForMenuImg.setBackgroundResource(R.drawable.menu_brown_bg);
        } else {
            contactViewHolder.layForMenuImg.setBackgroundResource(R.drawable.menu_green_bg);
        }

        //favourite button click listener
        contactViewHolder.favourite_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks if user logged in or not
                if (new SessionStores(context).getLoginStatus() != null && new SessionStores(context).getAccessToken() != null) {
                    contactViewHolder.favourite_image.setClickable(false);
                    contactViewHolder.favourite_image.setEnabled(false);
                    contactViewHolder.menu_image.setClickable(false);
                    contactViewHolder.menu_image.setEnabled(false);
                    int position = (Integer) v.getTag(); // position get from tag
                    MenuItemModel menuItemModel = menuList.get(position);
                    isFavourite = menuItemModel.isFavourite();
                    favName = menuItemModel.getTitle();
                    //According to favourite condition the api calls
                    if (isFavourite) { //if fav is selected delete api calls
                        SessionStores.MENU_MODEL = new MenuModel(menuItemModel.getTitle());
                        new DeleteFavLocationTask(context, SessionStores.MENU_MODEL.deleteFavourites(), v, contactViewHolder.menu_image, menuList, position, LocationsMenuDetail.arrayPos, favName, "detail");
                    } else { // else update api calls
                        SessionStores.MENU_MODEL = new MenuModel(menuItemModel.getTitle());
                        new UpdateFavLocationTask(context, SessionStores.MENU_MODEL.updateFavourites(), v, contactViewHolder.menu_image, menuList, position, LocationsMenuDetail.arrayPos, favName, "detail");
                    }
                } else {
                    Utils.ShowAlert(context, "Please login to favourite this item");
                }
            }
        });

        //Menu image click listener for detail info screen
        contactViewHolder.menu_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationsMenuDetail.layForInfo.setVisibility(View.VISIBLE);
                int position = (Integer) v.getTag(); // position get from tag
                Constants.INFO_ITEM_POS = position;
                MenuItemModel menuItemModel = menuList.get(position); // details fetch from MenuItemModel
                String header = menuItemModel.getTitle();
                String price = menuItemModel.getPrice();
                String desc = menuItemModel.getDescription();
                LocationsMenuDetail.txtForHeader.setText(header);
                LocationsMenuDetail.txtForPts.setText(price);
                LocationsMenuDetail.txtForSubTitle.setText(header);
                LocationsMenuDetail.txtForDesc.setText(desc);
                //imageview for chart is loaded
                imageLoader.get(menuItemModel.getInfoPath(), ImageLoader.getImageListener(LocationsMenuDetail.imgForChart, R.drawable.placeholder, R.drawable.placeholder));
                LocationsMenuDetail.imgForChart.setImageUrl(menuItemModel.getInfoPath(), imageLoader);
                boolean isFavourite = menuItemModel.isFavourite();
                if (new SessionStores(context).getLoginStatus() != null && new SessionStores(context).getAccessToken() != null) {
                    if (isFavourite) {
                        LocationsMenuDetail.infoFavPresent = true;
                        LocationsMenuDetail.imgForInfoFav.setImageResource(R.drawable.fav_selected_white_bg);
                    } else {
                        LocationsMenuDetail.infoFavPresent = false;
                        LocationsMenuDetail.imgForInfoFav.setImageResource(R.drawable.fav_unselected_white_bg);
                    }
                } else {
                    LocationsMenuDetail.imgForInfoFav.setImageResource(R.drawable.fav_unselected_white_bg);
                }
            }
        });
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //View initialized
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.menu_detail_item, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView title;
        protected TextView url;
        protected NetworkImageView menu_image;
        protected ImageView favourite_image;
        private RelativeLayout layForMenuImg;

        public ContactViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.txtForItemName);
            menu_image = (NetworkImageView) v.findViewById(R.id.imgForDish);
            favourite_image = (ImageView) v.findViewById(R.id.imgForFav);
            layForMenuImg = (RelativeLayout) v.findViewById(R.id.layForMenuImg);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_splash);
            dialog.show();
        }
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
