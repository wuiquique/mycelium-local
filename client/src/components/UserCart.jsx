"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Card,
  CardMedia,
  CardContent,
  Typography,
  Button,
  TextField,
} from "@mui/material";

export default function UserCart({
  products,
  cartOrCheckout,
  onChange,
  orderId,
}) {
  const [localProds, setLocalProds] = useState(products);

  useEffect(() => {
    setLocalProds(products);
  }, [products]);

  const handleDeleteItem = (product) => {
    axios
      .put(`/api/user/cart/`, { ...product, quantity: 0 })
      .then((response) => {
        onChange(response.data);
      });
  };

  const handleCommentChange = (i, value) => {
    let temp = [...localProds];
    temp[i]["comment"] = value;
    setLocalProds(temp);
  };

  const handleBlur = () => {
    axios
      .patch(`/api/admin/orders/${orderId}`)
      .then((response) => {
        onChange(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <div>
      {localProds.length === 0 ? (
        <Card elevation={10} sx={{ display: "flex" }}>
          <Typography variant="h4">
            No Cuentas con Objetos en tu Carrito :)
          </Typography>
        </Card>
      ) : (
        localProds.map((e, i) => (
          <Card
            elevation={10}
            sx={{ display: "flex", marginBottom: "10px" }}
            key={i}
          >
            <CardMedia
              sx={{ width: 250 }}
              component="img"
              image={e.pictures[0]}
              alt="Imagen de Producto"
            />
            <CardContent>
              <div className="flex justify-start">
                <Typography variant="h4" className="mr-5">
                  {e.name}
                </Typography>
              </div>
              <div className="text-left">
                <Typography variant="h6">{e.description}</Typography>
                <Typography>
                  <strong>Brand:</strong>&nbsp;{e.brand}
                </Typography>
                <Typography>
                  <strong>Weight:</strong>&nbsp;{e.weight}
                </Typography>
              </div>
            </CardContent>
            <div className="text-right p-5">
              {cartOrCheckout === "cart" ? (
                <Button variant="outlined" onClick={() => handleDeleteItem(e)}>
                  Eliminar
                </Button>
              ) : (
                <></>
              )}
              <Typography variant="h4">{`Q.${e.price}.00`}</Typography>
              <Typography variant="subtitle1">
                <strong>{`Qty. ${e.quantity}`}</strong>
              </Typography>
              {cartOrCheckout === "details" ? (
                <div className="mt-8">
                  <Button
                    sx={{ minWidth: "100% " }}
                    variant="outlined"
                    color="secondary"
                  >
                    <Typography>
                      <strong>{e.status}</strong>
                    </Typography>
                  </Button>
                  <TextField
                    className="mt-2"
                    label="Status Comment"
                    value={e.comment}
                    variant="standard"
                    size="small"
                    multiline
                    rows={2}
                    onChange={(ev) => handleCommentChange(i, ev.target.value)}
                    onBlur={() => handleBlur()}
                  />
                </div>
              ) : (
                <></>
              )}
            </div>
          </Card>
        ))
      )}
    </div>
  );
}
