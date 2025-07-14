package cn.gtemc.craftengine.dependency;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public enum DependencyRepository {
    MAVEN_CENTRAL("maven", "https://repo1.maven.org/maven2/") {
        @Override
        protected URLConnection openConnection(Dependency dependency) throws IOException {
            URLConnection connection = super.openConnection(dependency);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            return connection;
        }
    },
    MAVEN_CENTRAL_MIRROR("maven", "https://maven.aliyun.com/repository/public/");

    private final String url;
    private final String id;

    DependencyRepository(String id, String url) {
        this.url = url;
        this.id = id;
    }

    public String url() {
        return url;
    }

    public String id() {
        return id;
    }

    public static List<DependencyRepository> getByID(String id) {
        ArrayList<DependencyRepository> repositories = new ArrayList<>();
        for (DependencyRepository repository : values()) {
            if (id.equals(repository.id)) {
                repositories.add(repository);
            }
        }
        if (id.equals("maven") && Locale.getDefault() == Locale.SIMPLIFIED_CHINESE) {
            Collections.reverse(repositories);
        }
        return repositories;
    }

    protected URLConnection openConnection(Dependency dependency) throws IOException {
        @SuppressWarnings("deprecation") // 20
        URL dependencyUrl = new URL(this.url + dependency.mavenPath());
        return dependencyUrl.openConnection();
    }

    public byte[] downloadRaw(Dependency dependency) throws DependencyDownloadException {
        try {
            URLConnection connection = openConnection(dependency);
            try (InputStream in = connection.getInputStream()) {
                byte[] bytes = in.readAllBytes();
                if (bytes.length == 0) {
                    throw new DependencyDownloadException("Empty stream");
                }
                return bytes;
            }
        } catch (Exception e) {
            throw new DependencyDownloadException(e);
        }
    }

    public byte[] download(Dependency dependency) throws DependencyDownloadException {
        return downloadRaw(dependency);
    }

    public void download(Dependency dependency, Path file) throws DependencyDownloadException {
        try {
            Files.createDirectories(file.getParent());
            Files.write(file, download(dependency));
        } catch (IOException e) {
            throw new DependencyDownloadException(e);
        }
    }
}
