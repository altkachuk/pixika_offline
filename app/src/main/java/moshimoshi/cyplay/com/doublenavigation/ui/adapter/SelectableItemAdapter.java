package moshimoshi.cyplay.com.doublenavigation.ui.adapter;

import java.util.List;

/**
 * Created by romainlebouc on 23/08/16.
 */
public interface SelectableItemAdapter<Item> {

    boolean isSelectionMode();

    void notifySelectedItemsChange();

    List<Item> getSelectedItems();
}
