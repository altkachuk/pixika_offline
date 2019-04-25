package moshimoshi.cyplay.com.doublenavigation.ui.custom;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketOffer;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.OfferDetailAdapter;

/**
 * Created by wentongwang on 14/04/2017.
 */

public class OfferDetailDialog{

    private Context context;

    View closeIcon;

    RecyclerView recyclerView;

    private View dialogTitle;
    private View dialogContent;

    AlertDialog dialog;

    public OfferDetailDialog(Context context) {
        this.context = context;

        dialogTitle = LayoutInflater.from(context).inflate(R.layout.dialog_offer_detail_title, null);
        dialogContent = LayoutInflater.from(context).inflate(R.layout.dialog_offer_detail_content, null);

        closeIcon = dialogTitle.findViewById(R.id.dialog_close_icon);

        recyclerView = (RecyclerView) dialogContent.findViewById(R.id.offers_recycler_view);
    }


    public AlertDialog buildAlertDialog(List<BasketOffer> offer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        OfferDetailAdapter adapter = new OfferDetailAdapter(context);
        recyclerView.setAdapter(adapter);
        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.setItems(offer);

        builder.setCustomTitle(dialogTitle);
        builder.setView(dialogContent);
        dialog = builder.create();
        return dialog;
    }

}
