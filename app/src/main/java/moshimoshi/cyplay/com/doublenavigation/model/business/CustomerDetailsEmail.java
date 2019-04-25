package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.ModelField;

/**
 * Created by damien on 23/08/16.
 */
@Parcel
public class CustomerDetailsEmail {

    @ModelField(eventLogging = true)
    String personal;

    @ModelField(eventLogging = true)
    String professional;

    public void updateFromOther(CustomerDetailsEmail other) {
        if (other != null) {
            String professional = other.getProfessional();
            if (professional != null) {
                this.professional = professional;
            }

            String personal = other.getPersonal();
            if (personal != null) {
                this.personal = personal;
            }

        }
    }


    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    /**
     * Get the first email which is not null
     *
     * @return
     */
    public String getFirstEmail() {
        if (personal != null) {
            return personal;
        } else if (professional != null) {
            return professional;
        } else {
            return professional;
        }
    }
}
