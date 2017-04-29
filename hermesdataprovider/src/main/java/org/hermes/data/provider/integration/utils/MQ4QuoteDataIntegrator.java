package org.hermes.data.provider.integration.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Утилитарный класс-тула для мерджинга данных из выгрузки внешних источников (MetTrader4)
 * и проектных ресурсов
 *
 * Created by almaz on 29.04.17.
 */
public class MQ4QuoteDataIntegrator {

    private static final String DEFAULT_QUOTE_DATA_SEPARATOR = ",";
    private static final String DEFAULT_QUOTE_DATE_FORMAT = "yyyy.MM.dd HH:mm";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DEFAULT_QUOTE_DATE_FORMAT);

    public static void main(String[] args){
        String path2Folder1 = "/Users/almaz/Desktop/Projects/Hermes/Hermes/hermesdataprovider/src/main/resources/QUOTES/H1/FOREX";
        String path2Folder2 = "/Users/almaz/Desktop/Projects/Hermes/Hermes/hermesdataprovider/src/main/resources/qoute_data/quotes/H1/Forex";

        File source = new File(path2Folder1);
        File target = new File(path2Folder2);

        new MQ4QuoteDataIntegrator().integrate(source, target);
    }

    public void integrate(File source, File target){

        Set<String> sourceFiles = convert2Set(source.listFiles());
        Set<String> targetFiles = convert2Set(target.listFiles());

        Set<String> filesToCopy = getFilesForCopy(sourceFiles, targetFiles);
        System.out.println("Files to just copy: " + filesToCopy.size());
        filesToCopy.forEach(f -> moveFile(source + File.separator + f,
                target + File.separator + f));

        Sets.SetView<String> files2Processing = Sets.intersection(sourceFiles, targetFiles);
        System.out.println("Files for matching: " + files2Processing.size());
        files2Processing.forEach(f -> matchQuote(source + File.separator + f,
                target + File.separator + f));

        files2Processing.forEach(f -> deleteFile(new File(source + File.separator + f).toPath()));
    }


    private void matchQuote(String source, String target) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(target);

        File sourceFile = new File(source);
        File targetFile = new File(target);

        try {
            List<String> sourceLines = Files.readAllLines(sourceFile.toPath(), Charset.forName("UTF-8"));
            List<String> targetLines = Files.readAllLines(targetFile.toPath(), Charset.forName("UTF-8"));

            List<String> result = matchQuote(sourceLines, targetLines);

            Files.write(targetFile.toPath(), result, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> matchQuote(List<String> sourceLines, List<String> targetLines) {
        Preconditions.checkNotNull(sourceLines);
        Preconditions.checkNotNull(targetLines);

        Stream<Pair<Date, String>> sourceData = sourceLines.stream().map(MQ4QuoteDataIntegrator::processQuoteDataLine);
        Stream<Pair<Date, String>> targetData = targetLines.stream().map(MQ4QuoteDataIntegrator::processQuoteDataLine);

        sourceData = sourceData.sorted(Comparator.comparing(Pair::getLeft));
        targetData = targetData.sorted(Comparator.comparing(Pair::getLeft));

        Set<Pair<Date, String>> resultSet = new TreeSet<>(Comparator.comparing(Pair::getLeft));

        sourceData.forEach(resultSet::add);
        targetData.forEach(resultSet::add);

        Stream<String> result = resultSet.stream().map(Pair::getRight);

        return result.collect(Collectors.toList());
    }

    private static Pair<Date, String> processQuoteDataLine(String quoteData) {
        Preconditions.checkNotNull(quoteData);

        String[] split = quoteData.split(DEFAULT_QUOTE_DATA_SEPARATOR);

        try {
            String date = split[0];
            String time = split[1];

            Date date1 = SIMPLE_DATE_FORMAT.parse( String.format("%s %s", date, time));
            return Pair.of(date1, quoteData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    /***
     * Метод, возвращает список имен файлов, которые необходимо
     * просто скопировать в целевое хранилище без каких-либо
     * дополнительных действий
     *
     * @param sourceFiles - список файлов, находящихся в папке источник
     * @param targetFiles - список файлов, находящихся в папке целевого хранилища
     * @return Список файлов, находящихся в папке источника, но отсутствующие в
     *                      папке целевого хранилища
     */
    private Set<String> getFilesForCopy(Set<String> sourceFiles, Set<String> targetFiles) {
        Preconditions.checkNotNull(sourceFiles);
        Preconditions.checkNotNull(targetFiles);

        Set<String> result = new HashSet<>();
        result.addAll(sourceFiles);

        result.removeAll(targetFiles);
        return result;
    }

    private void moveFile(String path1, String path2) {
        Preconditions.checkNotNull(path1);
        Preconditions.checkNotNull(path2);

        File source = new File(path1);
        File target = new File(path2);

        try {
            Files.move(source.toPath(), target.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Set<String> convert2Set(File[] files) {
        Preconditions.checkNotNull(files);

        Set<String> result = new HashSet<>();
        Arrays.stream(files).map(File::getName).forEach(result::add);

        return result;
    }
}
