package farmersfridge.farmersfridge.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import farmersfridge.farmersfridge.asynctasks.DeleteFavouriteTask;
import farmersfridge.farmersfridge.asynctasks.UpdateFavouriteTask;
import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.database.models.MenuItem;
import farmersfridge.farmersfridge.menu.MenuDetail;
import farmersfridge.farmersfridge.models.MenuModel;
import farmersfridge.farmersfridge.support.CustomVolleyRequest;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by kruno on 15.06.16..
 */

/*
 * Adapter class is for Menu item detail contains
 * Category title
 * Vertical scroll items
 * Favourite icons
 */
public class MenuDetailAdapter extends RecyclerView.Adapter<MenuDetailAdapter.ContactViewHolder> {

    private List<MenuItem> menuList;
    private ImageLoader imageLoader;
    private Context context;
    public static String favName;

    public MenuDetailAdapter(Context context, List<MenuItem> menuList) {
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
        contactViewHolder.title.setText(menuList.get(i).vendName);

        //tags set for get position
        contactViewHolder.menu_image.setTag(i);
        contactViewHolder.favourite_image.setTag(i);

        //Imageview loads using volley custom request
        final String image_url = "http://d3uz0b82u39oll.cloudfront.net/" + menuList.get(i).iconPath +"/" + menuList.get(i).iconName;
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(image_url, ImageLoader.getImageListener(contactViewHolder.menu_image, R.drawable.placeholder, R.drawable.placeholder));
        contactViewHolder.menu_image.setImageUrl(image_url, imageLoader);

        //Condition checked for favourite selected or not selected.
        if (new SessionStores(context).getLoginStatus() != null && new SessionStores(context).getAccessToken() != null) {
            if (Favourites.isFavPresent(menuList.get(i).vendName).size() > 0) {
                contactViewHolder.favourite_image.setImageResource(R.drawable.favorite_selected);
            } else {
                contactViewHolder.favourite_image.setImageResource(R.drawable.favorite_unselected);
            }
        } else {
            contactViewHolder.favourite_image.setImageResource(R.drawable.favorite_unselected);
        }

        if(i%2 != 0) {
            contactViewHolder.layForMenuImg.setBackgroundResource(R.drawable.menu_brown_bg);
        } else {
            contactViewHolder.layForMenuImg.setBackgroundResource(R.drawable.menu_green_bg);
        }

        //favourite button click listener
        contactViewHolder.favourite_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks if user logged in or not
                if(new SessionStores(context).getLoginStatus() != null && new SessionStores(context).getAccessToken() != null) {
                    contactViewHolder.favourite_image.setClickable(false);
                    contactViewHolder.favourite_image.setEnabled(false);
                    contactViewHolder.menu_image.setClickable(false);
                    contactViewHolder.menu_image.setEnabled(false);
                    int position = (Integer) v.getTag();
                    String favName = menuList.get(position).vendName;
                    //According to favourite condition the api calls
                    if (Favourites.isFavPresent(favName).size() > 0) { //if fav is selected delete api calls
                        SessionStores.MENU_MODEL = new MenuModel(favName);
                        new DeleteFavouriteTask(context, SessionStores.MENU_MODEL.deleteFavourites(), v, contactViewHolder.menu_image, favName, "main");
                    } else { // else update api calls
                        SessionStores.MENU_MODEL = new MenuModel(favName);
                        new UpdateFavouriteTask(context, SessionStores.MENU_MODEL.updateFavourites(), v, contactViewHolder.menu_image, favName, "main");
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
                MenuDetail.layForInfo.setVisibility(View.VISIBLE);
                int position = (Integer) v.getTag(); // position get from tag
                String header = menuList.get(position).vendName;
                String price = menuList.get(position).price;
                String desc = menuList.get(position).vendName + " " + menuList.get(position).vendName;
                MenuDetail.txtForHeader.setText(header);
                MenuDetail.txtForPts.setText(price);
                MenuDetail.txtForSubTitle.setText(header);
                MenuDetail.txtForDesc.setText(desc);
                //imageview for chart is loaded
                String imageUrl = "http://d3uz0b82u39oll.cloudfront.net/" + menuList.get(position).nutritionPath +"/" + menuList.get(position).nutritionName;
                Log.e("imageUrl????",""+imageUrl);
                imageLoader.get(imageUrl, ImageLoader.getImageListener(MenuDetail.imgForChart, R.drawable.placeholder, R.drawable.placeholder));
                MenuDetail.imgForChart.setImageUrl(imageUrl, imageLoader);
                //Fav image icon updates
                if (new SessionStores(context).getLoginStatus() != null && new SessionStores(context).getAccessToken() != null) {
                    if (Favourites.isFavPresent(menuList.get(position).vendName).size() > 0) {
                        MenuDetail.infoFavPresent = true;
                        MenuDetail.imgForInfoFav.setImageResource(R.drawable.fav_selected_white_bg);
                    } else {
                        MenuDetail.infoFavPresent = false;
                        MenuDetail.imgForInfoFav.setImageResource(R.drawable.fav_unselected_white_bg);
                    }
                } else {
                    MenuDetail.imgForInfoFav.setImageResource(R.drawable.fav_unselected_white_bg);
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
            title =  (TextView) v.findViewById(R.id.txtForItemName);
            menu_image = (NetworkImageView)v.findViewById(R.id.imgForDish);
            favourite_image = (ImageView)v.findViewById(R.id.imgForFav);
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
        if (Favourites.isFavPresent(name).size() > 0){
            ImageView imageView = (ImageView) v;
            imageView.setImageResource(R.drawable.favorite_unselected);
        } else {
            ImageView imageView = (ImageView) v;
            imageView.setImageResource(R.drawable.favorite_selected);
        }
    }

}
