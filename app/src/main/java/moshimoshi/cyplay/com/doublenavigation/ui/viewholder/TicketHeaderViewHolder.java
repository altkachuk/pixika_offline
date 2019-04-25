package moshimoshi.cyplay.com.doublenavigation.ui.viewholder;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.utils.DateUtils;

/**
 * Created by romainlebouc on 16/05/16.
 */
public class TicketHeaderViewHolder extends ATicketViewHolder {

    private final static String FORMAT_PATTERN = "dd MMM yyyy";
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(FORMAT_PATTERN);

    @Nullable
    @BindView(R.id.container)
    LinearLayout background;

    @Nullable
    @BindView(R.id.ticket_date)
    TextView ticketDate;

    public TicketHeaderViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        if ( this.background !=null){
            this.background.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        }

    }

    @Override
    public void setTicketLine(Ticket ticket, int position) {
        if (ticketDate !=null ){
            if ( ticket.getPurchaseDate() != null) {
                ticketDate.setText(DateUtils.formatDate("dd MMMM yyyy", ticket.getPurchaseDate()));
            }else{
                ticketDate.setText(null);
            }
        }


    }
}
