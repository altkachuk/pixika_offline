package moshimoshi.cyplay.com.doublenavigation.app.component;

import dagger.Component;
import moshimoshi.cyplay.com.doublenavigation.app.module.RepositoryModule;
import ninja.cyplay.com.apilibrary.domain.component.ApplicationComponent;
import ninja.cyplay.com.apilibrary.domain.component.ExecutorComponent;
import ninja.cyplay.com.apilibrary.domain.repository.PlayRetailRepository;
import ninja.cyplay.com.apilibrary.domain.repository.dqe.DQERepository;
import retrofit.RequestInterceptor;

/**
 * Created by damien on 21/04/16.
 */
@Component(
        modules = {
                RepositoryModule.class
        },
        dependencies = {
                ApplicationComponent.class,
                ExecutorComponent.class
        }
)
public interface RepositoryComponent {

    PlayRetailRepository providePlayRetailRepository();
    RequestInterceptor provideRequestInterceptor();
    DQERepository provideDQEApiRepository();
}