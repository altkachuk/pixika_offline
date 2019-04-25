package moshimoshi.cyplay.com.doublenavigation.model.business;

public class ImageProductMedia {

    private Sku sku;
    private Media media;
    private Integer skuPosition;

    public ImageProductMedia(Sku sku, Media media, Integer skuPosition) {
        this.sku = sku;
        this.media = media;
        this.skuPosition = skuPosition;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Integer getSkuPosition() {
        return skuPosition;
    }

    public void setSkuPosition(Integer skuPosition) {
        this.skuPosition = skuPosition;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }
}