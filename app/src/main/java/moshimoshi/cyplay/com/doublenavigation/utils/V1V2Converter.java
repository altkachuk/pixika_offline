package moshimoshi.cyplay.com.doublenavigation.utils;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.Category;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerDetails;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerDetailsEmail;
import moshimoshi.cyplay.com.doublenavigation.model.business.Mail;
import moshimoshi.cyplay.com.doublenavigation.model.business.Media;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecSelector;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecification;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProfessionalDetails;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ska;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.model.business.WishlistItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EMediaType;
import ninja.cyplay.com.apilibrary.models.abstractmodels.v2.Company;
import ninja.cyplay.com.apilibrary.models.abstractmodels.v2.Contact;
import ninja.cyplay.com.apilibrary.models.abstractmodels.v2.ContactImage;
import moshimoshi.cyplay.com.doublenavigation.model.business.v2.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.v2.ProductCategory;
import moshimoshi.cyplay.com.doublenavigation.model.business.v2.ProductDescription;
import moshimoshi.cyplay.com.doublenavigation.model.business.v2.ProductVariant;
import moshimoshi.cyplay.com.doublenavigation.model.business.v2.Seller;
import ninja.cyplay.com.apilibrary.models.abstractmodels.v2.Country;
import ninja.cyplay.com.apilibrary.models.abstractmodels.v2.Order;
import ninja.cyplay.com.apilibrary.models.abstractmodels.v2.OrderItem;

/**
 * Created by andre on 15-Mar-19.
 */

public class V1V2Converter {

    private static final String LARGE_PREFIX = "?type=large";
    private static final String SMALL_PREFIX = "?type=small";

    static public JSONObject sellerV22V1(JSONObject jV2) throws JSONException {
        Gson gson = new Gson();
        Seller seller2 = gson.fromJson(jV2.toString(), Seller.class);

        moshimoshi.cyplay.com.doublenavigation.model.business.Seller seller1 = convertSeller22Seller1(seller2);

        Gson gson2 = new Gson();
        JSONObject jV1 = new JSONObject(gson2.toJson(seller1));
        return jV1;
    }

    private static moshimoshi.cyplay.com.doublenavigation.model.business.Seller convertSeller22Seller1(Seller seller2) {
        moshimoshi.cyplay.com.doublenavigation.model.business.Seller seller1 = new moshimoshi.cyplay.com.doublenavigation.model.business.Seller();
        seller1.setId(seller2.getId());
        seller1.setUsername(seller2.getUsername());
        seller1.setFirstName(seller2.getFirstName());
        seller1.setLastName(seller2.getLastName());
        seller1.setPassword(seller2.getPassword());
        seller1.setShopId("MOBILE");

        return seller1;
    }

    static public JSONObject customerV22V1(JSONObject jV2) throws JSONException {
        Gson gson = new Gson();
        Contact customer2 = gson.fromJson(jV2.toString(), Contact.class);

        Customer customer1 = convertCustomer22Customer1(customer2);

        Gson gson2 = new Gson();
        JSONObject jV1 = new JSONObject(gson2.toJson(customer1));
        return jV1;
    }

    static public JSONObject customerV22V12(JSONObject jV2) throws JSONException {
        JSONObject jV1 = new JSONObject();

        // id
        jV1.put("id", jV2.getString("id"));

        // -----------------------------------------------------------------------------------------
        // professional_details
        JSONObject professional_details = new JSONObject();
        Object company = jV2.get("company");
        String organizationName = "";
        if ((company instanceof JSONArray) && ((JSONArray)company).length() > 0) {
            organizationName = ((JSONObject)((JSONArray)company).get(0)).getString("name");
        }
        professional_details.put("organization_name", organizationName);
        jV1.put("professional_details", professional_details);
        // professional_details
        // -----------------------------------------------------------------------------------------


        // -----------------------------------------------------------------------------------------
        // details
        JSONObject details = new JSONObject();
        details.put("first_name", jV2.getString("first_name"));
        details.put("last_name", jV2.getString("last_name"));

        JSONObject email = new JSONObject();
        email.put("professional", jV2.getString("email"));
        details.put("email", email);

        JSONObject mail = new JSONObject();
        mail.put("address1", jV2.getString("address1"));
        mail.put("address2", jV2.getString("address2"));
        mail.put("zipcode", jV2.getString("zip"));
        mail.put("city", jV2.getString("city"));

        Object country = jV2.get("country");
        if (country instanceof JSONObject) {
            mail.put("country", ((JSONObject) country).getString("alpha_3"));
        }
        details.put("mail", mail);

        JSONArray medias = new JSONArray();
        JSONArray images = jV2.getJSONArray("images");
        for (int i = 0; i < images.length(); i++) {
            JSONObject image = images.getJSONObject(i);
            JSONObject media = new JSONObject();
            media.put("base64", image.getString("base64"));
            media.put("type", "PICTURE");
            medias.put(media);
        }
        details.put("medias", medias);
        jV1.put("details", details);
        // details
        // -----------------------------------------------------------------------------------------

        jV1.put("source", jV2.getString("source"));

        return jV1;
    }

