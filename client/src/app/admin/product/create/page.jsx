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
import CardHeader from "@mui/material/CardHeader";
import Avatar from "@mui/material/Avatar";
import { red } from "@mui/material/colors";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";

export default function Create() {
  const [tech, setTech] = useState([{ type: "", value: "" }]);

  const handleSubmit = (e) => {
    e.preventDefault();
    let out = {
      name: e.target.product_name.value,
      desc: e.target.product_desc.value,
      categId: e.target.product_categorie.value,
      brand: e.target.product_brand.value,
      weight: e.target.product_weight.value,
      quantity: e.target.product_quantity.value,
      price: e.target.product_price.value,
      urls: [
        e.target.product_img1.value,
        e.target.product_img2.value,
        e.target.product_img3.value,
      ],
      tech: [...tech],
    };
    axios.post("/api/product", out).then((response) => {
      console.log(response.data);
      if (response.data === "Succes") {
        location.reload();
      }
    });
  };

  const [age, setAge] = useState("");

  const handleChange = (e) => {
    setAge(e.target.value);
  };

  const addRow = (e) => {
    let temp = [...tech];
    temp.push({ type: "", value: "" });
    setTech(temp);
  };

  const deleteRow = (e, i) => {
    let temp = [...tech];
    temp.splice(i, 1);
    setTech(temp);
  };

  const changeInput = (i, c, v) => {
    let temp = [...tech];
    temp[i][c] = v;
    setTech(temp);
  };

  const [urls, setUrls] = useState([
    "/default.jpg",
    "/default.jpg",
    "/default.jpg",
  ]);

  const [categ, setCategs] = useState([
    { id: 1, name: "GOD" },
    { id: 2, name: "NonGOD" },
  ]);

  const changeImg = (i, u) => {
    let temp = [...urls];
    temp[i] = u;
    setUrls(temp);
  };

  return (
    <div>
      <Grid2 container spacing={2}>
        <div>
          <Card className="p-4" elevation={10} sx={{ width: "100%" }}>
            <div className="flex justify-center text-center">
              <CardMedia
                component="img"
                height="100%"
                image="/redwhite.png"
                alt="Mycelium Logo"
                sx={{ maxWidth: 345 }}
              />
            </div>
            <Typography variant="h5" mt={2} className="text-center">
              Create a new product
            </Typography>
            <form className="mt-8 mb-11" onSubmit={handleSubmit}>
              <Grid2 container spacing={2}>
                <Grid2 xs={12} md={6}>
                  <Card
                    className="p-4 justify-center text-center"
                    elevation={10}
                    sx={{ height: 200 }}
                  >
                    <TextField
                      className="mt-1"
                      label="Name"
                      variant="standard"
                      name="product_name"
                    />
                    <br />
                    <TextField
                      className="mt-1"
                      label="Description"
                      variant="standard"
                      name="product_desc"
                    />
                    <br />
                    <TextField
                      className="mt-1"
                      label="Brand"
                      variant="standard"
                      name="product_brand"
                    />
                  </Card>
                </Grid2>
                <Grid2 xs={12} md={6}>
                  <Card
                    className="p-4 justify-center text-center"
                    elevation={10}
                    sx={{ height: 200 }}
                  >
                    <TextField
                      className="mt-1"
                      label="Weight"
                      variant="standard"
                      name="product_weight"
                    />
                    <br />
                    <TextField
                      className="mt-1"
                      label="Price"
                      variant="standard"
                      name="product_price"
                    />
                    <br />
                    <FormControl
                      variant="standard"
                      sx={{ m: 1, minWidth: 215 }}
                    >
                      <InputLabel id="form-id-categorie">Categorie</InputLabel>
                      <Select
                        labelId="form-id-categorie"
                        name="product_categorie"
                        value={age}
                        onChange={handleChange}
                        label="Categories"
                      >
                        {categ.map((e, i) => (
                          <MenuItem key={i} value={e.id}>
                            {e.name}
                          </MenuItem>
                        ))}
                      </Select>
                    </FormControl>
                  </Card>
                </Grid2>
                <Grid2 lg={12}>
                  <Card className="p-4" elevation={10}>
                    <Grid2 container spacing={2}>
                      <Grid2 lg={4}>
                        <div className="p-4 justify-center text-center">
                          <CardMedia
                            component="img"
                            height="100%"
                            image={urls[0]}
                            alt="Img1"
                          />
                          <TextField
                            className="mt-1"
                            label="URL"
                            variant="standard"
                            name="product_img1"
                            onChange={(e) => changeImg(0, e.target.value)}
                          />
                        </div>
                      </Grid2>
                      <Grid2 lg={4}>
                        <div className="p-4">
                          <CardMedia
                            component="img"
                            height="100%"
                            image={urls[1]}
                            alt="Img2"
                          />
                          <TextField
                            className="mt-1"
                            label="URL"
                            variant="standard"
                            name="product_img2"
                            onChange={(e) => changeImg(1, e.target.value)}
                          />
                        </div>
                      </Grid2>
                      <Grid2 lg={4}>
                        <div className="p-4">
                          <CardMedia
                            component="img"
                            height="100%"
                            image={urls[2]}
                            alt="Img3"
                          />
                          <TextField
                            className="mt-1"
                            label="URL"
                            variant="standard"
                            name="product_img3"
                            onChange={(e) => changeImg(1, e.target.value)}
                          />
                        </div>
                      </Grid2>
                    </Grid2>
                  </Card>
                </Grid2>
                <Grid2 lg={12}>
                  <Card className="p-4" elevation={10}>
                    <div className="justify-center text-center">
                      <TextField
                        label="Quantity"
                        type="number"
                        variant="standard"
                        name="product_quantity"
                      />
                      <Typography variant="body1" mt={6}>
                        Technical Specifications
                      </Typography>
                    </div>
                    {tech.map((e, i) => (
                      <div key={i} className="justify-center text-center">
                        <TextField
                          className="m-1"
                          label="Type"
                          variant="standard"
                          value={e.type}
                          onChange={(e) =>
                            changeInput(i, "type", e.target.value)
                          }
                        />
                        <TextField
                          className="m-1"
                          label="Value"
                          variant="standard"
                          value={e.value}
                          onChange={(e) =>
                            changeInput(i, "value", e.target.value)
                          }
                        />
                        <Button
                          className="mt-6"
                          variant="outlined"
                          color="primary"
                          onClick={(e) => deleteRow(e, i)}
                        >
                          -
                        </Button>
                      </div>
                    ))}
                    <div className="justify-center text-center">
                      <Button
                        className="mt-6"
                        variant="outlined"
                        color="primary"
                        onClick={addRow}
                      >
                        +
                      </Button>
                    </div>
                  </Card>
                </Grid2>
                <Grid2 xs={12}>
                  <div className="justify-center text-center">
                    <Button
                      className="mt-6"
                      variant="outlined"
                      color="secondary"
                      type="submit"
                    >
                      Create
                    </Button>
                  </div>
                </Grid2>
              </Grid2>
            </form>
          </Card>
        </div>
      </Grid2>
    </div>
  );
}
