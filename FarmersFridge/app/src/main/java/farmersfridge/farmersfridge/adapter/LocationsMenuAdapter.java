package farmersfridge.farmersfridge.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.models.CathegoryModel;
import farmersfridge.farmersfridge.models.MenuItemModel;

/**
 * Created by karuppiah on 8/25/2016.
 */

/*
 * Adapter class is for Locations Menu item main contains
 * Category title
 * Horizontal scroll items
 * Arrow left and right
 */
public class LocationsMenuAdapter extends RecyclerView.Adapter<LocationsMenuAdapter.SimpleViewHolder> {

    private final Context mContext;
    private static List<CathegoryModel> categoryList;
    private static List<List<MenuItemModel>> menuItemList;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final ImageView im1;
        public final ImageView im2;
        private LocationsMenuHoriztlAdapter horizontalAdapter;
        private RecyclerView horizontalList;
        private LinearLayoutManager llm;

        public SimpleViewHolder(View view) {
            super(view);
            Context context = itemView.getContext();
            title = (TextView) view.findViewById(R.id.txtForHeader);

            im1 = (ImageView) itemView.findViewById(R.id.im1); //Arrow left
            im2 = (ImageView) view.findViewById(R.id.im2); //Arrow right

            horizontalList = (RecyclerView) itemView.findViewById(R.id.horizontal_list);
            llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            horizontalList.setLayoutManager(llm);

            //Horizontal list adapter initialized.
            horizontalAdapter = new LocationsMenuHoriztlAdapter(context);
            horizontalList.setAdapter(horizontalAdapter);

        }
    }

    public LocationsMenuAdapter(Context context, List<CathegoryModel> categoryList, List<List<MenuItemModel>> menuItemList) {
        mContext = context;
        this.categoryList = categoryList;
        this.menuItemList = menuItemList;
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View initialized
        final View view = LayoutInflater.from(mContext).inflate(R.layout.courses_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        Typeface headerFnt = Typeface.createFromAsset(mContext.getAssets(), "fonts/GrottoIronic-Bold.otf");
        holder.title.setTypeface(headerFnt);
        //Title of category is fetched from CathegoryModel class
        final CathegoryModel categoryModel = categoryList.get(position);
        holder.title.setText(categoryModel.getName().toUpperCase());
        List<MenuItemModel> menuList = menuItemList.get(position); //Menu items array list
        holder.horizontalAdapter.setData(menuList); // List of Strings
        holder.horizontalAdapter.setRowIndex(position);

        holder.horizontalList.setFocusable(false);
        holder.horizontalList.setClickable(false);

        //Arrow left click listener
        holder.im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.llm.findFirstCompletelyVisibleItemPosition() > 0) {
                    holder.horizontalList.smoothScrollToPosition(holder.llm.findFirstVisibleItemPosition() - 1);

                }
            }
        });

        //Arrow right click listener
        holder.im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.horizontalList.smoothScrollToPosition(holder.llm.findFirstVisibleItemPosition() + 1);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
