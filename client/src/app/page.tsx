"use client";

import Image from "next/image";
import { Inter } from "@next/font/google";
import styles from "./page.module.css";
import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Card,
  CardContent,
  CardMedia,
  Button,
  TextField,
  Typography,
  Paper,
  Grid,
  ImageList,
  ImageListItem,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import CardHeader from "@mui/material/CardHeader";
import Avatar from "@mui/material/Avatar";
import { red } from "@mui/material/colors";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import Carousel from "react-material-ui-carousel";
import TaskAltIcon from "@mui/icons-material/TaskAlt";
import PaymentIcon from "@mui/icons-material/Payment";
import Stack from "@mui/material/Stack";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import Box from "@mui/material/Box";
import LocalShippingOutlinedIcon from "@mui/icons-material/LocalShippingOutlined";

const inter = Inter({ subsets: ["latin"] });

export default function Home() {
  const [prods, setProds] = useState([
    {
      id: 1,
      name: "Cuchara de Oro",
      desc: "Una cuchara de ORO",
      brand: "Cosas de Oro S.A.",
      weight: 50,
      price: 50000,
      quantity: 2,
      categId: 1,
      url: "https://falabella.scene7.com/is/image/FalabellaPE/770197465_1?wid=800&hei=800&qlt=70",
    },
    {
      id: 2,
      name: "Cuchara de Plata",
      desc: "Una cuchara de Plata",
      brand: "Cosas NO de Oro S.A.",
      weight: 40,
      price: 5000,
      quantity: 20,
      categId: 2,
      url: "https://m.media-amazon.com/images/I/41X8dYTqnKL.jpg",
    },
    {
      id: 3,
      name: "Cuchara de Bronce",
      desc: "Una cuchara de Bronce",
      brand: "Cosas NO de Oro S.A.",
      weight: 30,
      price: 500,
      quantity: 200,
      categId: 2,
      url: "https://thumbs.dreamstime.com/b/cuchara-de-bronce-con-el-emblema-del-coraz%C3%B3n-en-la-manija-47338611.jpg",
    },
  ]);

  const [categ, setCategs] = useState([
    { id: 1, name: "GOD" },
    { id: 2, name: "NonGOD" },
  ]);

  const [newp, setNewP] = useState([
    {
      id: 1,
      name: "Dummy",
      desc: "Dummy Desc",
      brand: "Dummy Brand",
      weight: 1212,
      price: 232,
      quantity: 300,
      categId: 2,
      url: "/default.jpg",
    },
    {
      id: 2,
      name: "Dummy",
      desc: "Dummy Desc",
      brand: "Dummy Brand",
      weight: 1212,
      price: 232,
      quantity: 300,
      categId: 2,
      url: "/default.jpg",
    },
    {
      id: 3,
      name: "Dummy",
      desc: "Dummy Desc",
      brand: "Dummy Brand",
      weight: 1212,
      price: 232,
      quantity: 300,
      categId: 2,
      url: "/default.jpg",
    },
    {
      id: 4,
      name: "Dummy",
      desc: "Dummy Desc",
      brand: "Dummy Brand",
      weight: 1212,
      price: 232,
      quantity: 300,
      categId: 2,
      url: "/default.jpg",
    },
    {
      id: 5,
      name: "Dummy",
      desc: "Dummy Desc",
      brand: "Dummy Brand",
      weight: 1212,
      price: 232,
      quantity: 300,
      categId: 2,
      url: "/default.jpg",
    },
    {
      id: 6,
      name: "Dummy",
      desc: "Dummy Desc",
      brand: "Dummy Brand",
      weight: 1212,
      price: 232,
      quantity: 300,
      categId: 2,
      url: "/default.jpg",
    },
    {
      id: 7,
      name: "Dummy",
      desc: "Dummy Desc",
      brand: "Dummy Brand",
      weight: 1212,
      price: 232,
      quantity: 300,
      categId: 2,
      url: "/default.jpg",
    },
  ]);

  return (
    <div className="flex justify-center text-center">
      <Card className="p-4" elevation={10} sx={{ width: "100%" }}>
        <div className="flex justify-center text-center">
          <CardMedia
            className="text-center"
            component="img"
            height="100%"
            image="/redwhite.png"
            alt="Mycelium Logo"
            sx={{ maxWidth: 345 }}
          />
        </div>
        <Typography variant="h4" mt={2}>
          Mycelium
        </Typography>
        <Typography variant="h6" m={1}>
          Home
        </Typography>
        <Card className="m-4" elevation={10}>
          <Grid2 container spacing={2}>
            <Grid2 lg={4}>
              <Grid2 container spacing={2}>
                <Grid2 lg={3}>
                  <div className="inine-block align-middle">
                    <TaskAltIcon fontSize="large" className="mt-4" />
                  </div>
                </Grid2>
                <Grid2 lg={9}>
                  <div className="text-left">
                    <Typography variant="body1" mt={2}>
                      <b>Warranty</b>
                    </Typography>
                    <Typography variant="body2">
                      Guaranteed quality and support in each purchase
                    </Typography>
                  </div>
                </Grid2>
              </Grid2>
            </Grid2>
            <Grid2 lg={4}>
              <Grid2 container spacing={2}>
                <Grid2 lg={3}>
                  <div className="inine-block align-middle">
                    <PaymentIcon fontSize="large" className="mt-4" />
                  </div>
                </Grid2>
                <Grid2 lg={9}>
                  <div className="text-left">
                    <Typography variant="body1" mt={2}>
                      <b>Payment</b>
                    </Typography>
                    <Typography variant="body2">
                      Variety of payment options for your convenience
                    </Typography>
                  </div>
                </Grid2>
              </Grid2>
            </Grid2>
            <Grid2 lg={4}>
              <Grid2 container spacing={2}>
                <Grid2 lg={3}>
                  <div className="inine-block align-middle">
                    <LocalShippingOutlinedIcon
                      fontSize="large"
                      className="mt-4"
                    />
                  </div>
                </Grid2>
                <Grid2 lg={9}>
                  <div className="text-left">
                    <Typography variant="body1" mt={2}>
                      <b>Shipment</b>
                    </Typography>
                    <Typography variant="body2">
                      Enjoy our home delivery
                    </Typography>
                  </div>
                </Grid2>
              </Grid2>
            </Grid2>
          </Grid2>
        </Card>
        <Typography variant="h5" mt={6} className="text-left">
          Best Sellers
        </Typography>
        <Carousel sx={{ width: 1 }}>
          {prods.map((e, i) => (
            <div key={i}>
              <Paper className="h-80">
                <ListItemButton>
                  <Grid2 container spacing={12}>
                    <Grid2 lg={4}>
                      <CardMedia component="img" height="100%" image={e.url} />
                    </Grid2>
                    <Grid2 lg={8}>
                      <div className="text-left">
                        <Typography variant="h5" mt={2}>
                          {e.name}
                        </Typography>
                        <Typography variant="body1" mt={2}>
                          <b>Description:</b> {e.desc} <br />
                          <b>Brand:</b> {e.brand}
                        </Typography>
                      </div>
                      <div className="text-right">
                        <Typography variant="h6" mt={6}>
                          Q. {e.price}.00
                        </Typography>
                        <Typography
                          variant="subtitle2"
                          mt={1}
                          className="text-right"
                        >
                          <b>Quantity: </b> {e.quantity}
                        </Typography>
                      </div>
                    </Grid2>
                  </Grid2>
                </ListItemButton>
              </Paper>
            </div>
          ))}
        </Carousel>
        <CardMedia
          component="img"
          height="100px"
          image="/banner.jpg"
          alt="banner"
          className="mt-4"
          sx={{ borderRadius: "16px" }}
        />

        <Box className="overflow-x-auto">
          <List component={Stack} direction="row" height={400} width={3060}>
            {newp.map((e, i) => (
              <ListItem disablePadding key={i}>
                <ListItemButton>
                  <Card elevation={10} className="p-4">
                    <CardMedia
                      component="img"
                      height="100%"
                      image={newp[i].url}
                    />
                    <Typography variant="h6">{newp[i].name}</Typography>
                  </Card>
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Box>
      </Card>
    </div>
  );
}
