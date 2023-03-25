"use client";

import {
  Button,
  Card,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import Link from "next/link";
import React, { useEffect, useState } from "react";

export default function AdminProduct() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    axios.get("/api/product").then((response) => {
      setProducts(response.data);
      console.log(response.data);
    });
  }, []);

  const changeInput = (index, camp, value) => {
    let temp = [...products];
    temp[index][camp] = value;
    setProducts(temp);
  };

  const blurSave = (id, index) => {
    let post = {
      name: products[index].name,
      desc: products[index].desc,
      categorieId: products[index].categorie.id,
      brand: products[index].brand,
      weight: products[index].weight,
      quantity: products[index].quantity,
      price: products[index].price,
    };
    axios.put(`/api/product/${id}`, post).then((response) => {
      setProducts(response.data);
      console.log(response.data);
    });
  };

  return (
    <div>
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Typography variant="h3" className="text-center">
            Product Administration
          </Typography>
          <br />
          <Card elevation={10} sx={{ minHeight: 300 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Name</TableCell>
                  <TableCell>Description</TableCell>
                  <TableCell>Quantity</TableCell>
                  <TableCell>Price</TableCell>
                  <TableCell>Details</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {products.map((e, i) => (
                  <TableRow key={e.id}>
                    <TableCell>
                      <TextField
                        defaultValue={e.name}
                        variant="standard"
                        onChange={(ev) =>
                          changeInput(i, "name", ev.target.value)
                        }
                        onBlur={() => blurSave(e.id, i)}
                      />
                    </TableCell>
                    <TableCell>
                      <TextField
                        defaultValue={e.desc}
                        variant="standard"
                        onChange={(ev) =>
                          changeInput(i, "desc", ev.target.value)
                        }
                        onBlur={() => blurSave(e.id, i)}
                      />
                    </TableCell>
                    <TableCell>
                      <TextField
                        defaultValue={e.quantity}
                        variant="standard"
                        onChange={(ev) =>
                          changeInput(i, "quantity", ev.target.value)
                        }
                        onBlur={() => blurSave(e.id, i)}
                      />
                    </TableCell>
                    <TableCell>
                      <TextField
                        defaultValue={e.price}
                        variant="standard"
                        onChange={(ev) =>
                          changeInput(i, "price", ev.target.value)
                        }
                        onBlur={() => blurSave(e.id, i)}
                      />
                    </TableCell>
                    <TableCell>
                      <Button
                        variant="text"
                        component={Link}
                        href={`/admin/product/${e.id}/update`}
                      >
                        Details
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </Card>
        </Grid2>
      </Grid2>
    </div>
  );
}
