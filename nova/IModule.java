package nova;

/**
 * Created by Skeleton Man on 6/18/2016.
 */
public interface IModule {

    void onEnable();
    void onDisable();
    void toggleState();

    String getName();
    String getMetadata();

    void saveModule();
    void loadModule();
}
