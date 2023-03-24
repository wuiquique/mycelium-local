// @ts-nocheck
"use client";

import {
  Card,
  FormControl,
  MenuItem,
  Select,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import { useEffect, useState } from "react";

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [roles, setRoles] = useState([]);

  useEffect(() => {
    axios.get("/api/user/").then((response) => {
      setUsers(response.data);
    });
    axios.get("/api/role/").then((response) => {
      setRoles(response.data);
    });
  }, []);

  const changeInput = (index, camp, value) => {
    let temp = [...users];
    temp[index][camp] = value;
    setUsers(temp);
  };

  const changeSelect = (index, value) => {
    let temp = [...users];

    for (let i of roles) {
      if (i.id === value) {
        temp[index]["role"] = i.name;
        temp[index]["roleId"] = value;
      }
    }

    setUsers(temp);
  };

  const blurSave = (id, index) => {
    let post = {
      name: users[index].name,
      lastname: users[index].lastname,
      email: users[index].email,
      roleId: users[index].roleId,
    };
    axios.put(`/api/user/${id}`, post).then((response) => {
      setUsers(response.data);
      //console.log("noise");
    });
  };

  return (
    <div>
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Typography variant="h3" className="text-center">
            User Administration
          </Typography>
          <br />
          <Card elevation={10} sx={{ minHeight: 300 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>First Name</TableCell>
                  <TableCell>Last Name</TableCell>
                  <TableCell>Email</TableCell>
                  <TableCell>Role</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {users.length === 0 ? (
                  <></>
                ) : (
                  users.map((e, i) => (
                    <TableRow key={i}>
                      <TableCell>
                        <TextField
                          defaultValue={e.name}
                          variant="standard"
                          onChange={(ev) =>
                            changeInput(i, "name", ev.target.value)
                          }
                          onBlur={() => blurSave(e.id, i)}
                        />
                      </TableCell>
                      <TableCell>
                        <TextField
                          defaultValue={e.lastname}
                          variant="standard"
                          onChange={(ev) =>
                            changeInput(i, "lastname", ev.target.value)
                          }
                          onBlur={() => blurSave(e.id, i)}
                        />
                      </TableCell>
                      <TableCell>
                        <TextField
                          defaultValue={e.email}
                          variant="standard"
                          onChange={(ev) =>
                            changeInput(i, "email", ev.target.value)
                          }
                          onBlur={() => blurSave(e.id, i)}
                        />
                      </TableCell>
                      <TableCell>
                        <FormControl
                          variant="standard"
                          sx={{ m: 1, minWidth: 120 }}
                        >
                          <Select
                            value={e.roleId}
                            onChange={(ev) => {
                              changeSelect(i, ev.target.value);
                              blurSave(e.id, i);
                            }}
                          >
                            {roles.map((el, ind) => (
                              <MenuItem value={el.id} key={ind}>
                                {el.name}
                              </MenuItem>
                            ))}
                          </Select>
                        </FormControl>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </Card>
        </Grid2>
      </Grid2>
    </div>
  );
}
