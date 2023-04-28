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
  readOnly = false,
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
  readOnly?: boolean;
}) {
  return (
    <div className="grid grid-cols-[repeat(3,_minmax(0,_1fr))_auto] my-4 gap-1">
      <FormControl>
        <InputLabel>Campo</InputLabel>
        <Select
          disabled={readOnly}
          label="Campo"
          value={value.name ?? ""}
          onChange={(e) =>
            !readOnly
              ? onChange({
                  ...value,
                  name: e.target.value,
                })
              : undefined
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
          disabled={readOnly}
          label="Operación"
          value={value.operation}
          onChange={(e) =>
            !readOnly
              ? onChange({
                  ...value,
                  operation: e.target.value as
                    | "EQ"
                    | "GT"
                    | "LT"
                    | "GTEQ"
                    | "LTEQ"
                    | "LIKE",
                })
              : undefined
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
        disabled={readOnly}
        label="Valor"
        value={value.value}
        onChange={(e) =>
          !readOnly
            ? onChange({
                ...value,
                value: e.target.value,
              })
            : undefined
        }
      />
      {!readOnly ? (
        <Button onClick={() => onChange(null)}>
          <MdRemove />
        </Button>
      ) : null}
    </div>
  );
}
