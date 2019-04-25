package atproj.cyplay.com.dblibrary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLClientInfoException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by andre on 09-Aug-18.
 */

public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler {

    public transient final static SimpleDateFormat IN_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    static public final int DATABASE_VERSION = 1;
    static public final String DATABASE_NAME = "retail_crm";

    private final String configurations_table = "configurations";
    private final String shops_table = "shops";
    private final String devices_table = "devices";
    private final String sellers_table = "sellers";
    private final String seller_basket_items_table = "seller_basket_items";
    private final String families_table = "families";
    private final String products_table = "products";
    private final String product_names_table = "product_names";
    private final String product_families_table = "product_families";
    private final String customers_table = "customers_table";
    private final String customer_emails_table = "customer_emails";
    private final String customer_basket_items_table = "customer_basket_items_items";
    private final String sales_table = "sales";
    private final String wishlists_table = "wishlists";
    private final String offers_table = "offers";
    private final String offer_shops_table = "offer_shops";
    private final String offer_products_table = "offer_products";

    private String _dbPath;
    private Context _context;
    private static String _currentShopId;

    public DatabaseHandler(Context context, String applicationId) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _context = context;
        _dbPath = "/data/data/" + applicationId + "/databases/";
    }

    public boolean configurationPopulated() {
        try {
            JSONArray configurations = getAllConfigurations();
            boolean populated = configurations.length() > 0;
            return populated;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean productPopulated() {
        try {
            JSONArray families = getAllFamilies();
            boolean populated = families.length() > 0;
            return populated;
        } catch (Exception e) {
            return false;
        }
    }

    public void populateConfigDB(String source) throws JSONException {
        JSONObject jDB = new JSONObject(source);

        JSONArray configurations = jDB.getJSONArray("configurations");
        for (int i = 0; i < configurations.length(); i++) {
            JSONObject configuration = configurations.getJSONObject(i);
            addConfiguration(configuration);
        }
    }

    public void populateDB(String source) throws JSONException {
        JSONObject jDB = new JSONObject(source);

        JSONArray shops = jDB.getJSONArray("shops");
        for (int i = 0; i < shops.length(); i++) {
            JSONObject shop = shops.getJSONObject(i);
            addShop(shop);
        }

        /*JSONArray devices = jDB.getJSONArray("devices");
        for (int i = 0; i < devices.length(); i++) {
            JSONObject device = devices.getJSONObject(i);
            addDevice(device);
        }*/

        JSONArray sellers = jDB.getJSONArray("sellers");
        for (int i = 0; i < sellers.length(); i++) {
            JSONObject seller = sellers.getJSONObject(i);
            addSeller(seller);
        }

        JSONArray families = jDB.getJSONArray("families");
        for (int i = 0; i < families.length(); i++) {
            JSONObject family = families.getJSONObject(i);
            addFamily(family);
        }

        JSONArray products = jDB.getJSONArray("products");
        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            addProduct(product);
        }

        JSONArray customers = jDB.getJSONArray("customers");
        for (int i = 0; i < customers.length(); i++) {
            JSONObject customer = customers.getJSONObject(i);
            addCustomer(customer, false);
        }

        try {
            JSONArray offers = jDB.getJSONArray("offers");
            for (int i = 0; i < offers.length(); i++) {
                JSONObject offer = offers.getJSONObject(i);
                addOffer(offer);
            }
        } catch (Exception e) {
        }
        ;

    }

    public void setCurrentShop(String id) {
        _currentShopId = id;
    }

    public void claerAll() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM " + configurations_table);

        db.close();

        clearBusinessData();
    }

    public void clearBusinessData() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM " + shops_table);
        db.execSQL("DELETE FROM " + devices_table);
        db.execSQL("DELETE FROM " + sellers_table);
        db.execSQL("DELETE FROM " + seller_basket_items_table);
        db.execSQL("DELETE FROM " + families_table);
        db.execSQL("DELETE FROM " + products_table);
        db.execSQL("DELETE FROM " + product_names_table);
        db.execSQL("DELETE FROM " + product_families_table);
        db.execSQL("DELETE FROM " + customers_table);
        db.execSQL("DELETE FROM " + customer_emails_table);
        db.execSQL("DELETE FROM " + customer_basket_items_table);
        db.execSQL("DELETE FROM " + sales_table);
        db.execSQL("DELETE FROM " + wishlists_table);
        db.execSQL("DELETE FROM " + offers_table);
        db.execSQL("DELETE FROM " + offer_shops_table);
        db.execSQL("DELETE FROM " + offer_products_table);

        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // configurations
        String query = "CREATE TABLE " + configurations_table + "(" +
                " id TEXT PRIMARY KEY, json TEXT);";
        db.execSQL(query);

        // shops
        query = "CREATE TABLE " + shops_table + "(" +
                " id TEXT PRIMARY KEY, json TEXT);";
        db.execSQL(query);

        // devices
        query = "CREATE TABLE " + devices_table + "(" +
                " id TEXT PRIMARY KEY, json TEXT);";
        db.execSQL(query);

        // sellers
        query = "CREATE TABLE " + sellers_table + "(" +
                " id TEXT PRIMARY KEY, json TEXT, shop_id TEXT);";
        db.execSQL(query);

        // seller basket items
        query = "CREATE TABLE " + seller_basket_items_table + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT, seller_id TEXT);";
        db.execSQL(query);

        // families
        query = "CREATE TABLE " + families_table + "(" +
                " id TEXT PRIMARY KEY, json TEXT, parent TEXT, has_sub_families BOOLEAN);";
        db.execSQL(query);

        // products
        query = "CREATE TABLE " + products_table + "(" +
                " id TEXT PRIMARY KEY, json TEXT);";
        db.execSQL(query);

        // product names
        query = "CREATE TABLE " + product_names_table + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, product_id TEXT, text TEXT);";
        db.execSQL(query);

        // product families
        query = "CREATE TABLE " + product_families_table + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, product_id TEXT, family_id TEXT);";
        db.execSQL(query);

        // customers
        query = "CREATE TABLE " + customers_table + "(" +
                " id TEXT PRIMARY KEY, json TEXT, organization_name TEXT, first_name TEXT, last_name TEXT, is_updated BOOLEAN);";
        db.execSQL(query);

        // customer emails
        query = "CREATE TABLE " + customer_emails_table + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, customer_id TEXT, type TEXT, email TEXT);";
        db.execSQL(query);

        // customer basket items
        query = "CREATE TABLE " + customer_basket_items_table + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT, customer_id TEXT);";
        db.execSQL(query);

        // sales
        query = "CREATE TABLE " + sales_table + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);";
        db.execSQL(query);

        // wishlists
        query = "CREATE TABLE " + wishlists_table + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT, customer_id TEXT, shop_id TEXT);";
        db.execSQL(query);

        // offers
        query = "CREATE TABLE " + offers_table + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);";
        db.execSQL(query);

        // offer_shops
        query = "CREATE TABLE " + offer_shops_table + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, offer_id INTEGER, shop_id TEXT);";
        db.execSQL(query);

        // offer_products
        query = "CREATE TABLE " + offer_products_table + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, offer_id INTEGER, product_id TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + configurations_table);
        db.execSQL("DROP TABLE IF EXISTS " + shops_table);
        db.execSQL("DROP TABLE IF EXISTS " + devices_table);
        db.execSQL("DROP TABLE IF EXISTS " + sellers_table);
        db.execSQL("DROP TABLE IF EXISTS " + seller_basket_items_table);
        db.execSQL("DROP TABLE IF EXISTS " + families_table);
        db.execSQL("DROP TABLE IF EXISTS " + products_table);
        db.execSQL("DROP TABLE IF EXISTS " + product_names_table);
        db.execSQL("DROP TABLE IF EXISTS " + product_families_table);
        db.execSQL("DROP TABLE IF EXISTS " + customers_table);
        db.execSQL("DROP TABLE IF EXISTS " + customer_emails_table);
        db.execSQL("DROP TABLE IF EXISTS " + customer_basket_items_table);
        db.execSQL("DROP TABLE IF EXISTS " + sales_table);
        db.execSQL("DROP TABLE IF EXISTS " + wishlists_table);
        db.execSQL("DROP TABLE IF EXISTS " + offers_table);
        db.execSQL("DROP TABLE IF EXISTS " + offer_shops_table);
        db.execSQL("DROP TABLE IF EXISTS " + offer_products_table);
    }

    public void addConfiguration(JSONObject configuration) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String id = configuration.getString("id");

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("json", configuration.toString());

        db.insert(configurations_table, null, values);
        db.close();
    }

    public void addShop(JSONObject shop) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String id = shop.getString("id");

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("json", shop.toString());

        db.insert(shops_table, null, values);
        db.close();
    }

    public String addDevice(JSONObject device) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String id = device.getString("id");

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("json", device.toString());

        db.insert(devices_table, null, values);
        db.close();

        return id;
    }

    public void addSeller(JSONObject seller) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String id = seller.getString("id");

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("json", seller.toString());
        if (seller.has("basket") && seller.getJSONObject("basket").has("shop_id")) {
            String shopId = seller.getJSONObject("basket").getString("shop_id");
            values.put("shop_id", shopId);
        } else {
            String shopId = seller.getString("shop_id");
            values.put("shop_id", shopId);
        }

        db.insert(sellers_table, null, values);
        db.close();
    }

    public String addSellerBasketItem(String sellerId, JSONObject item) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("seller_id", sellerId);
        values.put("json", item.toString());

        long id = -1;
        if (item.has("id")) {
            id = item.getLong("id");
            values.put("id", id);
        }

        id = db.insert(seller_basket_items_table, null, values);
        db.close();

        return String.valueOf(id);
    }

    public void addFamily(JSONObject family) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String id = family.getString("id");
        boolean has_sub_families = family.getBoolean("has_sub_families");


        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("json", family.toString());
        if (family.has("parent")) {
            String parent = family.getString("parent");
            values.put("parent", parent);
        }
        values.put("has_sub_families", has_sub_families);

        db.insert(families_table, null, values);
        db.close();

    }

    public void addProduct(JSONObject product) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String id = product.getString("id");
        String name = product.getString("name");
        name += " " + product.getString("short_desc");
        name += " " + product.getString("long_desc");
        JSONArray families = product.getJSONArray("family_ids");

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("json", product.toString());

        db.insert(products_table, null, values);
        db.close();

        addProductName(id, name);
        addProductFamilies(id, families);
    }

    public void addProductNames(String productId, JSONArray names, JSONArray shortDescs, JSONArray longDesc) throws JSONException {
        String name = "";
        for (int i = 0; i < names.length(); i++) {
            JSONObject jName = names.getJSONObject(i);
            String text = jName.getString("text");
            name += " " + text;
        }
        for (int i = 0; i < shortDescs.length(); i++) {
            JSONObject jName = shortDescs.getJSONObject(i);
            String text = jName.getString("text");
            name += " " + text;
        }
        for (int i = 0; i < longDesc.length(); i++) {
            JSONObject jName = longDesc.getJSONObject(i);
            String text = jName.getString("text");
            name += " " + text;
        }

        addProductName(productId, name);
    }

    public void addProductName(String productId, String text) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("product_id", productId);
        values.put("text", text);

        db.insert(product_names_table, null, values);
        db.close();
    }

    public void addProductFamilies(String productId, JSONArray families) throws JSONException {
        for (int i = 0; i < families.length(); i++) {
            String familyId = families.getString(i);
            addProductFamily(productId, familyId);
        }
    }

    public void addProductFamily(String productId, String familyId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("product_id", productId);
        values.put("family_id", familyId);

        db.insert(product_families_table, null, values);
        db.close();
    }

    public String addCustomer(JSONObject customer, boolean isUpdated) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String id = customer.getString("id");
        String organization_name = customer.getJSONObject("professional_details").getString("organization_name");
        String first_name = "";
        String last_name = "";
        if (customer.getJSONObject("details").has("first_name"))
            first_name = customer.getJSONObject("details").getString("first_name");
        if (customer.getJSONObject("details").has("last_name"))
            last_name = customer.getJSONObject("details").getString("last_name");

        JSONObject emails = customer.getJSONObject("details").getJSONObject("email");

        customer.put("is_updated", isUpdated);

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("json", customer.toString());
        values.put("organization_name", organization_name);
        values.put("first_name", first_name);
        values.put("last_name", last_name);
        values.put("is_updated", isUpdated);

        db.insert(customers_table, null, values);
        db.close();

        addCustomerEmails(id, emails);

        return id;
    }

    public void addCustomers(JSONArray customers) throws JSONException {
        HashMap<String, JSONObject> emailsHashMap = new HashMap<>();

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        for (int i = 0; i < customers.length(); i++) {
            JSONObject customer = customers.getJSONObject(i);
            String id = customer.getString("id");
            String organization_name = customer.getJSONObject("professional_details").getString("organization_name");
            String first_name = "";
            String last_name = "";
            if (customer.getJSONObject("details").has("first_name"))
                first_name = customer.getJSONObject("details").getString("first_name");
            if (customer.getJSONObject("details").has("last_name"))
                last_name = customer.getJSONObject("details").getString("last_name");

            JSONObject emails = customer.getJSONObject("details").getJSONObject("email");
            emailsHashMap.put(id, emails);

            customer.put("is_updated", false);

            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("json", customer.toString());
            values.put("organization_name", organization_name);
            values.put("first_name", first_name);
            values.put("last_name", last_name);
            values.put("is_updated", false);

            db.insert(customers_table, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        addCustomerEmails(emailsHashMap);
    }

    public void addCustomerEmails(HashMap<String, JSONObject> emailsHashMap) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        for (String id : emailsHashMap.keySet()) {
            JSONObject emails = emailsHashMap.get(id);

            Iterator<String> i = emails.keys();
            while (i.hasNext()) {
                String type = i.next();
                String email = emails.getString(type);

                ContentValues values = new ContentValues();
                values.put("customer_id", id);
                values.put("type", type);
                values.put("email", email);

                db.insert(customer_emails_table, null, values);
            }

        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void addCustomerEmails(String customerId, JSONObject emails) throws JSONException {
        Iterator<String> i = emails.keys();
        while (i.hasNext()) {
            String key = i.next();
            String value = emails.getString(key);

            addCustomerEmail(customerId, key, value);
        }
    }

    public void addCustomerEmail(String customerId, String type, String email) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("customer_id", customerId);
        values.put("type", type);
        values.put("email", email);

        db.insert(customer_emails_table, null, values);
        db.close();
    }

    public String addCustomerBasketItem(String customerId, JSONObject item) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("customer_id", customerId);
        values.put("json", item.toString());

        long id = -1;
        if (item.has("id")) {
            id = item.getLong("id");
            values.put("id", id);
        }

        id = db.insert(customer_basket_items_table, null, values);
        db.close();

        return String.valueOf(id);
    }


    public JSONArray getAllConfigurations() throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + configurations_table;
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getAllShops() throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + shops_table;
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getShop(String id) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + shops_table
                + " WHERE id = " + "'" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getDevice(String id) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + devices_table
                + " WHERE id = " + "'" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public String updateDevice(JSONObject device) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String id = device.getString("id");
        String query = "UPDATE " + devices_table +
                " SET " + "json = '" + escape(device) + "'"
                + " WHERE id = " + "'" + id + "'" + ";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();

        return id;
    }

    public JSONArray getSeller(String id) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + sellers_table
                + " WHERE id = " + "'" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getSellersByShop(String shopId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + sellers_table
                + " WHERE shop_id = " + "'" + shopId + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public String updateSeller(JSONObject seller) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String id = seller.getString("id");
        String query = "UPDATE " + sellers_table +
                " SET " + "json = '" + escape(seller) + "'"
                + " WHERE id = " + "'" + id + "'" + ";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();

        return id;
    }

    public JSONArray getSellerBasketItem(String id) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + seller_basket_items_table
                + " WHERE id = " + "'" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                String itemId = cursor.getString(0);
                JSONObject object = new JSONObject(cursor.getString(1));
                object.put("id", itemId);
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getSellerBasketItemsBySeller(String sellerId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + seller_basket_items_table
                + " WHERE seller_id = " + "'" + sellerId + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                String itemId = cursor.getString(0);
                JSONObject object = new JSONObject(cursor.getString(1));
                object.put("id", itemId);
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public void clearSellerBasketItemsBySeller(String sellerId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + seller_basket_items_table
                + " WHERE seller_id = " + "'" + sellerId + "'";
        db.execSQL(query);
        db.close();
    }

    public String updateSellerBasketItem(JSONObject item) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String id = item.getString("id");
        String query = "UPDATE " + seller_basket_items_table +
                " SET " + "json = '" + escape(item) + "'"
                + " WHERE id = " + "'" + id + "'" + ";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();

        return id;
    }

    public JSONArray getAllFamilies() throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + families_table;
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }


    public JSONArray getFamily(String id) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + families_table
                + " WHERE id = " + "'" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getFamiliesByLevel(String level) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + families_table
                + " WHERE parent = " + "'" + level + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getProduct(String id) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + products_table
                + " WHERE id = " + "'" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getProductBySku(String id, String skuId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + products_table
                + " WHERE id = " + "'" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getProductsByFamily(String familyId, List<String> projection) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT t2.id, t2.json FROM " + product_families_table + " t1"
                + " LEFT JOIN " + products_table + " t2 ON t2.id = t1.product_id"
                + " WHERE t1.family_id = " + "'" + familyId + "'";

        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray searchProducts(String text) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT t2.id, t2.json FROM " + product_names_table + " t1"
                + " LEFT JOIN " + products_table + " t2 ON t2.id = t1.product_id"
                + " WHERE t1.text LIKE '%" + text + "%'";

        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getCustomer(String id) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + customers_table
                + " WHERE id = " + "'" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                object.put("wishlist", getWishlistsByCustomer(id));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getUpdatedCustomers() throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + customers_table
                + " WHERE is_updated = 1";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray searchCustomers(String searchText) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT t2.id, t2.json FROM " + customer_emails_table + " t1"
                + " LEFT JOIN " + customers_table + " t2 ON t2.id = t1.customer_id"
                + " WHERE t2.last_name LIKE '%" + searchText + "%'"
                + " OR t2.first_name LIKE '%" + searchText + "%'"
                + " OR t2.organization_name LIKE '%" + searchText + "%'"
                + " OR t1.email LIKE '%" + searchText + "%'";

        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }


    public JSONArray searchCustomers(String lastName, String firstName, String organizationName, String email) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT t2.id, t2.json FROM " + customer_emails_table + " t1"
                + " LEFT JOIN " + customers_table + " t2 ON t2.id = t1.customer_id"
                + " WHERE t2.last_name LIKE '%" + lastName + "%'"
                + " AND t2.first_name LIKE '%" + firstName + "%'"
                + " AND t2.organization_name LIKE '%" + organizationName + "%'"
                + " AND t1.email LIKE '%" + email + "%'";

        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONArray getCustomerBasketItemsByCustomer(String customerId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + customer_basket_items_table
                + " WHERE customer_id = " + "'" + customerId + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                String itemId = cursor.getString(0);
                JSONObject object = new JSONObject(cursor.getString(1));
                object.put("id", itemId);
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public String updateCustomer(JSONObject customer) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String str = customer.toString();
        str = DatabaseUtils.sqlEscapeString(str);

        String id = customer.getString("id");

        ContentValues values = new ContentValues();
        values.put("json", customer.toString());

        String query = "UPDATE " + customers_table +
                " SET " + "json = '" + escape(customer) + "'"
                + ", is_updated = " + 1
                + " WHERE id = " + "'" + id + "'" + ";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();

        return id;
    }

    public String updateCustomerUpdate(String id, boolean isUpdated) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "UPDATE " + customers_table +
                " SET " + "is_updated = " + (isUpdated ? 1 : 0)
                + " WHERE id = " + "'" + id + "'" + ";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();

        return id;
    }

    public void deleteSellerBasketItem(String id) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + seller_basket_items_table
                + " WHERE id = " + "'" + id + "'";
        db.execSQL(query);
        db.close();
    }

    public void deleteSellerBasketItems(List<String> ids) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + seller_basket_items_table
                + " WHERE id = " + "'" + ids.get(0) + "'";
        for (int i = 1; i < ids.size(); i++) {
            query += " OR id = " + "'" + ids.get(i) + "'";
        }
        db.execSQL(query);
        db.close();
    }

    public String updateCustomerBasketItem(JSONObject item) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String id = item.getString("id");
        String query = "UPDATE " + customer_basket_items_table +
                " SET " + "json = '" + escape(item) + "'"
                + " WHERE id = " + "'" + id + "'" + ";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();

        return id;
    }

    public void deleteCustomerBasketItem(String id) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + customer_basket_items_table
                + " WHERE id = " + "'" + id + "'";
        db.execSQL(query);
        db.close();
    }

    public void deleteCustomerBasketItems(List<String> ids) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + customer_basket_items_table
                + " WHERE id = " + "'" + ids.get(0) + "'";
        for (int i = 1; i < ids.size(); i++) {
            query += " OR id = " + "'" + ids.get(i) + "'";
        }
        db.execSQL(query);
        db.close();
    }

    public void clearCustomerBasketItemsBySeller(String customerId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + customer_basket_items_table
                + " WHERE customer_id = " + "'" + customerId + "'";
        db.execSQL(query);
        db.close();
    }

    public JSONArray getWishlistsByCustomer(String customerId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + wishlists_table
                + " WHERE customer_id = " + "'" + customerId + "'";
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                String itemId = cursor.getString(0);
                JSONObject object = new JSONObject(cursor.getString(1));
                String shopId = cursor.getString(3);
                object.put("id", itemId);
                object.put("shop_id", shopId);
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public void deleteWishlist(String id) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + wishlists_table
                + " WHERE id = " + "'" + id + "'";
        db.execSQL(query);
        db.close();
    }

    public String addSale(JSONObject purchase) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("json", purchase.toString());

        long id = db.insert(sales_table, null, values);
        db.close();

        return String.valueOf(id);
    }

    public String addWishlist(String customerId, JSONObject wishlist, String shopId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("json", wishlist.toString());
        values.put("customer_id", customerId);
        values.put("shop_id", shopId);

        long id = db.insert(wishlists_table, null, values);
        db.close();

        return String.valueOf(id);
    }

    public String addOffer(JSONObject offer) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("json", offer.toString());

        String id = String.valueOf(db.insert(offers_table, null, values));
        db.close();

        if (offer.has("shop_ids")) {
            JSONArray shopIds = offer.getJSONArray("shop_ids");
            for (int i = 0; i < shopIds.length(); i++) {
                String shopId = shopIds.getString(i);
                addOfferShop(id, shopId);
            }
        }

        if (offer.has("product_ids")) {
            JSONArray productIds = offer.getJSONArray("product_ids");
            for (int i = 0; i < productIds.length(); i++) {
                String productId = productIds.getString(i);
                addOfferProduct(id, productId);
            }
        }

        return String.valueOf(id);
    }

    public String addOfferShop(String offerId, String shopId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("offer_id", offerId);
        values.put("shop_id", shopId);

        String id = String.valueOf(db.insert(offer_shops_table, null, values));
        db.close();

        return String.valueOf(id);
    }

    public String addOfferProduct(String offerId, String productId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("offer_id", offerId);
        values.put("product_id", productId);

        String id = String.valueOf(db.insert(offer_products_table, null, values));
        db.close();

        return String.valueOf(id);
    }

    public JSONArray getAllSales() throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + sales_table;
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                JSONObject object = new JSONObject(cursor.getString(1));
                object.put("id", id);
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public void deleteSale(String id) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + sales_table
                + " WHERE id = " + "'" + id + "'";
            db.execSQL(query);
            db.close();
    }

    public void clearSales() throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + sales_table;
        db.execSQL(query);
        db.close();
    }

    public JSONArray getAllOffers() throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + offers_table;
        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return jsonArray;
    }

    public JSONObject getOfferByProduct(String productId) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT t1.id, t1.json FROM " + offers_table + " t1"
                + " LEFT JOIN " + offer_products_table + " t2 ON t2.offer_id = t1.id"
                + " WHERE t2.product_id = '" + productId + "'";

        Cursor cursor = db.rawQuery(query, null);

        JSONArray jsonArray = new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject object = new JSONObject(cursor.getString(1));
                jsonArray.put(object);
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (jsonArray.length() > 0) {
            return jsonArray.getJSONObject(jsonArray.length()-1);
        }

        return null;
    }


    public JSONObject createBasket() throws JSONException {
        String shopId = null;
        JSONArray shops = getAllShops();
        for (int i = 0; i < shops.length(); i++) {
            JSONObject shop = shops.getJSONObject(i);
            shopId = shop.getString("id");
            break;
        }

        JSONObject result = new JSONObject();
        result.put("currency_iso4217", "EUR");
        result.put("items_quantity", 0);
        result.put("total", 0);
        result.put("items_total_amount", 0);
        result.put("shop_id", shopId);
        result.put("total_discount", 0);
        result.put("delivery_fees_amount", 0);
        result.put("items", new JSONArray());
        return result;
    }

    public JSONObject createBasket(JSONArray items) throws JSONException {
        JSONObject basket = createBasket();

        int items_quantity = 0;
        double total = 0.0;
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            int qty = item.getInt("qty");
            items_quantity += qty;
            double price = item.getDouble("unit_price") * (double)qty;
            total += price;
        }

        basket.put("items", items);
        basket.put("items_quantity", items_quantity);
        basket.put("total", String.valueOf(total));
        basket.put("items_total_amount", String.valueOf(total));
        basket.put("creation_date", IN_FORMAT.format(new Date()));

        return basket;
    }

    private static String escape(String src) {
        String res = src.replaceAll("\\'","\\'\\'");
        return res;
    }

    private static String escape(JSONObject jsonObject) {
        return escape(jsonObject.toString());
    }
}
