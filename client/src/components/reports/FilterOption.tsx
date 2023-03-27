import {
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import { MdRemove } from "react-icons/md";

type Filter = {
  name: string;
  operation: "EQ" | "GT" | "LT" | "GTEQ" | "LTEQ" | "LIKE";
  value: any;
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
        tableName: string;
        columns: {
          name: string;
          displayName: string;
          type: "TEXT" | "INTEGER" | "FLOATING" | "DATETIME";
        }[];
      }
    | undefined;
}) {
  return (
    <div className="grid grid-cols-[repeat(3,_minmax(0,_1fr))_auto] my-4 gap-1">
      <FormControl>
        <InputLabel>Campo</InputLabel>
        <Select
          label="Campo"
          value={value.name ?? ""}
          onChange={(e) =>
            onChange({
              ...value,
              name: e.target.value,
            })
          }
        >
          {report?.columns?.map((f, i) => (
            <MenuItem key={f.name} value={f.name}>
              {f.displayName}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControl>
        <InputLabel>Operación</InputLabel>
        <Select
          label="Operación"
          value={value.operation}
          onChange={(e) =>
            onChange({
              ...value,
              operation: e.target.value as
                | "EQ"
                | "GT"
                | "LT"
                | "GTEQ"
                | "LTEQ"
                | "LIKE",
            })
          }
        >
          <MenuItem value="EQ">Igual a</MenuItem>
          <MenuItem value="GT">Mayor que</MenuItem>
          <MenuItem value="GTEQ">Mayor o igual que</MenuItem>
          <MenuItem value="LT">Menor que</MenuItem>
          <MenuItem value="LTEQ">Menor o igual que</MenuItem>
          <MenuItem value="LIKE">Similar a</MenuItem>
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
