package org.hermes.data.provider.integration.utils;

import java.io.File;

/**
 *
 *
 *
 * Created by almaz on 29.04.17.
 */
public class MQ4ExportFileNameRenamer {

    private static final String FILE_NAME_POSTFIX = "60.csv";

    public static void main(String[] args) {

        String path = "/Users/almaz/Desktop/Projects/Hermes/Hermes/hermesdataprovider/src/main/resources/" +
                "qoute_data/quotes/H1/Indexes/";

        File folder = new File(path);

        if(!folder.isDirectory())
            return;

        if(folder.listFiles() == null)
            return;

        if(folder.listFiles().length == 0)
            return;

        for (File file : folder.listFiles()) {
            String fileName = file.getName();

            if(fileName.contains(FILE_NAME_POSTFIX)) {
                String newFileName = fileName.substring(0, fileName.indexOf(FILE_NAME_POSTFIX)) + ".csv";

                File newFile = new File(folder + File.separator + newFileName);
                file.renameTo(newFile);
            }
        }
    }


}
