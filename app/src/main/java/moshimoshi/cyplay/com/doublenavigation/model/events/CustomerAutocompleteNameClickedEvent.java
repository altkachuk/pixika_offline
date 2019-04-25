package moshimoshi.cyplay.com.doublenavigation.model.events;

/**
 * Created by damien on 03/05/16.
 */
public class CustomerAutocompleteNameClickedEvent {

    private final String clickedName;


    public CustomerAutocompleteNameClickedEvent(String clickedName) {
        this.clickedName = clickedName;
    }

    public String getClickedName() {
        return clickedName;
    }

}
