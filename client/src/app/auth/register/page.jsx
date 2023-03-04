"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import { Card, CardMedia, Button, TextField, Typography } from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import BackPage from "../../../components/BackPage";

export default function Register() {
  const handleSubmit = (e) => {
    e.preventDefault();
    let post = {
      email: e.target.email.value,
      password: e.target.password.value,
      first_name: e.target.first_name.value,
      last_name: e.target.last_name.value,
    };
    axios
      .post("ruta", post)
      .then((response) => {
        console.log(response.data);
        console.log("navigate -> ?");
      })
      .catch((response) => {
        console.log("error");
      });
  };

  return (
    <div>
      <BackPage />
      <div className="flex justify-center text-center">
        <Card className="p-4" elevation={10} sx={{ maxWidth: 345 }}>
          <CardMedia
            component="img"
            height="100%"
            image="/redwhite.png"
            alt="Mycelium Logo"
          />
          <Typography variant="h4" mt={2}>
            mycelium :)
          </Typography>
          <form className="mt-8 mb-11" onSubmit={handleSubmit}>
            <TextField
              label="First Name"
              variant="standard"
              name="first_name"
            />
            <TextField label="Last Name" variant="standard" name="last_name" />
            <TextField label="Email" variant="standard" name="email" />
            <TextField label="Password" variant="standard" name="password" />
            <br />
            <Button
              className="mt-6"
              variant="outlined"
              disableElevation
              color="secondary"
              type="submit"
            >
              Register
            </Button>
          </form>
        </Card>
      </div>
    </div>
  );
}
