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

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.DeleteFavMyFridgeTask;
import farmersfridge.farmersfridge.models.FavApiModel;
import farmersfridge.farmersfridge.my_fridge.MyFridgeList;
import farmersfridge.farmersfridge.support.CustomVolleyRequest;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 8/18/2016.
 */
public class FavouriteMenuListAdapter extends RecyclerView.Adapter<FavouriteMenuListAdapter.ContactViewHolder> {

    private ImageLoader imageLoader;
    private Context context;
    private static Boolean isFavourite;
    public static String favName;

    public FavouriteMenuListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return Constants.favArray.size();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, final int i) {
        Typeface titleFnt = Typeface.createFromAsset(context.getAssets(), "fonts/GarageFonts - FreightMicro Pro Medium Italic.otf");
        final RecyclerView.Adapter adapter = this;
        contactViewHolder.title.setTypeface(titleFnt);
        contactViewHolder.title.setText(Constants.favArray.get(i).getVendName());
        contactViewHolder.menu_image.setTag(i);
        contactViewHolder.favourite_image.setTag(i);
        //Image url for menu image
        String image_url = "http://d3uz0b82u39oll.cloudfront.net/" + Constants.favArray.get(i).getIconPath() + "/" + Constants.favArray.get(i).getIconName();
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(image_url, ImageLoader.getImageListener(contactViewHolder.menu_image, R.drawable.placeholder, R.drawable.placeholder));
        contactViewHolder.menu_image.setImageUrl(image_url, imageLoader);
        contactViewHolder.favourite_image.setImageResource(R.drawable.favorite_selected);

        //Background for menu images has been updated as brown and green bg, for some images the background doesn't present
        if (i % 2 != 0) {
            contactViewHolder.layForMenuImg.setBackgroundResource(R.drawable.menu_brown_bg);
        } else {
            contactViewHolder.layForMenuImg.setBackgroundResource(R.drawable.menu_green_bg);
        }

        contactViewHolder.favourite_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactViewHolder.favourite_image.setClickable(false);
                contactViewHolder.favourite_image.setEnabled(false);
                contactViewHolder.menu_image.setClickable(false);
                contactViewHolder.menu_image.setEnabled(false);
                int position = (Integer) v.getTag();
                //Delete fav api is called
                SessionStores.FAV_MODEL = new FavApiModel(Constants.favArray.get(i).getVendName());
                new DeleteFavMyFridgeTask(context, SessionStores.FAV_MODEL.deleteFavourites(), adapter, Constants.favArray.get(i).getVendName(), i);
            }
        });

        contactViewHolder.menu_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Menu info screen values are set
                MyFridgeList.layForInfo.setVisibility(View.VISIBLE);
                Constants.MYFRIDGE_INFO_FAV_POS = (Integer) v.getTag();
                String header = Constants.favArray.get(i).getVendName();
                String price = Constants.favArray.get(i).getPrice();
                String desc = Constants.favArray.get(i).getVendName() + " " + Constants.favArray.get(i).getVendName();
                MyFridgeList.txtForHeader.setText(header);
                MyFridgeList.txtForPts.setText(price);
                MyFridgeList.txtForSubTitle.setText(header);
                MyFridgeList.txtForDesc.setText(desc);
                //Image url for menu info image
                String image_url = "http://d3uz0b82u39oll.cloudfront.net/" + Constants.favArray.get(i).getNutritionPath() + "/" + Constants.favArray.get(i).getNutritionName();
                imageLoader.get(image_url, ImageLoader.getImageListener(MyFridgeList.imgForChart, R.drawable.placeholder, R.drawable.placeholder));
                MyFridgeList.imgForChart.setImageUrl(image_url, imageLoader);
                MyFridgeList.imgForInfoFav.setImageResource(R.drawable.fav_selected_white_bg);
            }
        });
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
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

    public static void favImgUpdated(View v) {
        if (isFavourite) {
            isFavourite = false;
            ImageView imageView = (ImageView) v;
            imageView.setImageResource(R.drawable.favorite_unselected);
        } else {
            isFavourite = true;
            ImageView imageView = (ImageView) v;
            imageView.setImageResource(R.drawable.favorite_selected);
        }
    }
}
