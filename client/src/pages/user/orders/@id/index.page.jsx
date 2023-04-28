import React, { useEffect, useState } from "react";
import axios from "axios";
import Grid2 from "@mui/material/Unstable_Grid2";
import {
  Button,
  Card,
  CardMedia,
  CardContent,
  Typography,
} from "@mui/material";
import UserCart from "@/components/UserCart";
import BackPage from "@/components/BackPage";
import { useUser } from "@/hooks/userContext";

export function Page({ params: { id } }) {
  const [detalle, setDetalle] = useState({});
  const [producto, setProducto] = useState([]);
  const [user, setUser] = useUser();

  useEffect(() => {
    axios.get(`/api/user/order/${id}`).then((response) => {
      setDetalle(response.data);
      setProducto(response.data.productList);
      console.log(response.data.productList);
    });
  }, []);

  function descargar() {
    let temp = [];
    for (let p of producto) {
      let o = {
        productName: p.productName,
        userId: user.id,
        address: detalle.direction + ", " + detalle.state + ", " + detalle.city,
        categoryId: p.productCategorie,
        boughtPrice: p.productPrice,
        porcentage: 30,
        quantity: p.quantity,
        weight: p.weight,
        international: !(p.integOrderId === null),
      };
      console.log(o);
      temp.push(o);
    }
    axios
      .post(`/api/user/order/receipt`, temp, { responseType: "blob" })
      .then((response) => {
        const url = URL.createObjectURL(response.data);
        const nombreArchivo = "Factura.pdf";
        const link = document.createElement("a");
        link.href = url;
        link.download = nombreArchivo;
        link.click();
      });
  }

  const getTotal = () => {
    let total = 0;
    for (let i of producto) {
      total += i.productPrice;
    }
    return total;
  };

  return (
    <div>
      <Grid2 container spacing={2}>
        <Grid2 lg={8}>
          {producto.map((e, i) => (
            <Card
              key={i}
              elevation={10}
              sx={{ display: "flex", marginBottom: "10px" }}
            >
              <CardMedia
                sx={{ width: 250 }}
                component="img"
                image={e.pictures[0]}
                alt="Imagen de producto"
              />
              <CardContent>
                <div className="flex justify-start">
                  <Typography variant="h4" className="mr-5">
                    {e.productName}
                  </Typography>
                </div>
                <div className="text-left">
                  <Typography variant="h6">{e.productDesc}</Typography>
                  <Typography>
                    <strong>Brand:</strong>&nbsp;{e.productBrand}
                  </Typography>
                  <Typography>
                    <strong>Quantity:</strong>&nbsp;{e.quantity}
                  </Typography>
                  <Typography>
                    <strong>Status:</strong>&nbsp;{e.status.name}
                  </Typography>
                </div>
              </CardContent>
            </Card>
          ))}
        </Grid2>
        <Grid2 lg={4}>
          <Card elevation={10}>
            <CardContent className="text-center">
              <Typography variant="h4">Total: Q.{getTotal()}.00</Typography>
              <Button
                className="mt-6"
                variant="outlined"
                color="secondary"
                size="large"
                onClick={descargar}
              >
                Descargar Factura
              </Button>
            </CardContent>
          </Card>
        </Grid2>
      </Grid2>
    </div>
  );
}
