import FilterOption from "@/components/reports/FilterOption";
import OrderOption from "@/components/reports/OrderOption";
import { useTexts } from "@/hooks/textContext";
import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Chip,
  Divider,
  FormControl,
  Unstable_Grid2 as Grid2,
  InputLabel,
  MenuItem,
  OutlinedInput,
  Select,
  Typography,
} from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import axios from "axios";
import { useCallback, useEffect, useMemo, useState } from "react";
import { MdAdd } from "react-icons/md";
import BackPage from "../../../components/BackPage";

export function Page() {
  const texts = useTexts();

  const [reports, setReports] = useState<
    Record<
      string,
      {
        name: string;
        tableName: string;
        columns: {
          name: string;
          displayName: string;
          type: "TEXT" | "INTEGER" | "FLOATING" | "DATETIME";
        }[];
        preconfigurations: Record<
          string,
          {
            name: string;
            filters: {
              name: string;
              operation: "EQ" | "GT" | "LT" | "GTEQ" | "LTEQ" | "LIKE";
              value: any;
            }[];
            sorts: { name: string; order: "ASC" | "DESC" }[];
          }
        >;
      }
    >
  >({});
  const [selectedReportType, setSelectedReportType] = useState<string>("");
  const report = useMemo<typeof reports[number] | undefined>(
    () => reports[selectedReportType],
    [reports, selectedReportType]
  );

  const [selectedPreconfiguration, setSelectedPreconfiguration] =
    useState<string>("");
  useEffect(() => {
    if (!report) setSelectedPreconfiguration("");
  }, [report]);
  const preconfiguration = useMemo<
    typeof reports[number]["preconfigurations"][string] | null
  >(
    () =>
      report && selectedPreconfiguration !== ""
        ? report.preconfigurations[selectedPreconfiguration]
        : null,
    [report, selectedPreconfiguration]
  );

  const [sorts, setOrders] = useState<
    { name: string; order: "ASC" | "DESC" }[]
  >([]);
  const [filters, setFilters] = useState<
    {
      name: string;
      operation: "EQ" | "GT" | "LT" | "GTEQ" | "LTEQ" | "LIKE";
      value: any;
    }[]
  >([]);

  const [selectedFields, setSelectedFields] = useState<string[]>([]);
  const [genHeaders, setGenHeaders] = useState<
    { name: string; displayName: string }[]
  >([]);

  const [generatedReport, setGeneratedReport] = useState<
    Record<string, any>[] | null
  >(null);

  const generateReport = useCallback(() => {
    axios
      .post("/api/reports/generate", {
        reportType: selectedReportType,
        selected: selectedFields,
        filters: (preconfiguration?.filters ?? []).concat(
          filters.flatMap((f) => {
            let col = report?.columns?.find((c) => c.name === f.name);
            if (!col) return [];
            if (col.type === "INTEGER") {
              return {
                ...f,
                value: parseInt(f.value, 10),
              };
            } else if (col.type === "FLOATING") {
              return {
                ...f,
                value: parseFloat(f.value),
              };
            }
            return f;
          })
        ),
        sorts: (preconfiguration?.sorts ?? []).concat(sorts),
      })
      .then((res) => {
        setGenHeaders(
          (selectedFields.length > 0
            ? report?.columns?.filter(
                (c) => selectedFields.indexOf(c.name) > -1
              )
            : report?.columns) ?? []
        );
        setGeneratedReport(res.data);
      });
  }, [
    filters,
    preconfiguration?.filters,
    preconfiguration?.sorts,
    report?.columns,
    selectedFields,
    selectedReportType,
    sorts,
  ]);

  useEffect(() => {
    axios.get("/api/reports").then((response) => {
      setReports(response.data);
    });
  }, []);

  return (
    <div>
      <BackPage />
      <Card>
        <CardHeader title={texts.reportpage.title} />
        <CardContent>
          <FormControl className="m-1 w-full">
            <InputLabel id="reports-label">
              {texts.reportpage.report}
            </InputLabel>
            <Select
              className="w-full"
              labelId="reports-label"
              value={selectedReportType}
              input={<OutlinedInput label={texts.reportpage.report} />}
              onChange={(e) => setSelectedReportType(e.target.value)}
            >
              {Object.entries(reports).map(([id, r]) => (
                <MenuItem key={id} value={id}>
                  {r.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          {report ? (
            <>
              <FormControl className="m-1 w-full">
                <InputLabel id="reports-label">
                  {texts.reportpage.preconfiguration}
                </InputLabel>
                <Select
                  className="w-full"
                  labelId="reports-label"
                  value={
                    selectedPreconfiguration === ""
                      ? "custom"
                      : selectedPreconfiguration
                  }
                  input={
                    <OutlinedInput label={texts.reportpage.preconfiguration} />
                  }
                  onChange={(e) =>
                    setSelectedPreconfiguration(
                      e.target.value === "custom" ? "" : e.target.value
                    )
                  }
                >
                  <MenuItem value="custom">Custom</MenuItem>
                  {Object.entries(report.preconfigurations).map(([id, p]) => (
                    <MenuItem key={id} value={id}>
                      {p.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <FormControl className="m-1 w-full">
                <InputLabel id="multiple-fields-label">
                  {texts.reportpage.fields}
                </InputLabel>
                <Select
                  labelId="multiple-fields-label"
                  value={selectedFields}
                  onChange={(e) =>
                    setSelectedFields(
                      typeof e.target.value === "string"
                        ? e.target.value.split(",")
                        : e.target.value
                    )
                  }
                  multiple
                  input={<OutlinedInput label={texts.reportpage.fields} />}
                  renderValue={(selected) => (
                    <Box sx={{ display: "flex", flexWrap: "wrap", gap: 0.5 }}>
                      {selected.map((value) => (
                        <Chip
                          key={value}
                          label={
                            report.columns.find((c) => c.name === value)
                              ?.displayName ?? ""
                          }
                        />
                      ))}
                    </Box>
                  )}
                >
                  {report.columns.map((f, i) => (
                    <MenuItem key={f.name} value={f.name}>
                      {f.displayName}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <Divider variant="middle" />
              <Grid2 container spacing={2}>
                <Grid2 xs={12} md={6}>
                  <Typography variant="h6" component="div">
                    {texts.reportpage.filters}
                  </Typography>
                  {preconfiguration
                    ? preconfiguration.filters.map((o, i) => (
                        <FilterOption
                          key={i}
                          readOnly
                          report={report}
                          value={o}
                          onChange={(val) => {}}
                        />
                      ))
                    : null}
                  {filters.map((o, i) => (
                    <FilterOption
                      key={i}
                      report={report}
                      value={o}
                      onChange={(val) => {
                        const temp = [...filters];
                        if (val === null) {
                          temp.splice(i, 1);
                        } else {
                          temp[i] = val;
                        }
                        setFilters(temp);
                      }}
                    />
                  ))}
                  <Button
                    className="m-1 w-full"
                    onClick={() =>
                      setFilters([
                        ...filters,
                        { name: "", operation: "EQ", value: "" },
                      ])
                    }
                  >
                    <MdAdd />
                    &nbsp;{texts.reportpage.addfilterbutton}
                  </Button>
                </Grid2>
                <Grid2 xs={12} md={6}>
                  <Typography variant="h6" component="div">
                    {texts.reportpage.sort}
                  </Typography>
                  {preconfiguration
                    ? preconfiguration.sorts.map((o, i) => (
                        <OrderOption
                          key={i}
                          readOnly
                          report={report}
                          value={o}
                          onChange={(val) => {}}
                        />
                      ))
                    : null}
                  {sorts.map((o, i) => (
                    <OrderOption
                      key={i}
                      report={report}
                      value={o}
                      onChange={(val) => {
                        const temp = [...sorts];
                        if (val === null) {
                          temp.splice(i, 1);
                        } else {
                          temp[i] = val;
                        }
                        setOrders(temp);
                      }}
                    />
                  ))}
                  <Button
                    className="m-1 w-full"
                    onClick={() =>
                      setOrders([...sorts, { name: "", order: "ASC" }])
                    }
                  >
                    <MdAdd />
                    &nbsp;{texts.reportpage.addsortbutton}
                  </Button>
                </Grid2>
              </Grid2>
              <Divider variant="middle" />
              <Button className="m-1 w-full" onClick={generateReport}>
                {texts.reportpage.generatebutton}
              </Button>
            </>
          ) : null}
        </CardContent>
      </Card>
      <Card>
        <CardContent>
          {generatedReport !== null ? (
            <DataGrid
              columns={genHeaders.map((h) => ({
                field: h.name,
                headerName: h.displayName,
                flex: 1,
              }))}
              autoHeight
              rows={generatedReport.map((r, i) => ({ ...r, _id: i }))}
              initialState={{
                pagination: {
                  paginationModel: {
                    pageSize: 5,
                  },
                },
              }}
              pageSizeOptions={[5, 10, 15]}
              getRowId={(t) => t._id}
              rowSelection={false}
            />
          ) : null}
        </CardContent>
      </Card>
    </div>
  );
}
