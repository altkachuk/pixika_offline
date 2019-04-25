package moshimoshi.cyplay.com.doublenavigation.app.component;

/**
 * Created by romainlebouc on 28/08/16.
 */
public class OfflineInteractorComponentInstance {

    private OfflineInteractorComponent interactorComponent;
    /** Constructeur privé */
    private OfflineInteractorComponentInstance()
    {}

    /** Instance unique non préinitialisée */
    private static OfflineInteractorComponentInstance INSTANCE = null;

    /** Point d'accès pour l'instance unique du singleton */
    public static OfflineInteractorComponentInstance getInstance()
    {
        if (INSTANCE == null)
        { 	INSTANCE = new OfflineInteractorComponentInstance();
        }
        return INSTANCE;
    }

    public OfflineInteractorComponent getInteractorComponent() {
        return interactorComponent;
    }

    public void setInteractorComponent(OfflineInteractorComponent interactorComponent) {
        this.interactorComponent = interactorComponent;
    }
}

