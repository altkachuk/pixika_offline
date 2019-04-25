package moshimoshi.cyplay.com.doublenavigation.model.events;

import ninja.cyplay.com.apilibrary.models.business.ECustomerOpinion;

/**
 * Created by damien on 19/05/16.
 */
public class CustomerOpinionFinishedEvent {

    private final ECustomerOpinion opinion;
    private final Boolean opinionSuccess;

    public CustomerOpinionFinishedEvent(ECustomerOpinion opinion, Boolean opinionSuccess) {
        this.opinion = opinion;
        this.opinionSuccess = opinionSuccess;
    }

    public ECustomerOpinion getOpinion() {
        return opinion;
    }

    public Boolean getOpinionSuccess() {
        return opinionSuccess;
    }

}
