"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Card,
  CardContent,
  CardMedia,
  Button,
  TextField,
  Typography,
  Table,
  TableHead,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";

export default function adminusers() {

  const [users, setUsers] = ([])
  

  return (
    <div>
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>First Name</TableCell>
                <TableCell>Last Name</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>

            </TableBody>
          </Table>
        </Grid2>
      </Grid2>
    </div>
  );
}
