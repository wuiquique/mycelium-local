import {
  Button,
  Card,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";
import BackPage from "../../../components/BackPage";

export function Page() {
  useEffect(() => {
    axios.get("/api/categories/").then((response) => {
      setCategs(response.data);
    });
  }, []);

  const [categ, setCategs] = useState([]);

  const changeInput = (index, camp, value) => {
    let temp = [...categ];
    temp[index][camp] = value;
    // console.log(temp);
    setCategs(temp);
  };

  const blurSave = (id, index) => {
    let post = {
      name: categ[index].name,
    };
    axios.put(`/api/categories/${id}`, post).then((response) => {
      // setCategs(response.data);
    });
  };

  const deleteRow = (id, i) => {
    let temp = [...categ];
    temp.splice(i, 1);
    setCategs(temp);
    axios.delete(`/api/categories/${id}`).then((response) => {
      console.log("Delete Succesfull");
    });
  };

  const addRow = (e) => {
    /*let temp = [...categ];
    temp.push({ id: categ.length + 1, name: "" });
    setCategs(temp);*/
    let post = { name: "New Category" };
    axios.post(`/api/categories/`, post).then((response) => {
      let temp = [...categ];
      temp.push({ id: response.data, name: "New Category" });
      setCategs(temp);
    });
  };

  return (
    <div className="justify-center">
      <BackPage />
      <Card className="p-4 mt-2" elevation={10} sx={{ width: "100%" }}>
        <Typography variant="h3" className="text-center">
          Categories Administration
        </Typography>{" "}
        <br />
        <Card elevation={10} sx={{ minHeight: 300 }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell>Delete</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {categ.map((e, i) => (
                <TableRow key={i}>
                  <TableCell>
                    <TextField
                      value={e.name}
                      variant="standard"
                      onChange={(ev) => changeInput(i, "name", ev.target.value)}
                      onBlur={() => blurSave(e.id, i)}
                    />
                  </TableCell>
                  <Button
                    className="mt-6"
                    variant="outlined"
                    color="primary"
                    onClick={() => deleteRow(e.id, i)}
                  >
                    Delete
                  </Button>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          <div className="flex justify-center text-center">
            <Button
              className="m-6"
              variant="outlined"
              color="secondary"
              onClick={addRow}
            >
              +
            </Button>
          </div>
        </Card>
      </Card>
    </div>
  );
}
