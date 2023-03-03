import {
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
} from "@mui/material";
import React from "react";
import { MdRemove } from "react-icons/md";

type Order = { index: number | null; order: "asc" | "desc" };

export default function OrderOption({
  value,
  onChange,
  report,
}: {
  value: Order;
  onChange: (value: Order | null) => void;
  report:
    | {
        name: string;
        document: string;
        fields: { name: string; key: string; type: "text" | "number" }[];
      }
    | undefined;
}) {
  return (
    <div className="grid grid-cols-[repeat(2,_minmax(0,_1fr))_auto] my-4 gap-1">
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
        <InputLabel>Ordenamiento</InputLabel>
        <Select
          label="Ordenamiento"
          value={value.order}
          onChange={(e) =>
            onChange({
              ...value,
              order: e.target.value as "asc" | "desc",
            })
          }
        >
          <MenuItem value="asc">Ascendiente</MenuItem>
          <MenuItem value="desc">Descendiente</MenuItem>
        </Select>
      </FormControl>
      <Button onClick={() => onChange(null)}>
        <MdRemove />
      </Button>
    </div>
  );
}
