package moshimoshi.cyplay.com.doublenavigation.app.component;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import dagger.Component;
import moshimoshi.cyplay.com.doublenavigation.app.module.DBModule;
import ninja.cyplay.com.apilibrary.domain.component.ApplicationComponent;

/**
 * Created by andre on 23-Dec-18.
 */

@Component(modules = {DBModule.class}, dependencies = {ApplicationComponent.class})
public interface DBComponent {
    IDatabaseHandler databaseHandler();
}
