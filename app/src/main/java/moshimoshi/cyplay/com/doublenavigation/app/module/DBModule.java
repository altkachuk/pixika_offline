package moshimoshi.cyplay.com.doublenavigation.app.module;

import android.content.Context;
import android.os.Build;

import atproj.cyplay.com.dblibrary.BuildConfig;
import atproj.cyplay.com.dblibrary.db.DatabaseHandler;
import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import dagger.Module;
import dagger.Provides;

/**
 * Created by andre on 23-Dec-18.
 */

@Module
public class DBModule {
    @Provides
    IDatabaseHandler databaseHandler(Context context) {
        return new DatabaseHandler(context, BuildConfig.APPLICATION_ID);
    }
}
