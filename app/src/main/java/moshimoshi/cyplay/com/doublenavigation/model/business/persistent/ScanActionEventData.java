package moshimoshi.cyplay.com.doublenavigation.model.business.persistent;

import ninja.cyplay.com.apilibrary.models.business.enums.EAction;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventContentData;
import ninja.cyplay.com.apilibrary.models.business.reporting.ActionEventData;

/**
 * Created by romainlebouc on 18/10/16.
 */
public class ScanActionEventData extends ActionEventData<ScanActionEventData> {

    public final static String SCANNED_CODE_KEY = "scanned_code";

    public ScanActionEventData(EResource resource) {
        this.action = EAction.SCAN.getCode();
        this.resource = resource.getCode();
    }

    public ScanActionEventData setScannedCode(String code) {
        this.setValue(code);
        this.getContents().add(new ActionEventContentData(SCANNED_CODE_KEY, code));
        return this;
    }

}