    private static Customer convertCustomer22Customer1(Contact customer2) {
        Customer customer1 = new Customer();
        customer1.setId(customer2.getId());

        ProfessionalDetails professionalDetails = new ProfessionalDetails();
        String organizationName = customer2.getCompany().size() > 0 ? customer2.getCompany().get(0).getName() : "";
        professionalDetails.setOrganizationName(organizationName);
        customer1.setProfessionalDetails(professionalDetails);

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setFirstName(customer2.getFirstName());
        customerDetails.setLastName(customer2.getLastName());
        CustomerDetailsEmail email = new CustomerDetailsEmail();
        email.setProfessional(customer2.getEmail());
        customerDetails.setEmail(email);
        Mail mail = new Mail();
        mail.setAddress1(customer2.getAddress1());
        mail.setAddress2(customer2.getAddress2());
        mail.setZipcode(customer2.getZip());
        mail.setCity(customer2.getCity());
        String country = customer2.getCountry() != null ? customer2.getCountry().getAlpha3() : "";
        mail.setCountry(country);
        customerDetails.setMail(mail);

        List<Media> medias = new ArrayList<>();
        for (ContactImage image : customer2.getImages()) {
            Media media = new Media();
            media.setHash(image.getBase64());
            media.setType(EMediaType.PICTURE.toString());
        }
        customerDetails.setMedias(medias);

        customer1.setDetails(customerDetails);

        customer1.setSource(customer2.getSource());

        return customer1;
    }

    public static Contact convertCustomer12Customer2(Customer customer1, boolean isUpdated) {
        Contact contact = new Contact();
        contact.setId(customer1.getId());
        contact.setFirstName(customer1.getDetails().getFirst_name());
        contact.setLastName(customer1.getDetails().getLast_name());
        contact.setEmail(customer1.getDetails().getEmail().getProfessional());
        contact.setAddress1(customer1.getDetails().getMail().getAddress1());
        contact.setAddress2(customer1.getDetails().getMail().getAddress2());
        contact.setZip(customer1.getDetails().getMail().getZipcode());
        contact.setCity(customer1.getDetails().getMail().getCity());
        contact.setState(customer1.getDetails().getMail().getRegion());

        Country country = new Country();
        country.setAlpha3(customer1.getDetails().getMail().getCountry());
        contact.setCountry(country);

        contact.setSource(customer1.getSource());

        Company company = new Company();
        company.setName(customer1.getProfessionalDetails().getOrganizationName());
        List<Company> companies = new ArrayList<>();
        companies.add(company);
        contact.setCompany(companies);

        List<String> wishes = new ArrayList<>();
        for (WishlistItem wishlistItem : customer1.getWishlist()) {
            wishes.add(wishlistItem.getSkuId());
        }
        contact.setWishes(wishes);

        List<ContactImage> images = new ArrayList<>();
        for (Media media : customer1.getDetails().getMedias()) {
            ContactImage contactImage = new ContactImage();
            contactImage.setBase64(media.getBase64());
            images.add(contactImage);
        }
        contact.setImages(images);

        contact.setIsUpdated(isUpdated);

        return contact;
    }

