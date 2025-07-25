package cn.gtemc.craftengine.dependency;

import java.util.Set;

public interface DependencyManager {

    void loadDependencies(Set<Dependency> dependencies);

    ClassLoader obtainClassLoaderWith(Set<Dependency> dependencies);

    void close();
}
