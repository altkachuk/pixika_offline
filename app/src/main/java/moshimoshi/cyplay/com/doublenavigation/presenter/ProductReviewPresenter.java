package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.ProductReview;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by romainlebouc on 03/01/2017.
 */

public interface ProductReviewPresenter {
    void setView(ResourceView<ProductReview> view);

    void setCreateView(ResourceView<ProductReview> view);

    void addReview(ProductReview productReview);

    void getProductReview(String customerId,
                          String productId,
                          String skuId);
    void updateReview(ProductReview productReview);

}
