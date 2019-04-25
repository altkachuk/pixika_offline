package moshimoshi.cyplay.com.doublenavigation.domain.interactor;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import moshimoshi.cyplay.com.doublenavigation.model.business.CatalogueLevel;
import ninja.cyplay.com.apilibrary.domain.executor.InteractorExecutor;
import ninja.cyplay.com.apilibrary.domain.executor.MainThreadExecutor;
import ninja.cyplay.com.apilibrary.domain.interactor.AbstractInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.CatalogueInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACatalogueLevel;

/**
 * Created by andre on 17-Aug-18.
 */

public class OfflineCatalogueInteractorImpl extends AbstractInteractor implements CatalogueInteractor {

    private IDatabaseHandler _databaseHandler;


    public OfflineCatalogueInteractorImpl(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        super(interactorExecutor, mainThreadExecutor);
        _databaseHandler = databaseHandler;
    }

    @Override
    public void getCatalogueLevel(String catalogueLevel, final ResourceRequestCallback<PR_ACatalogueLevel> getCatalogueLevelCallback) {
        JSONObject result = new JSONObject();

        try {
            // category
            JSONArray families = _databaseHandler.getFamily(catalogueLevel);
            if (families.length() > 0) {
                JSONObject family = families.getJSONObject(0);
                result.put("category", familyToCategory(family));
            }

            // sub_categories
            families = _databaseHandler.getFamiliesByLevel(catalogueLevel);
            JSONArray sub_categories = new JSONArray();
            for (int i = 0; i < families.length(); i++) {
                JSONObject family = families.getJSONObject(i);
                sub_categories.put(familyToCategory(family));
            }
            result.put("sub_categories", sub_categories);

        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        }

        Gson gson = new Gson();
        final PR_ACatalogueLevel resource = gson.fromJson(result.toString(), CatalogueLevel.class);
        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                getCatalogueLevelCallback.onSuccess(resource);
            }
        });
    }

    private JSONObject familyToCategory(JSONObject family) {
        JSONObject result = new JSONObject();

        try {
            if (family.has("id"))
                result.put("id", family.getString("id"));
            if (family.has("name")) {
                JSONArray name = family.getJSONArray("name");
                if (name.length() > 0)
                    result.put("name", name.getJSONObject(0).getString("text"));
            }
            if (family.has("path"))
                result.put("path", family.getString("path"));
            if (family.has("parent"))
                result.put("parent", family.getString("parent"));
            if (family.has("has_sub_families"))
                result.put("has_sub_families", family.getBoolean("has_sub_families"));
        } catch (JSONException e) {
            Log.d("Error", e.getMessage());
        };

        return result;
    }
}
