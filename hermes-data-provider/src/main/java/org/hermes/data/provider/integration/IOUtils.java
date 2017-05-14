package org.hermes.data.provider.integration;

import org.hermes.core.entities.QuoteHistory;
import org.hermes.core.entities.constants.Quote;
import org.hermes.core.entities.price.TimeFrame;
import org.hermes.core.utils.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Almaz on 27.09.2015.
 */
public final class IOUtils {
    private IOUtils() {
    }

    public static File getResourceFile(Class clazz, String fileName) throws IOException, URISyntaxException {
        ClassLoader classLoader = clazz.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if(resource == null)
            throw new FileNotFoundException(fileName + " not found");
        return new File(resource.toURI());
    }

    public static QuoteHistory getHistoryData(String sourceFileName, Quote quote, TimeFrame timeFrame) throws Exception {
        File resourceFile = IOUtils.getResourceFile(ProjectProperties.class, sourceFileName);
        QuoteHistory quoteHistory = new CSVReader().read(resourceFile, quote, timeFrame);
        return quoteHistory;
    }

    public static void checkAndCreateFolder(Path path) throws IOException {
        Files.createDirectories(path.getParent());

        if(false == path.toFile().exists())
            Files.createFile(path);
    }
}
