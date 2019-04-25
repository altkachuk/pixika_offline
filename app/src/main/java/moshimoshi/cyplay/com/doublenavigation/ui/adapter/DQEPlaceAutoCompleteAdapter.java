package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ninja.cyplay.com.apilibrary.models.abstractmodels.DQEResult;

/**
 * Created by wentongwang on 24/05/2017.
 */
public class DQEPlaceAutoCompleteAdapter extends AbstractDropDownListAdapter {

    private List<DQEResult> dqeResults;


    public DQEPlaceAutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
        dqeResults = new ArrayList<>();
    }

    @Override
    public String getItem(int position) {
        //return string to put in the edit text
        return getAddress(dqeResults.get(position));
    }

    public DQEResult getCompleteItem(int position) {
        return dqeResults.get(position);
    }

    public void setmResultList(List<DQEResult> list) {
        dqeResults = list;
        mResultList = getDorpDownList();
        notifyDataSetChanged();
    }

    @Override
    protected List<String> getDropDownResults(CharSequence constraint) {
        //show auto complete list
        return getDorpDownList();
    }

    private String getAddress(DQEResult result) {
        String[] address = result.getLabel().split("\\|");
        return address[0];
    }

    private List<String> getDorpDownList(){
        List<String> mResults = new ArrayList<>();

        for (DQEResult item : dqeResults) {
            mResults.add(item.getLabel());
        }

        return mResults;
    }
}