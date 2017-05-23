package nova.module;

import java.util.ArrayList;

/**
 * @author Kurt Dee
 * @since 6/18/2016
 */
public interface IModule {

    void onEnable();
    void onDisable();
    void toggleState();

    String getName();
    String getMetadata();

    ArrayList<String> getAliases();

    String getDescription();

    void loadModule();

    void saveModule();

}
