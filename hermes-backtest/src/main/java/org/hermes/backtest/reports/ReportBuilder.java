package org.hermes.backtest.reports;

import org.hermes.backtest.exceptions.ReportBuilderException;

import java.io.OutputStream;

/**
 * Created by Almaz on 01.10.2015.
 */
public interface ReportBuilder {

    void build() throws ReportBuilderException;

    void save(OutputStream stream) throws ReportBuilderException;
}
