package com.example.a50beees.ui.Recycler;

import android.graphics.Bitmap;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a50beees.R;

import java.util.ArrayList;

public class SandboxRecyclerAdapter extends RecyclerView.Adapter<SandboxRecyclerAdapter.ViewHolder> {

    private final ArrayList<Pair<String, Bitmap>> localDataSet;
    private ViewHolder last_selected;
    private static RecyclerViewClickListener itemListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private String type;

        @Override
        public void onClick(View v) {
            if (last_selected != null) {last_selected.itemView.setSelected(false);}
            last_selected = this;
            v.setSelected(true);

            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }

        public ViewHolder(View view, String type) {
            super(view);
            // Define click listener for the ViewHolder's View

            imageView = (ImageView) view.findViewById(R.id.sandbox_recycler_item_image);

            itemView.setOnClickListener(this);

            this.type = type;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public SandboxRecyclerAdapter(ArrayList<Pair<String, Bitmap>> dataSet, RecyclerViewClickListener itemListener) {
        localDataSet = dataSet;
        this.itemListener = itemListener;
    }

    public String get_selected_type() {
        return last_selected.type;
    }

    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sandbox_recycler_item, viewGroup, false);

        return new ViewHolder(view, "");
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getImageView().setImageBitmap(localDataSet.get(position).second);
        viewHolder.setType(localDataSet.get(position).first);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
