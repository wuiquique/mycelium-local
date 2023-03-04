"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  TextField,
  Typography,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Select,
  MenuItem,
  FormControl,
  Card,
  Button,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import BackPage from "../../../components/BackPage";

export default function Category() {
  const [categ, setCategs] = useState([
    { id: 1, name: "GOD" },
    { id: 2, name: "NonGOD" },
  ]);

  const changeInput = (index, camp, value) => {
    let temp = [...categ];
    temp[index][camp] = value;
    console.log(temp);
    setCategs(temp);
  };

  const blurSave = (id, index) => {
    let post = {
      id: categ[index].id,
      name: categ[index].name,
    };
    axios.post(`/api/admin/categories/${id}`, post).then((response) => {
      setCategs(response.data);
    });
  };

  const deleteRow = (e, i) => {
    let temp = [...categ];
    temp.splice(i, 1);
    setCategs(temp);
  };

  const addRow = (e) => {
    let temp = [...categ];
    temp.push({ id: categ.length + 1, name: "" });
    setCategs(temp);
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
                    onClick={(e) => deleteRow(e, i)}
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
