package activities.mswift.info.walaapp.wala.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.model.AboutUsOurTeamModel;

/**
 * Created by karuppiah on 1/21/2016.
 */
public class AboutUsTeamAdapter extends RecyclerView.Adapter<AboutUsTeamAdapter.ViewHolder> {
    private ArrayList<AboutUsOurTeamModel> ourTeamArray = new ArrayList<>();
    private Context context;


    public AboutUsTeamAdapter(ArrayList<AboutUsOurTeamModel> ourTeamArray) {
        this.ourTeamArray = ourTeamArray;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AboutUsTeamAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        context = parent.getContext();
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.about_us_list_item, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        AboutUsOurTeamModel teamModel = ourTeamArray.get(position);

        viewHolder.teamTitle.setText(teamModel.getTitle());
        viewHolder.teamContent.setText(teamModel.getContent());
        TypedArray imgs = context.getResources().obtainTypedArray(R.array.ourTeamImage);
        viewHolder.imageView.setImageResource(imgs.getResourceId(position, -1));

    }


    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView teamTitle;
        public TextView teamContent;
        public ImageView imageView;
        String text;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            teamTitle = (TextView) itemLayoutView.findViewById(R.id.textForAboutTeamTitle);
            teamContent = (TextView) itemLayoutView.findViewById(R.id.textForAboutTeamContent);
            imageView = (ImageView) itemLayoutView.findViewById(R.id.imgForOurTeam);
        }


        @Override
        public void onClick(View view) {
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ourTeamArray.size();
    }
}
