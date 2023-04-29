package com.mycelium.local.controller.report;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mycelium.local.dynamic.reports.Report;
import com.mycelium.local.dynamic.reports.Report.Generator.Filter.Operation;
import com.mycelium.local.dynamic.reports.Report.Generator.Sort.Order;
import com.mycelium.local.dynamic.reports.ReportManager;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

/**
    Representa un filtro básico que se utiliza para generar informes.
    */
class BasicFilter {
    public String name;
    public Operation operation;
    public Object value;
}

/**

    Representa una ordenación básica que se utiliza para generar informes.
    */
class BasicSort {
    public String name;
    public Order order;
}

/**

    Representa la solicitud de generación de informe.
    */
class GenerateReportRequest {
    public String reportType;
    public Set<String> selected;
    public List<BasicFilter> filters;
    public List<BasicSort> sorts;
}

/**

    Controlador para la generación de informes.
    */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/reports")
public class ReportController {

    /**
    El administrador de informes utilizado para generar informes.
    */
    private ReportManager reportManager;

    /**
    Construye una nueva instancia de ReportController.
    @param reportManager el administrador de informes utilizado para generar informes.
    */
    public ReportController(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    /**
    Obtiene una lista de los informes disponibles.
    @return un mapa que contiene los nombres de los informes y los objetos de informe asociados.
    */
    @Get("/")
    public Map<String, Report> availableReports() {
        return Report.getAvailableReports();
    }

    /**
    Genera un informe utilizando los parámetros especificados.
    @param body los parámetros de solicitud utilizados para generar el informe.
    @return una lista de mapas que representan los registros del informe.
    */
    @Post("/generate")
    public List<Map<String, ?>> generate(@Body GenerateReportRequest body) {
        var generator = new Report.Generator(Report.getAvailableReports().get(body.reportType));
        for (var col : body.selected) {
            generator.select(col);
        }
        for (var filter : body.filters) {
            generator.where(filter.name, filter.operation, filter.value);
        }
        for (var sort : body.sorts) {
            generator.orderBy(sort.name, sort.order);
        }
        return reportManager.generateReport(generator);
    }
}
