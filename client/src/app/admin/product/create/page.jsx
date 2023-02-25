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
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";

export default function create() {
  return (
    <div className="flex justify-center text-center">
      <Grid2 container spacing={2}>
        <div>
          <Grid></Grid>
        </div>
      </Grid2>
    </div>
  );
}
