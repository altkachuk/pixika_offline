package moshimoshi.cyplay.com.doublenavigation.app.component;

/**
 * Created by romainlebouc on 28/08/16.
 */
public class InteractorComponentInstance {

    private InteractorComponent interactorComponent;
    /** Constructeur privé */
    private InteractorComponentInstance()
    {}

    /** Instance unique non préinitialisée */
    private static InteractorComponentInstance INSTANCE = null;

    /** Point d'accès pour l'instance unique du singleton */
    public static InteractorComponentInstance getInstance()
    {
        if (INSTANCE == null)
        { 	INSTANCE = new InteractorComponentInstance();
        }
        return INSTANCE;
    }

    public InteractorComponent getInteractorComponent() {
        return interactorComponent;
    }

    public void setInteractorComponent(InteractorComponent interactorComponent) {
        this.interactorComponent = interactorComponent;
    }
}

