package moshimoshi.cyplay.com.doublenavigation.app.component;


import dagger.Component;
import moshimoshi.cyplay.com.doublenavigation.app.module.CalendarModule;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CalendarEventList;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.DashboardEventList;

/**
 * Created by damien on 19/04/16.
 */
@Component(
        modules = {
                CalendarModule.class
        },
        dependencies = {
                InteractorComponent.class,
                RuntimeComponent.class,
                BusComponent.class,
                OfflineInteractorComponent.class
        }
)
public interface CalendarComponent {

    CalendarEventList calendarEvents();

    DashboardEventList dashboardEvents();

}