package org.hermes.backtest.reports;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.hermes.backtest.exceptions.ReportBuilderException;
import org.hermes.core.research.ResearchResult;
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
 * Created by Almaz
 * Date: 05.10.2015 21:54
 */
public class HtmlResearchReportBuilder implements ReportBuilder {

    private List<ResearchResult> researchResults;
    private String template;
    private Template velocityTemplate;
    private VelocityContext velocityContext;

    public HtmlResearchReportBuilder(List<ResearchResult> researchResults,
                                     String templateName) {
        this.researchResults = researchResults;
        this.template = templateName;
    }

    public HtmlResearchReportBuilder(ResearchResult researchResult,
                                     String templateName) {
        this.researchResults = new ArrayList<>();
        this.researchResults.add(researchResult);
        this.template = templateName;
    }

    @Override
    public void build() throws ReportBuilderException {
        try {
            VelocityEngine velocityEngine = new VelocityEngine();

            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
            File templates = IOUtils.getResourceFile(this.getClass(), "templates");
            velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, templates.getAbsolutePath());

            velocityEngine.init();
            velocityTemplate = velocityEngine.getTemplate(template, "UTF-8");

            this.velocityContext  = new VelocityContext();

            for (ResearchResult result : researchResults) {
                Map<String, Object> properties = result.getProperties();

                for (String s : properties.keySet())
                    velocityContext.put(s, properties.get(s));
            }

            velocityContext.put("researchResults", researchResults);
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
