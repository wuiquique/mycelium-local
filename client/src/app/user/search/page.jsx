"use client";

import React, { useEffect, useState, useTheme } from "react";
import axios from "axios";
import {
  Card,
  CardMedia,
  CardContent,
  Typography,
  Button,
  TextField,
  FormControl,
  InputLabel,
  Select,
  OutlinedInput,
  MenuItem,
  Box,
  Chip,
  Slider,
  Rating,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import { AiOutlineSearch } from "react-icons/ai";
import BackPage from "../../../components/BackPage";

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
  PaperProps: {
    style: {
      maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
      width: 250,
    },
  },
};

function valuetext(value) {
  return `${value}.00`;
}

export default function Search() {
  const [categories, setCategories] = useState([
    "Diamante",
    "Platino",
    "Oro",
    "Plata",
    "Bronce",
    "Hierro",
  ]);
  const [products, setProducts] = useState([
    {
      type: "local",
      id: "1",
      name: "Cuchara de Oro",
      description: "Muy Fina",
      quantity: "3",
      category: "Oro", //-> nombre
      brand: "Dioro",
      weight: "23",
      price: 75,
      pictures: [
        "https://falabella.scene7.com/is/image/FalabellaPE/770197465_1?wid=800&hei=800&qlt=70",
      ], //-> solo 1
    },
  ]);
  /*
    useEffect(() => {
        axios.get('/api/categories/')
        .then(response => {
            setCategories(response.data)
        })
        .catch(error => {
            console.log(error)
        })
    }, [])
    */
  const [value, setValue] = useState([20, 37]);

  const handleSliderChange = (event, newValue) => {
    setValue(newValue);
  };
  const [personName, setPersonName] = useState([]);

  const handleChange = (event) => {
    const {
      target: { value },
    } = event;
    setPersonName(
      // On autofill we get a stringified value.
      typeof value === "string" ? value.split(",") : value
    );
  };

  return (
    <div>
      <BackPage />
      <Typography variant="h3" className="text-center">
        Search Results
      </Typography>
      <br />
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Box sx={{ display: "flex", alignItems: "flex-end" }}>
            <AiOutlineSearch size="2rem" />
            <TextField sx={{ width: "100%" }} variant="standard" />
          </Box>
        </Grid2>
        <Grid2 lg={6}>
          <Typography variant="h6" className="text-center">
            Category Filter
          </Typography>
          <FormControl sx={{ width: "100%" }}>
            <InputLabel id="demo-multiple-chip-label">Chip</InputLabel>
            <Select
              labelId="demo-multiple-chip-label"
              id="demo-multiple-chip"
              multiple
              value={personName}
              onChange={handleChange}
              input={<OutlinedInput id="select-multiple-chip" label="Chip" />}
              renderValue={(selected) => (
                <Box sx={{ display: "flex", flexWrap: "wrap", gap: 0.5 }}>
                  {selected.map((value) => (
                    <Chip key={value} label={value} />
                  ))}
                </Box>
              )}
              MenuProps={MenuProps}
            >
              {categories.map((name) => (
                <MenuItem key={name} value={name}>
                  {name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid2>
        <Grid2 lg={6}>
          <Typography variant="h6" className="text-center">
            Price Filter ($)
          </Typography>
          <Slider
            className="mt-2"
            value={value}
            onChange={handleSliderChange}
            valueLabelDisplay="auto"
            getAriaValueText={valuetext}
            max={1000}
          />
        </Grid2>
        {products.map((e, i) => (
          <Grid2 lg={6} key={i}>
            <Button>
              <Card elevation={10} sx={{ display: "flex" }}>
                <CardMedia
                  sx={{ width: "50%" }}
                  component="img"
                  image={e.pictures[0]}
                  alt="Imagen de Producto"
                />
                <CardContent className="text-left">
                  <Typography variant="h5">{e.name}</Typography>
                  <Typography variant="h6">{e.description}</Typography>
                  <Typography variant="body1">
                    <strong>{e.brand}</strong>
                  </Typography>
                  <br />
                  <Typography variant="h4">$.{e.price}.00</Typography>
                  <br />
                  <Rating readOnly size="large" />
                </CardContent>
              </Card>
            </Button>
          </Grid2>
        ))}
      </Grid2>
    </div>
  );
}
