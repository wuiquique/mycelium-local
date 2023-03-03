import {
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import React from "react";
import { MdRemove } from "react-icons/md";

type Filter = {
  index: number | null;
  comparison: "gt" | "lt" | "gteq" | "lteq" | "eq" | "neq";
  value: string;
};

export default function FilterOption({
  value,
  onChange,
  report,
}: {
  value: Filter;
  onChange: (value: Filter | null) => void;
  report:
    | {
        name: string;
        document: string;
        fields: { name: string; key: string; type: "text" | "number" }[];
      }
    | undefined;
}) {
  return (
    <div className="grid grid-cols-[repeat(3,_minmax(0,_1fr))_auto] my-4 gap-1">
      <FormControl>
        <InputLabel>Campo</InputLabel>
        <Select
          label="Campo"
          value={value.index ?? ""}
          onChange={(e) =>
            onChange({
              ...value,
              index:
                typeof e.target.value === "string"
                  ? parseInt(e.target.value, 10)
                  : e.target.value,
            })
          }
        >
          {report?.fields?.map((f, i) => (
            <MenuItem key={f.key} value={i}>
              {f.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControl>
        <InputLabel>Operación</InputLabel>
        <Select
          label="Operación"
          value={value.comparison}
          onChange={(e) =>
            onChange({
              ...value,
              comparison: e.target.value as
                | "gt"
                | "lt"
                | "gteq"
                | "lteq"
                | "eq"
                | "neq",
            })
          }
        >
          <MenuItem value="gt">Mayor que</MenuItem>
          <MenuItem value="gteq">Mayor o igual que</MenuItem>
          <MenuItem value="lt">Menor que</MenuItem>
          <MenuItem value="lteq">Menor o igual que</MenuItem>
          <MenuItem value="eq">Igual a</MenuItem>
          <MenuItem value="neq">No igual a</MenuItem>
        </Select>
      </FormControl>
      <TextField
        label="Valor"
        value={value.value}
        onChange={(e) =>
          onChange({
            ...value,
            value: e.target.value,
          })
        }
      />
      <Button onClick={() => onChange(null)}>
        <MdRemove />
      </Button>
    </div>
  );
}
