package com.example.gallery.ui.gallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.data.model.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private OnItemLongClickListener longClickListener;
    private int contextMenuPosition = -1;

    // interface za click
    public interface OnItemClickListener {
        void onItemClick(ImageItem item, int position);
    }

    private List<ImageItem> items = new ArrayList<>();
    private OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    // interface za long click
    public interface OnItemLongClickListener {
        boolean onItemLongClick(ImageItem item, int position);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {this.longClickListener = listener;}

    public int getContextMenuPosition() {
        return contextMenuPosition;
    }
    public ImageItem getItemAt(int position) {
        if (position >= 0 && position < items.size()) {
            return items.get(position);
        }
        return null;
    }


    public void setItems(List<ImageItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageItem item = items.get(position);
        holder.imageView.setImageResource(item.getImageRes());
        holder.titleView.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleView = itemView.findViewById(R.id.textTitle);

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(items.get(position), position);
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    contextMenuPosition = pos;
                    if (longClickListener != null) {
                        return longClickListener.onItemLongClick(items.get(pos), pos);
                    }
                }
                return false;
            });
        }
    }
}
