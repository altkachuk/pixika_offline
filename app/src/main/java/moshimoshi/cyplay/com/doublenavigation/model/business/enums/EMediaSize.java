package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.HashMap;

/**
 * Created by romainlebouc on 06/09/16.
 */
public enum EMediaSize {
    FULL("hd", EMediaType.PICTURE),
    PREVIEW("sd", EMediaType.PICTURE_PREVIEW),
    SELECTOR("", EMediaType.PICTURE_SELECTOR);

    private final String code;
    private final static HashMap<String, EMediaSize> CODE_TO_EMEDIAFORMAT = new HashMap<>();
    private final EMediaType eMediaTypeForPicture;
    static {
        for (EMediaSize eMediaSize : EMediaSize.values()){
            CODE_TO_EMEDIAFORMAT.put(eMediaSize.code, eMediaSize);
        }
    }

    EMediaSize(String code, EMediaType eMediaTypeForPicture) {
        this.code = code;
        this.eMediaTypeForPicture = eMediaTypeForPicture;
    }

    public static EMediaSize getEMediaSizeFromCode(String code){
        return CODE_TO_EMEDIAFORMAT.get(code);
    }

    public String getCode() {
        return code;
    }

    public EMediaType geteMediaTypeForPicture() {
        return eMediaTypeForPicture;
    }
}
