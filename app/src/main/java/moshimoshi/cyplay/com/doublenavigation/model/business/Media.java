package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaType;

/**
 * Created by romainlebouc on 01/08/16.
 */
@Parcel
public class Media  implements Serializable {

    String type;
    String hash;
    String base64;
    String source;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public EMediaType getMediaType(){
        return EMediaType.valueFromCode(this.type);
    }

    public static List<Media> getMediasOfType(List<Media> listToFilter, EMediaType eMediaType){
        List<Media> result = new ArrayList<>();
        if ( listToFilter != null){
            for (Media media : listToFilter){
                if (eMediaType == media.getMediaType()){
                    result.add(media);
                }
            }
        }
        return result;
    }

    public static Media getFirstMediaOfType(List<Media> medias,  EMediaType eMediaType){
        Media result = null;
        if ( medias != null){
            for (Media media : medias){
                if (eMediaType == media.getMediaType()){
                    result= media;
                    break;
                }
            }
        }
        return result;
    }

    public static Media getFirstMediaOfTypeForProduct(Product product, EMediaType eMediaType){
        Media result = null;
        if (product !=null){
            result= getFirstMediaOfType(product.getMedias(), eMediaType);
            if ( result !=null){
                return  result;
            }
            List<Sku> skus = product.getSkus();
            if (skus!=null){
                for (Sku sku : skus){
                    result= getFirstMediaOfType(sku.getMedias(), eMediaType);
                    if (result !=null){
                        return  result;
                    }
                }
            }
        }
        return result;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
