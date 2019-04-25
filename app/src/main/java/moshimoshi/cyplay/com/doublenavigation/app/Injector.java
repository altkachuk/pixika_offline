package moshimoshi.cyplay.com.doublenavigation.app;

import moshimoshi.cyplay.com.doublenavigation.app.component.DBComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.DaggerDBComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.DaggerOfflineInteractorComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.InteractorComponentInstance;
import moshimoshi.cyplay.com.doublenavigation.app.component.OfflineInteractorComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.InteractorComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.DaggerInteractorComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.RepositoryComponent;
import moshimoshi.cyplay.com.doublenavigation.app.module.DBModule;
import moshimoshi.cyplay.com.doublenavigation.app.module.OfflineInteractorModule;
import moshimoshi.cyplay.com.doublenavigation.app.component.BusComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.CalendarComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.DaggerBusComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.DaggerCalendarComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.DaggerPlayRetailComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.DaggerPresenterComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.DaggerRuntimeComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.PlayRetailComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.PresenterComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.RuntimeComponent;
import moshimoshi.cyplay.com.doublenavigation.app.module.BusModule;
import moshimoshi.cyplay.com.doublenavigation.app.module.CalendarModule;
import moshimoshi.cyplay.com.doublenavigation.app.module.PresenterModule;
import moshimoshi.cyplay.com.doublenavigation.app.module.RepositoryModule;
import moshimoshi.cyplay.com.doublenavigation.app.module.RuntimeModule;
import ninja.cyplay.com.apilibrary.domain.component.ApplicationComponent;
import ninja.cyplay.com.apilibrary.domain.component.DaggerApplicationComponent;
import ninja.cyplay.com.apilibrary.domain.component.DaggerExecutorComponent;
import moshimoshi.cyplay.com.doublenavigation.app.component.DaggerRepositoryComponent;
import ninja.cyplay.com.apilibrary.domain.component.ExecutorComponent;
import ninja.cyplay.com.apilibrary.domain.module.ApplicationModule;
import ninja.cyplay.com.apilibrary.domain.module.ExecutorModule;
import ninja.cyplay.com.apilibrary.domain.module.InteractorModule;

public enum Injector {

    INSTANCE;

    PlayRetailComponent playRetailComponent;

    // private __ctor__
    Injector() {
    }

    void initializeComponent(PlayRetailApp playRetailApp) {

        BusComponent busComponent =
                DaggerBusComponent.builder().busModule(new BusModule(playRetailApp)).build();

        RuntimeComponent runtimeComponent =
                DaggerRuntimeComponent.builder()
                        .runtimeModule(new RuntimeModule(playRetailApp, busComponent.provideBus()))
                        .busComponent(busComponent)
                        .build();

        ApplicationComponent applicationComponent =
                DaggerApplicationComponent.builder()
                        .applicationModule(new ApplicationModule(playRetailApp))
                        .build();

        ExecutorComponent executorComponent =
                DaggerExecutorComponent.builder()
                        .executorModule(new ExecutorModule())
                        .build();

        RepositoryComponent repositoryComponent =
                DaggerRepositoryComponent.builder()
                        .applicationComponent(applicationComponent)
                        .executorComponent(executorComponent)
                        .repositoryModule(new RepositoryModule(playRetailApp, runtimeComponent.provideApiValue()))
                        .build();

        InteractorComponent interactorComponent =
                DaggerInteractorComponent.builder()
                        .applicationComponent(applicationComponent)
                        .executorComponent(executorComponent)
                        .repositoryComponent(repositoryComponent)
                        .interactorModule(new InteractorModule(runtimeComponent.provideApiValue()))
                        .build();

        InteractorComponentInstance.getInstance().setInteractorComponent(interactorComponent);

        DBComponent dbComponent =
                DaggerDBComponent.builder()
                        .applicationComponent(applicationComponent)
                        .dBModule(new DBModule())
                        .build();

        OfflineInteractorComponent offlineInteractorComponent =
                DaggerOfflineInteractorComponent.builder()
                        .applicationComponent(applicationComponent)
                        .dBComponent(dbComponent)
                        .executorComponent(executorComponent)
                        .offlineInteractorModule(new OfflineInteractorModule())
                        .build();

        CalendarComponent calendarComponent =
                DaggerCalendarComponent.builder()
                        .calendarModule(new CalendarModule(busComponent.provideBus(),
                                interactorComponent.event(), runtimeComponent.provideSellerContext(), runtimeComponent.provideApiValue()))
                        .interactorComponent(interactorComponent)
                        .runtimeComponent(runtimeComponent)
                        .busComponent(busComponent)
                        .offlineInteractorComponent(offlineInteractorComponent)
                        .build();

        PresenterComponent presenterComponent =
                DaggerPresenterComponent.builder()
                        .presenterModule(new PresenterModule())
                        .runtimeComponent(runtimeComponent)
                        .applicationComponent(applicationComponent)
                        .interactorComponent(interactorComponent)
                        .calendarComponent(calendarComponent)
                        .busComponent(busComponent)
                        .offlineInteractorComponent(offlineInteractorComponent)
                        .build();


        PlayRetailComponent playRetailComponent =
                DaggerPlayRetailComponent.builder()
                        .applicationComponent(applicationComponent)
                        .runtimeComponent(runtimeComponent)
                        .executorComponent(executorComponent)
                        .repositoryComponent(repositoryComponent)
                        .interactorComponent(interactorComponent)
                        .presenterComponent(presenterComponent)
                        .calendarComponent(calendarComponent)
                        .busComponent(busComponent)
                        .dBComponent(dbComponent)
                        .offlineInteractorComponent(offlineInteractorComponent)
                        .build();

        this.playRetailComponent = playRetailComponent;
    }

    public PlayRetailComponent getPlayRetailComponent() {
        return playRetailComponent;
    }

}
