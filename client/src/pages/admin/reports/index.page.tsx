import FilterOption from "@/components/reports/FilterOption";
import OrderOption from "@/components/reports/OrderOption";
import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Chip,
  Divider,
  FormControl,
  InputLabel,
  MenuItem,
  OutlinedInput,
  Select,
  Typography,
  Unstable_Grid2 as Grid2,
} from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import axios from "axios";
import { useCallback, useEffect, useState } from "react";
import { MdAdd } from "react-icons/md";
import BackPage from "../../../components/BackPage";

export function Page() {
  const [availableReports, setAvailableReports] = useState<
    {
      name: string;
      tableName: string;
      columns: {
        name: string;
        displayName: string;
        type: "TEXT" | "INTEGER" | "FLOATING" | "DATETIME";
      }[];
    }[]
  >([]);

  useEffect(() => {
    axios.get("/api/reports").then((response) => {
      setAvailableReports(response.data);
    });
  }, []);

  const [selectedReport, setSelectedReport] = useState<number | null>(null);
  const [orders, setOrders] = useState<
    { name: string; order: "ASC" | "DESC" }[]
  >([]);
  const [filters, setFilters] = useState<
    {
      name: string;
      operation: "EQ" | "GT" | "LT" | "GTEQ" | "LTEQ" | "LIKE";
      value: any;
    }[]
  >([]);

  const [fields, setFields] = useState<string[]>([]);

  const [generated, setGenerated] = useState<Record<string, any>[]>([]);

  const generateReport = useCallback(() => {
    if (selectedReport === null) return;
    axios
      .post("/api/reports/generate", {
        reportType: availableReports[selectedReport].tableName,
        selected: fields,
        filters: filters,
        sorts: orders,
      })
      .then((response) => {
        setGenerated(response.data);
      });
  }, [availableReports, fields, filters, orders, selectedReport]);

  return (
    <div>
      <BackPage />
      <Card>
        <CardHeader title="Reportes" />
        <CardContent>
          <FormControl className="m-1 w-full">
            <InputLabel id="reports-label">Reporte</InputLabel>
            <Select
              className="w-full"
              labelId="reports-label"
              value={selectedReport ?? ""}
              input={<OutlinedInput label="Reporte" />}
              onChange={(e) =>
                setSelectedReport(e.target.value as number | null)
              }
            >
              {availableReports.map((r, i) => (
                <MenuItem key={r.tableName} value={i}>
                  {r.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl className="m-1 w-full">
            <InputLabel id="multiple-fields-label">Campos</InputLabel>
            <Select
              labelId="multiple-fields-label"
              value={fields}
              onChange={(e) =>
                setFields(
                  typeof e.target.value === "string"
                    ? e.target.value.split(",")
                    : e.target.value
                )
              }
              multiple
              input={<OutlinedInput label="Campos" />}
              renderValue={(selected) => (
                <Box sx={{ display: "flex", flexWrap: "wrap", gap: 0.5 }}>
                  {selected.map((value) => (
                    <Chip
                      key={value}
                      label={
                        availableReports[selectedReport ?? -1]?.columns?.find(
                          (c) => c.name === value
                        )?.displayName ?? ""
                      }
                    />
                  ))}
                </Box>
              )}
            >
              {selectedReport !== null
                ? availableReports[selectedReport].columns.map((f, i) => (
                    <MenuItem key={f.name} value={f.name}>
                      {f.displayName}
                    </MenuItem>
                  ))
                : null}
            </Select>
          </FormControl>
          <Divider variant="middle" />
          <Grid2 container spacing={2}>
            <Grid2 xs={12} md={6}>
              <Typography variant="h6" component="div">
                Filtros
              </Typography>
              {filters.map((o, i) => (
                <FilterOption
                  key={i}
                  report={
                    selectedReport !== null
                      ? availableReports[selectedReport]
                      : undefined
                  }
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
                &nbsp;Agregar
              </Button>
            </Grid2>
            <Grid2 xs={12} md={6}>
              <Typography variant="h6" component="div">
                Ordenamiento
              </Typography>
              {orders.map((o, i) => (
                <OrderOption
                  key={i}
                  report={
                    selectedReport !== null
                      ? availableReports[selectedReport]
                      : undefined
                  }
                  value={o}
                  onChange={(val) => {
                    const temp = [...orders];
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
                  setOrders([...orders, { name: "", order: "ASC" }])
                }
              >
                <MdAdd />
                &nbsp;Agregar
              </Button>
            </Grid2>
          </Grid2>
          <Divider variant="middle" />
          <Button className="m-1 w-full" onClick={generateReport}>
            Generar
          </Button>
        </CardContent>
      </Card>
      <Card>
        <CardContent>
          {selectedReport !== null ? (
            <DataGrid
              columns={
                fields.length > 0
                  ? fields.map((f) => ({
                      field:
                        availableReports[selectedReport].columns.find(
                          (c) => c.name === f
                        )?.name ?? "",
                      headerName:
                        availableReports[selectedReport].columns.find(
                          (c) => c.name === f
                        )?.displayName ?? "",
                      flex: 1,
                    }))
                  : availableReports[selectedReport].columns.map((c) => ({
                      field: c.name,
                      headerName: c.displayName,
                      flex: 1,
                    }))
              }
              autoHeight
              rows={generated.map((r, i) => ({ ...r, _id: i }))}
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
