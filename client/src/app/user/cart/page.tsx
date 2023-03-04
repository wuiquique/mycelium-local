"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Typography,
  Button,
  Card,
  CardMedia,
  CardContent,
  Box,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import Link from "next/link";
import UserCart from "../../../components/UserCart";
import BackPage from "../../../components/BackPage";

export default function Cart() {
  const [products, setProducts] = useState<any[]>([]);

  useEffect(() => {
    axios
      .get("/api/user/cart")
      .then((response) => {
        setProducts(response.data);
      })
      .catch((error) => {});
  }, []);

  const getTotal = () => {
    let total = 0;
    for (let i of products) {
      total += i.price;
    }
    return total;
  };

  return (
    <div>
      <BackPage />
      <Typography variant="h3" className="text-center">
        mycelium cart :)
      </Typography>
      <br />
      <Grid2 container spacing={2}>
        <Grid2 lg={8}>
          <UserCart
            products={products}
            cartOrCheckout="cart"
            onChange={setProducts}
          />
        </Grid2>
        <Grid2 lg={4}>
          <Card elevation={10}>
            <CardContent className="text-center">
              <Typography variant="h4">{`Total: Q.${getTotal()}.00`}</Typography>
              <Button
                className="mt-6"
                variant="outlined"
                color="secondary"
                size="large"
                component={Link}
                href="/user/checkout"
              >
                Checkout
              </Button>
            </CardContent>
            <CardMedia
              component="img"
              width="100%"
              image="/redwhite.png"
              alt="Mycelium Logo"
              className="p-2"
            />
          </Card>
        </Grid2>
      </Grid2>
    </div>
  );
}
