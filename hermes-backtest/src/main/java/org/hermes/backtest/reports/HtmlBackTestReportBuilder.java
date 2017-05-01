package org.hermes.backtest.reports;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.hermes.backtest.BackTestResult;
import org.hermes.backtest.exceptions.ReportBuilderException;
import org.hermes.data.provider.integration.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by
 * Author:  Almaz
 * Date:    02.10.2015
 * Hour:    22
 * Minute:  36
 * <p>
 */
public class HtmlBackTestReportBuilder implements ReportBuilder {

    public static final String DEFAULT_TEMPLATE = "HtmlBackTestReportTemplate.vm";
    private static String JS_FILE_NAME = "jquery.js";
    private List<BackTestResult> backTestResults;
    private Template velocityTemplate;
    private VelocityContext velocityContext;

    public HtmlBackTestReportBuilder(BackTestResult backTestResults){
        this.backTestResults = new ArrayList<>();
        this.backTestResults.add(backTestResults);
    }

    public HtmlBackTestReportBuilder(List<BackTestResult> backTestResults){
        this.backTestResults = backTestResults;
    }

    @Override
    public void build() throws ReportBuilderException {
        try {
            VelocityEngine velocityEngine = new VelocityEngine();

            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
            File templates = IOUtils.getResourceFile(this.getClass(), "templates");
            velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, templates.getAbsolutePath());

            velocityEngine.init();
            velocityTemplate = velocityEngine.getTemplate(DEFAULT_TEMPLATE, "UTF-8");
            velocityContext = new VelocityContext();

            for (BackTestResult backTestResult : backTestResults) {
                Map<String, Object> properties = backTestResult.getProperties();

                for (String s : properties.keySet())
                    velocityContext.put(s, properties.get(s));
            }
        } catch (IOException | URISyntaxException e) {
            throw new ReportBuilderException(e);
        }
    }

    @Override
    public void save(OutputStream outputStream) throws ReportBuilderException {
        try (PrintWriter printWriter = new PrintWriter(outputStream)){
            velocityTemplate.merge(velocityContext, printWriter);
            printWriter.flush();
        }
    }
}
