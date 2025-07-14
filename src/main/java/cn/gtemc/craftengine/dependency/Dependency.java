package cn.gtemc.craftengine.dependency;

import cn.gtemc.craftengine.dependency.relocation.Relocation;
import cn.gtemc.craftengine.util.PluginProperties;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;

public class Dependency {
    private final String id;
    private final String groupId;
    private final String rawArtifactId;
    private final List<Relocation> relocations;
    private final Predicate<Path> verifier;

    public Dependency(String id, String groupId, String artifactId, List<Relocation> relocations) {
        this.id = id;
        this.groupId = groupId;
        this.rawArtifactId = artifactId;
        this.relocations = relocations;
        this.verifier = (p) -> true;
    }

    public Dependency(String id, String groupId, String artifactId, List<Relocation> relocations, Predicate<Path> verifier) {
        this.id = id;
        this.groupId = groupId;
        this.rawArtifactId = artifactId;
        this.relocations = relocations;
        this.verifier = verifier;
    }

    public boolean verify(Path remapped) {
        return this.verifier.test(remapped);
    }

    public String id() {
        return id;
    }

    public String groupId() {
        return groupId;
    }

    public String rawArtifactId() {
        return rawArtifactId;
    }

    public List<Relocation> relocations() {
        return relocations;
    }

    public String toLocalPath() {
        return rewriteEscaping(groupId).replace(".", "/") + "/" + this.rawArtifactId + "/" + getVersion();
    }

    private static final String MAVEN_FORMAT = "%s/%s/%s/%s-%s.jar";

    public String mavenPath() {
        return String.format(MAVEN_FORMAT,
                rewriteEscaping(groupId).replace(".", "/"),
                rewriteEscaping(rawArtifactId),
                getVersion(),
                rewriteEscaping(rawArtifactId),
                getVersion()
        );
    }

    public String fileName(String classifier) {
        String name = this.rawArtifactId.toLowerCase(Locale.ENGLISH).replace('_', '-');
        String extra = classifier == null || classifier.isEmpty()
                ? ""
                : "-" + classifier;
        return name + "-" + this.getVersion() + extra + ".jar";
    }

    public String getVersion() {
        return PluginProperties.getValue(id);
    }

    public static String rewriteEscaping(String s) {
        return s.replace("{}", ".");
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dependency that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
