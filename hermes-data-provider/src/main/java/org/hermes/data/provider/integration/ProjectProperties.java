package org.hermes.data.provider.integration;


import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * Created by Almaz
 * Date: 15.10.2015
 */
public final class ProjectProperties {

    private static String DEFAULT_CONFIG_FILE = "hermes.properties";
    private static Properties properties;

    public static class SingletonHolder {
        public static final ProjectProperties HOLDER_INSTANCE = new ProjectProperties();
    }

    public static ProjectProperties getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private ProjectProperties(){
        try {
            properties = new Properties();
            File resourceFile = IOUtils.getResourceFile(ProjectProperties.class, DEFAULT_CONFIG_FILE);
            properties.load(new FileReader(resourceFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getReportHomeDirectory(){
        return properties.getProperty("reports.home-directory");
    }
    public String getQuotesHistoryHomeDirectory(){
        return properties.getProperty("quote-history.home-directory");
    }

}
