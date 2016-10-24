package farmersfridge.farmersfridge.adapter;

import android.content.Context;
import android.graphics.Typeface;
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

import java.util.List;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.DeleteFavMyFridgeTask;
import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.models.FavApiModel;
import farmersfridge.farmersfridge.models.MyFridgeModelRec;
import farmersfridge.farmersfridge.my_fridge.MyFridgeList;
import farmersfridge.farmersfridge.support.CustomVolleyRequest;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 7/26/2016.
 */
public class FavouriteMenuAdapter extends RecyclerView.Adapter<FavouriteMenuAdapter.ContactViewHolder> {

    private ImageLoader imageLoader;
    private Context context;
    private Boolean isFavourite;
    List<MyFridgeModelRec> favList;

    public FavouriteMenuAdapter(Context context) {
        this.context = context;
        favList = Constants.favArray;
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, final int position) {
        Typeface font2 = Typeface.createFromAsset(context.getAssets(), "fonts/GrottoIronic-Bold.otf");
        Typeface font4 = Typeface.createFromAsset(context.getAssets(), "fonts/GarageFonts - FreightMicro Pro Bold Italic.otf");
        final RecyclerView.Adapter adapter = this;

        contactViewHolder.header.setTypeface(font2);
        contactViewHolder.title.setTypeface(font4);
        contactViewHolder.title.setText(favList.get(position).getVendName());

        //Image url for menu image
        String image_url = "http://d3uz0b82u39oll.cloudfront.net/" + favList.get(position).getIconPath() + "/" + favList.get(position).getIconName();
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(image_url, ImageLoader.getImageListener(contactViewHolder.imageview, R.drawable.placeholder, R.drawable.placeholder));
        contactViewHolder.imageview.setImageUrl(image_url, imageLoader);
        contactViewHolder.favorite.setImageResource(R.drawable.favorite_selected);

        contactViewHolder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to avoid multiple clicks on favourite icon the click is disabled
                contactViewHolder.favorite.setClickable(false);
                contactViewHolder.favorite.setEnabled(false);
                contactViewHolder.imageview.setClickable(false);
                contactViewHolder.imageview.setEnabled(false);
                String favName = favList.get(position).getVendName();
                //Delete fav api is called
                SessionStores.FAV_MODEL = new FavApiModel(favName);
                new DeleteFavMyFridgeTask(context, SessionStores.FAV_MODEL.deleteFavourites(), adapter, favName, position);
            }
        });

        contactViewHolder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Move to list fragment of favourite items
                FragmentMain fragmentMain = (FragmentMain) context;
                FragmentManager fragmentManager = fragmentMain.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MyFridgeList fragment = new MyFridgeList();
                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.my_fridge_fav, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView header;
        private NetworkImageView imageview;
        private ImageView favorite;

        public ContactViewHolder(View v) {
            super(v);
            imageview = (NetworkImageView) v.findViewById(R.id.imgForDish);
            title = (TextView) v.findViewById(R.id.txtForItemName);
            header = (TextView) v.findViewById(R.id.txtForHeader);
            favorite = (ImageView) v.findViewById(R.id.imgForFav);
        }
    }
}
