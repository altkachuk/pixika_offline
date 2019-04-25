package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EPhoneType;
import ninja.cyplay.com.apilibrary.models.ModelField;

@Parcel
public class CustomerDetailsPhone {

    @ModelField(eventLogging = true)
    String professional;

    @ModelField(eventLogging = true)
    String mobile;

    @ModelField(eventLogging = true)
    String home;

    public void updateFromOther(CustomerDetailsPhone other) {
        if (other != null) {
            String professional = other.getProfessional();
            if (professional != null) {
                this.professional = professional;
            }

            String mobile = other.getMobile();
            if (mobile != null) {
                this.mobile = mobile;
            }

            String home = other.getHome();
            if (home != null) {
                this.home = home;
            }
        }
    }

    public String getProfessional() {
        return professional;
    }

    public String getMobile() {
        return mobile;
    }

    public String getHome() {
        return home;
    }

    /**
     * Get the first phone number which is not null
     *
     * @return
     */
    public PhoneDetail getFirstPhoneNumber() {
        if (mobile != null) {
            return new PhoneDetail(EPhoneType.MOBILE, mobile);
        } else if (home != null) {
            return new PhoneDetail(EPhoneType.LANDLINE, home);
        } else if (professional != null) {
            return new PhoneDetail(EPhoneType.PROFESSIONAL, professional);
        } else {
            return null;
        }
    }


    public static class PhoneDetail {
        private final EPhoneType ePhoneType;
        private final String phoneNumber;

        public PhoneDetail(EPhoneType ePhoneType, String phoneNumber) {
            this.ePhoneType = ePhoneType;
            this.phoneNumber = phoneNumber;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public EPhoneType getePhoneType() {
            return ePhoneType;
        }
    }

}

