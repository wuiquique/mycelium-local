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
import { DateTimePicker } from "@mui/x-date-pickers";

export default function UserAddress({ setAddress }) {
  const [date1, setDate1] = useState("2014-08-18T21:11:54");
  const [date2, setDate2] = useState("2014-08-18T21:11:54");

  return (
    <div>
      <Card className="p-4" elevation={10}>
        <CardContent>
          <Typography variant="h4">Mailing Address</Typography>
          <TextField
            sx={{ minWidth: "100%" }}
            label="Address"
            variant="standard"
          />
          <div>
            <TextField label="State" variant="standard" className="mr-1" />
            <TextField label="City" variant="standard" />
          </div>
          <div>
            <TextField label="Zip" variant="standard" className="mr-1" />
            <TextField label="Phone" variant="standard" />
          </div>
          <Typography>Delivery Range</Typography>
          <DateTimePicker
            renderInput={(props) => <TextField {...props} />}
            label="From"
            value={date1}
            onChange={(newValue) => {
              setDate1(newValue);
            }}
          />
          <DateTimePicker
            renderInput={(props) => <TextField {...props} />}
            label="To"
            value={date2}
            onChange={(newValue) => {
              setDate1(newValue);
            }}
          />
        </CardContent>
      </Card>
    </div>
  );
}