    public static Contact convertSale12Customer2(Sale sale) {
        Contact contact = convertCustomer12Customer2(sale.getCustomer(), false);

        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setDateCreated(sale.getBasket().getCreation_date());
        List<OrderItem> orderItems = new ArrayList<>();
        for (BasketItem basketItem : sale.getBasket().getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setReference(basketItem.getSku_id());
            orderItem.setPrice(basketItem.getUnit_price());
            orderItem.setQuantity(basketItem.getQty());
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        orders.add(order);
        contact.setOrders(orders);

        return contact;
    }

    static public JSONObject familyV22V1(JSONObject jV2) throws JSONException {
        Gson gson = new Gson();
        ProductCategory category2 = gson.fromJson(jV2.toString(), ProductCategory.class);

        Category category1 = convertCategory22Category1(category2);
        category1.setHas_sub_families(false);
        category1.setParent("0");


        Gson gson2 = new Gson();
        JSONObject jV1 = new JSONObject(gson2.toJson(category1));

        JSONArray name = new JSONArray();
        JSONObject n = new JSONObject();
        n.put("lang", "en");
        n.put("text", category2.getName());
        name.put(n);

        jV1.put("name", name);
        jV1.put("parent", 0);

        return jV1;
    }

    static private Category convertCategory22Category1(ProductCategory category2) {
        Category category1 = new Category();
        category1.setId(category2.getName());
        category1.setHas_sub_families(false);

        return category1;
    }

    static public List<String> getMediasFromProductV2(JSONObject jV2) {
        Gson gson = new Gson();
        Product product2 = gson.fromJson(jV2.toString(), Product.class);

        List<String> result = new ArrayList<>();
        if (product2.getVariants() != null) {
            for (ProductVariant variant : product2.getVariants()) {
                if (variant.getMedias() != null) {
                    for (moshimoshi.cyplay.com.doublenavigation.model.business.v2.Media media : variant.getMedias()) {
                        String url = media.getUrl();
                        if (result.size() == 0) {
                            result.add(url + SMALL_PREFIX);
                        }
                        result.add(url + LARGE_PREFIX);
                    }
                }
            }
        }

        return result;
    }

    static public JSONObject productV22V1(Context context, JSONObject jV2, String language) throws JSONException {
        Gson gson = new Gson();
        Product product2 = gson.fromJson(jV2.toString(), Product.class);

        moshimoshi.cyplay.com.doublenavigation.model.business.Product product1 = convertProduct22Product1(context, product2, language);

        Gson gson2 = new Gson();
        JSONObject jV1 = new JSONObject(gson2.toJson(product1));

        return jV1;
    }

    private static moshimoshi.cyplay.com.doublenavigation.model.business.Product convertProduct22Product1(Context context, Product product2, String language) {
        moshimoshi.cyplay.com.doublenavigation.model.business.Product product1 = new moshimoshi.cyplay.com.doublenavigation.model.business.Product();
        product1.setId(product2.getReference());

        ProductDescription productDescription = product2.getDescriptionByLanguage(language);
        product1.setName(productDescription.getName());
        product1.setShort_desc(productDescription.getDescription());
        product1.setLong_desc(productDescription.getDescription());

        List<String> familyIds = new ArrayList<>();
        if (product2.getCategory() != null) {
            for (ProductCategory productCategory : product2.getCategory()) {
                familyIds.add(productCategory.getName());
            }
        }
        product1.setFamilyIds(familyIds);

        product1.setBrand("Pixika");

        List<Media> productMedias = new ArrayList<>();
        HashMap<String, String> productSelectors = new HashMap<>();

        List<Sku> skus = new ArrayList<>();
        if (product2.getVariants() != null) {
            for (ProductVariant productVariant : product2.getVariants()) {
                Sku sku = new Sku();
                sku.setId(productVariant.getReference());

                ProductDescription skuDescription = productVariant.getDescriptionByLanguage(language);
                if (skuDescription != null) {
                    sku.setName(skuDescription.getName());
                    sku.setDesc(skuDescription.getDescription());

                    List<ProductSpecification> specifications = new ArrayList<>();
                    if (skuDescription.getColor() != null) {
                        ProductSpecification specification = new ProductSpecification();
                        specification.setId("color");
                        String specText = StringUtils.getStringResourceByName(context, "color");
                        specification.setSpec(specText);
                        specification.setValue(skuDescription.getColor());
                        specifications.add(specification);
                        productSelectors.put("color", "color");
                    }
                    if (skuDescription.getCapacity() != null) {
                        ProductSpecification specification = new ProductSpecification();
                        specification.setId("capacity");
                        String specText = StringUtils.getStringResourceByName(context, "capacity");
                        specification.setSpec(specText);
                        specification.setValue(skuDescription.getCapacity());
                        specifications.add(specification);
                        productSelectors.put("capacity", "capacity");
                    }
                    sku.setSpecs(specifications);
                }

                List<Media> medias = new ArrayList<>();
                if (productVariant.getMedias() != null) {
                    for (moshimoshi.cyplay.com.doublenavigation.model.business.v2.Media media2 : productVariant.getMedias()) {
                        Media media = new Media();
                        media.setType("PICTURE");
                        String lUrl = media2.getUrl() + LARGE_PREFIX;
                        String lDir = context.getApplicationInfo().dataDir + "/images/" + convertUrlToImageName(lUrl) + ".jpg";
                        media.setSource(lDir);
                        medias.add(media);

                        if (productMedias.size() == 0) {
                            Media productMedia = new Media();
                            String sUrl = media2.getUrl() + SMALL_PREFIX;
                            String sDir = context.getApplicationInfo().dataDir + "/images/" + convertUrlToImageName(sUrl) + ".jpg";
                            productMedia.setSource(sDir);
                            productMedia.setType("PICTURE_PREVIEW");
                            productMedias.add(productMedia);
                        }
                    }
                }
                sku.setMedias(medias);

                List<Ska> availabilities = new ArrayList<>();
                Ska ska = new Ska();
                ska.setShopId("MOBILE");
                ska.setPrice(productVariant.getPrice());
                ska.setStock(productVariant.getStock());
                availabilities.add(ska);
                sku.setAvailabilities(availabilities);

                skus.add(sku);
            }
            product1.setSkus(skus);

            product1.setMedias(productMedias);

            List<ProductSpecSelector> productSpecSelectors = new ArrayList<>();
            for (String key : productSelectors.keySet()) {
                ProductSpecSelector selector = new ProductSpecSelector();
                selector.setSpec_id(productSelectors.get(key));
                productSpecSelectors.add(selector);
            }
            product1.setSelectors(productSpecSelectors);
        }


        return product1;
    }

    static public String convertUrlToImageName(String url) {
        String[] t = url.split("/");
        String image = t[t.length-1];

        String[] t2 = image.split("\\?");
        String name = t2[0];

        if (image.contains(LARGE_PREFIX)) {
            name += "L";
        }
        if (image.contains(SMALL_PREFIX)) {
            name += "S";
        }
        return name;
    }
}
