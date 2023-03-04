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
import CreditCard from "../../../components/CreditCard";
import UserAddress from "../../../components/UserAddress";
import UserCart from "../../../components/UserCart";
import BackPage from "../../../components/BackPage";

export default function Checkout() {
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
  const [address, setAddress] = useState({});

  useEffect(() => {
    axios
      .get("/api/user/cart")
      .then((response) => {
        setProducts(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  return (
    <div>
      <Typography variant="h2" className="text-center">
        Checkout
      </Typography>
      <br />
      <Grid2 container spacing={2}>
        <Grid2 lg={6}>
          <CreditCard />
        </Grid2>
        <Grid2 lg={6}>
          <UserAddress products={products} />
        </Grid2>
        <Grid2 lg={12}>
          <UserCart
            products={products}
            cartOrCheckout="checkout"
            onChange={setProducts}
            orderId={2}
          />
        </Grid2>
      </Grid2>
    </div>
  );
}
