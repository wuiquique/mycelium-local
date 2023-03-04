"use client";

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
  List,
  ListItem,
  MenuItem,
  OutlinedInput,
  Select,
  TextField,
  Typography,
  Unstable_Grid2 as Grid2,
} from "@mui/material";
import React, { useState } from "react";
import { MdAdd, MdRemove } from "react-icons/md";
import BackPage from "../../../components/BackPage";

export default function Reports() {
  const [availableReports, setAvailableReports] = useState<
    {
      name: string;
      document: string;
      fields: { name: string; key: string; type: "text" | "number" }[];
    }[]
  >([
    {
      name: "Inventario",
      document: "inventory",
      fields: [
        { name: "Nombre", key: "name", type: "text" },
        { name: "Precio", key: "price", type: "number" },
      ],
    },
  ]);

  const [selectedReport, setSelectedReport] = useState<number | null>(null);
  const [orders, setOrders] = useState<
    { index: number | null; order: "asc" | "desc" }[]
  >([]);
  const [filters, setFilters] = useState<
    {
      index: number | null;
      comparison: "gt" | "lt" | "gteq" | "lteq" | "eq" | "neq";
      value: string;
    }[]
  >([]);

  const [fields, setFields] = useState<number[]>([]);

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
                <MenuItem key={r.document} value={i}>
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
                    ? e.target.value.split(",").map((d) => parseInt(d, 10))
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
                        availableReports[selectedReport ?? -1].fields[value]
                          .name
                      }
                    />
                  ))}
                </Box>
              )}
            >
              {selectedReport !== null
                ? availableReports[selectedReport].fields.map((f, i) => (
                    <MenuItem key={f.key} value={i}>
                      {f.name}
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
                    { index: null, comparison: "lt", value: "" },
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
                  setOrders([...orders, { index: null, order: "asc" }])
                }
              >
                <MdAdd />
                &nbsp;Agregar
              </Button>
            </Grid2>
          </Grid2>
          <Divider variant="middle" />
          <Button className="m-1 w-full">Generar</Button>
        </CardContent>
      </Card>
    </div>
  );
}
