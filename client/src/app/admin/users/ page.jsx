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
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";

export default function adminusers() {
  return (
    <div>
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Table></Table>
        </Grid2>
      </Grid2>
    </div>
  );
}
