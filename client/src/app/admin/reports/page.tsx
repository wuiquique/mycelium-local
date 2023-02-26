"use client";

import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Chip,
  FormControl,
  InputLabel,
  List,
  ListItem,
  MenuItem,
  OutlinedInput,
  Select,
  Typography,
  Unstable_Grid2 as Grid2,
} from "@mui/material";
import React, { useState } from "react";
import { MdAdd, MdRemove } from "react-icons/md";

export default function Reports() {
  const [availableReports, setAvailableReports] = useState([
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

  const [fields, setFields] = useState<number[]>([]);

  return (
    <Card>
      <CardHeader title="Reportes" />
      <CardContent>
        <Grid2 container spacing={2}>
          <Grid2 xs={12}>
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
          </Grid2>
          <Grid2 xs={12}>
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
          </Grid2>
          <Grid2 xs={12} md={6}>
            <Typography variant="h6" component="div">
              Filtros
            </Typography>
          </Grid2>
          <Grid2 xs={12} md={6}>
            <Typography variant="h6" component="div">
              Ordenamiento
            </Typography>
            {orders.map((o, i) => (
              <div
                className="grid grid-cols-[repeat(2,_minmax(0,_1fr))_auto] my-4 gap-1"
                key={i}
              >
                <FormControl>
                  <InputLabel id="ordering-select">Campo</InputLabel>
                  <Select
                    value={o.index ?? ""}
                    onChange={(e) => {
                      const temp = [...orders];
                      temp[i] = {
                        ...o,
                        index:
                          typeof e.target.value === "string"
                            ? parseInt(e.target.value, 10)
                            : e.target.value,
                      };
                      setOrders(temp);
                    }}
                    input={<OutlinedInput label="Campo" />}
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
                <FormControl>
                  <InputLabel id="ordering-select">Ordenamiento</InputLabel>
                  <Select
                    value={o.order}
                    onChange={(e) => {
                      const temp = [...orders];
                      temp[i] = {
                        ...o,
                        order: e.target.value as "asc" | "desc",
                      };
                      setOrders(temp);
                    }}
                    input={<OutlinedInput label="Ordenamiento" />}
                  >
                    <MenuItem value="asc">Ascendiente</MenuItem>
                    <MenuItem value="desc">Descendiente</MenuItem>
                  </Select>
                </FormControl>
                <Button
                  onClick={() => {
                    const temp = [...orders];
                    temp.splice(i, 1);
                    setOrders(temp);
                  }}
                >
                  <MdRemove />
                </Button>
              </div>
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
      </CardContent>
    </Card>
  );
}
