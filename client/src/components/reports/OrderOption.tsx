import {
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
} from "@mui/material";
import { MdRemove } from "react-icons/md";

type Order = { name: string; order: "ASC" | "DESC" };

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
    <div className="grid grid-cols-[repeat(2,_minmax(0,_1fr))_auto] my-4 gap-1">
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
        <InputLabel>Ordenamiento</InputLabel>
        <Select
          label="Ordenamiento"
          value={value.order}
          onChange={(e) =>
            onChange({
              ...value,
              order: e.target.value as "ASC" | "DESC",
            })
          }
        >
          <MenuItem value="ASC">Ascendiente</MenuItem>
          <MenuItem value="DESC">Descendiente</MenuItem>
        </Select>
      </FormControl>
      <Button onClick={() => onChange(null)}>
        <MdRemove />
      </Button>
    </div>
  );
}
