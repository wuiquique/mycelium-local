import { Button, Card, CardMedia, TextField, Typography } from "@mui/material";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import Select from "@mui/material/Select";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import { useEffect, useState } from "react";
import BackPage from "../../../../components/BackPage";

export function Page({ params: { id } }) {
  const [categ, setCategs] = useState<
    {
      id: number;
      name: string;
    }[]
  >([]);
  const [prod, setProd] = useState<{
    name: string;
    desc: string;
    brand: string;
    weight: number;
    quantity: number;
    price: number;
    categorieId: number;
  } | null>(null);
  const [pictures, setPictures] = useState<string[]>([]);
  const [technical, setTechnical] = useState<
    {
      type: string;
      value: string;
    }[]
  >([]);

  useEffect(() => {
    axios.get(`/api/product/${id}`).then((response) => {
      console.log(response.data);
      setProd(response.data);
      setPictures(response.data.pictures);
      setTechnical(response.data.technical);
    });
    axios.get("/api/categories").then((response) => {
      console.log(response.data);
      setCategs(response.data);
    });
  }, [id]);

  const handleSubmit = (e) => {
    e.preventDefault();
    let out = {
      name: e.currentTarget.product_name.value,
      desc: e.currentTarget.product_desc.value,
      categorieId: e.currentTarget.product_categorie.value,
      brand: e.currentTarget.product_brand.value,
      weight: e.currentTarget.product_weight.value,
      quantity: e.currentTarget.product_quantity.value,
      price: e.currentTarget.product_price.value,
      pictures: [
        e.currentTarget.product_img0.value,
        e.currentTarget.product_img1.value,
        e.currentTarget.product_img2.value,
      ],
      technical: [...technical],
    };
    console.log(out);
    axios.put(`/api/product/${id}`, out).then((response) => {
      console.log(response.data);
      if (response.data === "Success") {
        window.location.reload();
      }
    });
  };

  const handleChange = (e) => {
    if (!prod) return;
    let temp = { ...prod };
    temp.categorieId = e.target.value;
    setProd(temp);
  };

  const addRow = (e) => {
    let temp = [...technical];
    temp.push({ type: "", value: "" });
    setTechnical(temp);
  };

  const deleteRow = (e, i) => {
    let temp = [...technical];
    temp.splice(i, 1);
    setTechnical(temp);
  };

  const changeInput = (i, c, v) => {
    let temp = [...technical];
    temp[i][c] = v;
    setTechnical(temp);
  };

  const changeImg = (i, u) => {
    let temp = [...pictures];
    temp[i] = u;
    setPictures(temp);
  };

  return (
    <div>
      <BackPage />
      <Grid2 container spacing={2}>
        <div className="justify-center text-center">
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
            <Typography variant="h5" mt={2}>
              Update product
            </Typography>
            {prod !== null ? (
              <form className="mt-8 mb-11" onSubmit={handleSubmit}>
                <Grid2 container spacing={2}>
                  <Grid2 xs={12} md={6}>
                    <Card className="p-4" elevation={10} sx={{ height: 200 }}>
                      <TextField
                        className="mt-1"
                        label="Name"
                        variant="standard"
                        name="product_name"
                        defaultValue={prod.name}
                      />
                      <br />
                      <TextField
                        className="mt-1"
                        label="Description"
                        variant="standard"
                        name="product_desc"
                        defaultValue={prod.desc}
                      />
                      <br />
                      <TextField
                        className="mt-1"
                        label="Brand"
                        variant="standard"
                        name="product_brand"
                        defaultValue={prod.brand}
                      />
                    </Card>
                  </Grid2>
                  <Grid2 xs={12} md={6}>
                    <Card className="p-4" elevation={10} sx={{ height: 200 }}>
                      <TextField
                        className="mt-1"
                        label="Weight"
                        variant="standard"
                        name="product_weight"
                        defaultValue={prod.weight}
                      />
                      <br />
                      <TextField
                        className="mt-1"
                        label="Price"
                        variant="standard"
                        name="product_price"
                        defaultValue={prod.price}
                      />
                      <br />
                      <FormControl
                        variant="standard"
                        sx={{ m: 1, minWidth: 215 }}
                      >
                        <InputLabel id="form-id-categorie">
                          Categorie
                        </InputLabel>
                        <Select
                          labelId="form-id-categorie"
                          name="product_categorie"
                          defaultValue={prod.categorieId}
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
                        {[
                          ...pictures,
                          "/default.jpg",
                          "/default.jpg",
                          "/default.jpg",
                        ]
                          .slice(0, 3)
                          .map((p, i) => (
                            <Grid2 lg={4} key={i}>
                              <div className="p-4">
                                <CardMedia
                                  component="img"
                                  height="100%"
                                  image={p}
                                  alt="Img2"
                                />
                                <TextField
                                  className="mt-1"
                                  label="URL"
                                  variant="standard"
                                  name={"product_img" + i.toString()}
                                  defaultValue={p}
                                  onChange={(e) => changeImg(1, e.target.value)}
                                />
                              </div>
                            </Grid2>
                          ))}
                      </Grid2>
                    </Card>
                  </Grid2>
                  <Grid2 lg={12}>
                    <Card className="p-4" elevation={10}>
                      <div>
                        <TextField
                          label="Quantity"
                          type="number"
                          variant="standard"
                          defaultValue={prod.quantity}
                          name="product_quantity"
                        />
                        <Typography variant="body1" mt={6}>
                          Technical Specifications
                        </Typography>
                      </div>
                      {technical.map((e, i) => (
                        <div key={i}>
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
                      <div>
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
                    <Button
                      className="mt-6"
                      variant="outlined"
                      color="secondary"
                      type="submit"
                    >
                      Update
                    </Button>
                  </Grid2>
                </Grid2>
              </form>
            ) : null}
          </Card>
        </div>
      </Grid2>
    </div>
  );
}
