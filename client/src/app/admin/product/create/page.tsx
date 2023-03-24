"use client";

import { Button, Card, CardMedia, TextField, Typography } from "@mui/material";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import type { SelectChangeEvent } from "@mui/material/Select";
import Select from "@mui/material/Select";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import { FormEvent, useEffect, useState } from "react";
import BackPage from "../../../../components/BackPage";

export default function Create() {
  const [tech, setTech] = useState([{ type: "", value: "" }]);

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    let out = {
      name: e.currentTarget.product_name.value,
      desc: e.currentTarget.product_desc.value,
      categorieId: e.currentTarget.product_categorie.value,
      brand: e.currentTarget.product_brand.value,
      weight: e.currentTarget.product_weight.value,
      quantity: e.currentTarget.product_quantity.value,
      price: e.currentTarget.product_price.value,
      // urls: [
      //   e.currentTarget.product_img0.value,
      //   e.currentTarget.product_img1.value,
      //   e.currentTarget.product_img2.value,
      // ],
      // tech: [...tech],
    };
    axios.post("/api/product", out).then((response) => {
      console.log(response.data);
      if (response.data === "Succes") {
        location.reload();
      }
    });
  };

  const [age, setAge] = useState("");

  const handleChange = (e: SelectChangeEvent<string>) => {
    setAge(e.target.value);
  };

  const addRow = (e: React.MouseEvent<HTMLButtonElement>) => {
    let temp = [...tech];
    temp.push({ type: "", value: "" });
    setTech(temp);
  };

  const deleteRow = (e: React.MouseEvent<HTMLButtonElement>, i: number) => {
    let temp = [...tech];
    temp.splice(i, 1);
    setTech(temp);
  };

  const changeInput = (i: number, c: "type" | "value", v: string) => {
    let temp = [...tech];
    temp[i][c] = v;
    setTech(temp);
  };

  const [urls, setUrls] = useState([
    "/default.jpg",
    "/default.jpg",
    "/default.jpg",
  ]);

  const [categ, setCategs] = useState<
    {
      id: number;
      name: string;
    }[]
  >([]);

  const changeImg = (i: number, u: string) => {
    let temp = [...urls];
    temp[i] = u;
    setUrls(temp);
  };

  useEffect(() => {
    axios.get("/api/categories").then((res) => {
      setCategs(res.data);
    });
  }, []);

  return (
    <div>
      <BackPage />
      <Grid2 container spacing={2}>
        <div>
          <Card className="p-4" elevation={10} sx={{ width: "100%" }}>
            <div className="flex justify-center">
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
                    className="p-4 justify-center"
                    elevation={10}
                    sx={{ height: 200 }}
                  >
                    <TextField
                      className="mt-1 w-full"
                      label="Name"
                      variant="standard"
                      name="product_name"
                    />
                    <br />
                    <TextField
                      className="mt-1 w-full"
                      label="Description"
                      variant="standard"
                      name="product_desc"
                    />
                    <br />
                    <TextField
                      className="mt-1 w-full"
                      label="Brand"
                      variant="standard"
                      name="product_brand"
                    />
                  </Card>
                </Grid2>
                <Grid2 xs={12} md={6}>
                  <Card
                    className="p-4 justify-center"
                    elevation={10}
                    sx={{ height: 200 }}
                  >
                    <TextField
                      className="mt-1 w-full"
                      label="Weight"
                      variant="standard"
                      name="product_weight"
                    />
                    <br />
                    <TextField
                      className="mt-1 w-full"
                      label="Price"
                      variant="standard"
                      name="product_price"
                    />
                    <br />
                    <FormControl className="mt-1 w-full" variant="standard">
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
                      {urls.map((url, i) => (
                        <Grid2 lg={4} key={i}>
                          <div className="p-4 justify-center text-center">
                            <CardMedia
                              component="img"
                              height="100%"
                              image={url}
                              alt="Img1"
                            />
                            <TextField
                              className="mt-1"
                              label="URL"
                              variant="standard"
                              name={"product_img" + i}
                              value={url}
                              onChange={(e) => changeImg(i, e.target.value)}
                            />
                          </div>
                        </Grid2>
                      ))}
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
