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
  FormControl
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";

export default function AdminUsers() {

  const [users, setUsers] = useState([
    {id: 1, first_name: 'Diego', last_name: 'Vallejo', email: 'd@gmail.com', rol: 'Admin', rol_id: 1}
  ])
  const [roles, setRoles] = useState([
    {rol: 'Admin', rol_id: 1},
    {rol: 'User', rol_id: 2},
    {rol: 'Emp', rol_id: 3},
  ])

  useEffect(() => {
    axios.get('/api/user')
    .then(response => {
      setUsers(response.data)
    })
    axios.get('/api/role')
    .then(response => {
      setRoles(response.data)
    })
  }, [])

  const changeInput = (index, camp, value) => {
    let temp = [...users]
    temp[index][camp] = value
    console.log(temp)
    setUsers(temp)
  }

  const changeSelect = (index, value) => {
    let temp = [...users]
    for (let i of roles) {
      if (i.rol_id === value) {
        temp[index]['rol'] = i.rol
        temp[index]['rol_id'] = value
      }
    }
    setUsers(temp)
    console.log(temp)

  }

  const blurSave = (id, index) => {
    let post = {
      first_name: users[index].first_name,
      last_name: users[index].last_name,
      email: users[index].email, 
      rol: users[index].rol_id
    }
    axios.post(`/api/user/${id}`, post)
    .then(response => {
      setUsers(response.data)
    })
  }

  return (
    <div>
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Typography variant='h3'>User Administration</Typography>
          <br/>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>First Name</TableCell>
                <TableCell>Last Name</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Rol</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {
                users.map((e, i) => (
                  <TableRow key={i} >
                    <TableCell>
                      <TextField
                        defaultValue={e.first_name}
                        variant='standard'
                        onChange={(ev) => changeInput(i, 'first_name', ev.target.value)}
                        onBlur={() => blurSave(e.id, i)}
                      />
                    </TableCell>
                    <TableCell>
                      <TextField
                        defaultValue={e.last_name}
                        variant='standard'
                      />
                    </TableCell>
                    <TableCell>
                      <TextField
                        defaultValue={e.email}
                        variant='standard'
                        onChange={(ev) => changeInput(i, 'last_name', ev.target.value)}
                        onBlur={() => blurSave(e.id, i)}
                      />
                    </TableCell>
                    <TableCell>
                      <FormControl variant='standard' sx={{ m: 1, minWidth: 120 }}>
                        <Select
                          value={e.rol_id}
                          onChange={(ev) => changeSelect(i, ev.target.value)}
                          onBlur={() => blurSave(e.id, i)}
                        >
                          <MenuItem value={e.rol_id}>{e.rol}</MenuItem>
                          {
                            roles.filter(r => r.rol_id !== e.rol_id).map((el, ind) => (
                              <MenuItem value={el.rol_id} key={ind}>{el.rol}</MenuItem>
                            ))
                          }
                        </Select>
                      </FormControl>
                    </TableCell>
                  </TableRow>
                ))
              }
            </TableBody>
          </Table>
        </Grid2>
      </Grid2>
    </div>
  );
}
