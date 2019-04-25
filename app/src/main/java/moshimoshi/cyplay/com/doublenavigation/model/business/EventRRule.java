package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EventRRuleFreq;

/**
 * Created by romainlebouc on 28/04/16.
 */
@Parcel
public class EventRRule {

    Integer y;
    Integer M;
    Integer d;
    String freq;

    public EventRRuleFreq getEventRRuleFreq() {
        return EventRRuleFreq.valueFromCode(this.freq);
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getM() {
        return M;
    }

    public void setM(Integer m) {
        M = m;
    }

    public Integer getD() {
        return d;
    }

    public void setD(Integer d) {
        this.d = d;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }
}
