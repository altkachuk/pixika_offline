package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.CustomerImageViewHolder;
import moshimoshi.cyplay.com.doublenavigation.ui.viewholder.CustomerInfoImageViewHolder;

/**
 * Created by damien on 20/04/16.
 */
public class CustomerInfoImageAdapter extends ItemAdapter<Media,CustomerInfoImageViewHolder>  {

    private List<Media> medias;

    public CustomerInfoImageAdapter(Context context, List<Media> medias) {
        super(context);
        this.medias = medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
        notifyDataSetChanged();
    }

    @Override
    public CustomerInfoImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer_image, parent, false);
        return new CustomerInfoImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomerInfoImageViewHolder holder, int position) {
        final Media media = medias.get(position);
        if (media != null) {
            holder.setItem(media);
        }
    }

    @Override
    public int getItemCount() {
        return medias != null ? medias.size() : 0;
    }

}
