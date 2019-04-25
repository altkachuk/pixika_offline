package atproj.cyplay.com.dblibrary.db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by andre on 09-Aug-18.
 */

public interface IDatabaseHandler {

    boolean configurationPopulated();
    boolean productPopulated();

    void populateConfigDB(String source) throws JSONException;
    void populateDB(String source) throws JSONException;

    void setCurrentShop(String id);

    void claerAll();
    void clearBusinessData();

    // populate DB from external file
    void addConfiguration(JSONObject configuration) throws JSONException;
    void addShop(JSONObject shop) throws JSONException;
    String addDevice(JSONObject device) throws JSONException;
    void addSeller(JSONObject seller) throws JSONException;
    String addSellerBasketItem(String sellerId, JSONObject item) throws JSONException;
    void addFamily(JSONObject family) throws JSONException;
    void addProduct(JSONObject product) throws JSONException;
    void addProductNames(String productId, JSONArray names, JSONArray shortDescs, JSONArray longDesc) throws JSONException;
    void addProductName(String productId, String text) throws JSONException;
    void addProductFamilies(String productId, JSONArray families) throws JSONException;
    void addProductFamily(String productId, String familyId) throws JSONException;
    String addCustomer(JSONObject customer, boolean addedByApp) throws JSONException;
    void addCustomers(JSONArray customers) throws JSONException;
    void addCustomerEmails(HashMap<String, JSONObject> emailsHashMap) throws JSONException;
    void addCustomerEmails(String customerId, JSONObject emails) throws JSONException;
    void addCustomerEmail(String customerId, String type, String email) throws JSONException;
    String addCustomerBasketItem(String customerId, JSONObject item) throws JSONException;
    String addSale(JSONObject sale) throws JSONException;
    String addWishlist(String customerId, JSONObject wishlist, String shopId) throws JSONException;
    String addOffer(JSONObject offer) throws JSONException;
    String addOfferShop(String offerId, String shopId) throws JSONException;
    String addOfferProduct(String offerId, String productId) throws JSONException;

    // API
    JSONArray getAllConfigurations() throws JSONException;
    JSONArray getAllShops() throws JSONException;
    JSONArray getShop(String id) throws JSONException;
    JSONArray getDevice(String id) throws JSONException;
    String updateDevice(JSONObject device) throws JSONException;
    JSONArray getSeller(String id) throws JSONException;
    JSONArray getSellersByShop(String shopId) throws JSONException;
    String updateSeller(JSONObject seller) throws JSONException;
    JSONArray getSellerBasketItem(String id) throws JSONException;
    JSONArray getSellerBasketItemsBySeller(String sellerId) throws JSONException;
    void clearSellerBasketItemsBySeller(String sellerId) throws JSONException;
    String updateSellerBasketItem(JSONObject item) throws JSONException;
    JSONArray getAllFamilies() throws JSONException;
    JSONArray getFamily(String id) throws JSONException;
    JSONArray getFamiliesByLevel(String level) throws JSONException;
    JSONArray getProduct(String id) throws  JSONException;
    JSONArray getProductBySku(String id, String skuId) throws  JSONException;
    JSONArray getProductsByFamily(String familyId, List<String> projection) throws JSONException;
    JSONArray searchProducts(String text) throws  JSONException;
    JSONArray getCustomer(String id) throws  JSONException;
    JSONArray getUpdatedCustomers() throws JSONException;
    JSONArray searchCustomers(String searchText) throws  JSONException;
    JSONArray searchCustomers(String lastName, String firstName, String organizationName, String email) throws  JSONException;
    JSONArray getCustomerBasketItemsByCustomer(String customerId) throws JSONException;
    String updateCustomer(JSONObject customer) throws JSONException;
    String updateCustomerUpdate(String id, boolean isUpdated) throws JSONException;
    void deleteSellerBasketItem(String id) throws JSONException;
    void deleteSellerBasketItems(List<String> ids) throws JSONException;
    String updateCustomerBasketItem(JSONObject item) throws JSONException;
    void deleteCustomerBasketItem(String id) throws JSONException;
    void deleteCustomerBasketItems(List<String> ids) throws JSONException;
    void clearCustomerBasketItemsBySeller(String customerId) throws JSONException;
    JSONArray getWishlistsByCustomer(String customerId) throws JSONException;
    void deleteWishlist(String id) throws JSONException;
    
    JSONArray getAllSales() throws JSONException;
    void deleteSale(String id) throws JSONException;
    void clearSales() throws JSONException;

    JSONArray getAllOffers() throws JSONException;
    JSONObject getOfferByProduct(String productId) throws JSONException;



    JSONObject createBasket() throws JSONException;
    JSONObject createBasket(JSONArray items) throws JSONException;
}
