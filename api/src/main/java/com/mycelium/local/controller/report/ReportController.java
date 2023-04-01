package com.mycelium.local.controller.report;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
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

class BasicFilter {
    public String name;
    public Operation operation;
    public Object value;
}

class BasicSort {
    public String name;
    public Order order;
}

class GenerateReportRequest {
    public String reportType;
    public Set<String> selected;
    public List<BasicFilter> filters;
    public List<BasicSort> sorts;
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/reports")
public class ReportController {

    private ReportManager reportManager;

    public ReportController(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Get("/")
    public List<Report> availableReports() {
        List<Report> res = Lists.newArrayList();
        for (var entry : Report.getAvailableReports().entrySet()) {
            res.add(entry.getValue());
        }
        return res;
    }

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
