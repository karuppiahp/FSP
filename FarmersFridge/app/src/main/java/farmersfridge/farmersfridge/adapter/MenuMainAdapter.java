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

import java.util.ArrayList;
import java.util.List;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.models.MenuModelRec;

/**
 * Created by karuppiah on 6/28/2016.
 */

/*
 * Adapter class is for Locations Menu item main contains
 * Category title
 * Horizontal scroll items
 * Arrow left and right
 */
public class MenuMainAdapter extends RecyclerView.Adapter<MenuMainAdapter.SimpleViewHolder> {

    private final Context mContext;
    private static List<MenuModelRec> mData;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder{
        public final TextView title;
        public final ImageView im1;
        public final ImageView im2;
        private MenuListAdapter horizontalAdapter;
        private RecyclerView horizontalList;
        private LinearLayoutManager llm;

        public SimpleViewHolder(View view) {
            super(view);
            Context context = itemView.getContext();
            title = (TextView) view.findViewById(R.id.txtForHeader);

            im1 = (ImageView)view.findViewById(R.id.im1);
            im2 = (ImageView)view.findViewById(R.id.im2);

            horizontalList = (RecyclerView) itemView.findViewById(R.id.horizontal_list);
            llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            horizontalList.setLayoutManager(llm);

            //Horizontal list adapter initialized.
            horizontalAdapter = new MenuListAdapter(context);
            horizontalList.setAdapter(horizontalAdapter);

        }
    }

    public MenuMainAdapter(Context context, List<MenuModelRec> data) {
        mContext = context;
        if (data != null)
            mData = new ArrayList<>(data);
        else mData = new ArrayList<>();
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
        holder.title.setText(mData.get(position).getCathegoryName().toUpperCase());
        holder.horizontalAdapter.setData(mData.get(position).getMenuItem()); // List of Strings
        holder.horizontalAdapter.setRowIndex(position);

        holder.horizontalList.setFocusable(false);
        holder.horizontalList.setClickable(false);

        //Arrow left click listener
        holder.im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("left");
                if (holder.llm.findFirstCompletelyVisibleItemPosition() > 0){
                    holder.horizontalList.smoothScrollToPosition(holder.llm.findFirstVisibleItemPosition() - 1);
                }
            }
        });

        //Arrow right click listener
        holder.im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("right");
                holder.horizontalList.smoothScrollToPosition(holder.llm.findFirstVisibleItemPosition() + 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
