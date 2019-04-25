package moshimoshi.cyplay.com.doublenavigation.model.events;

/**
 * Created by romainlebouc on 29/04/2017.
 */

public class LimitedConsumptionEvent {

    private final int maxConsumption;
    private int consumptionCount;

    public LimitedConsumptionEvent(int maxConsumption) {
        this.maxConsumption = maxConsumption;
        this.consumptionCount = this.maxConsumption;
    }

    public boolean consume() {
        consumptionCount--;
        return consumptionCount >= 0;
    }

}
