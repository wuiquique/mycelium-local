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
import ImageList from "@mui/material/ImageList";
import ImageListItem from "@mui/material/ImageListItem";
import Rating from "@mui/material/Rating";
import CommentTree from "../../../components/comments/CommentTree";

export default function Product() {
  const [prod, setProd] = useState({
    id: 1,
    name: "Cuchara de Oro",
    desc: "Una cuchara de ORO",
    brand: "Cosas de Oro S.A.",
    weight: 50,
    price: 50000,
    quantity: 2,
    categId: 1,
  });

  const [urls, setUrls] = useState([
    "https://falabella.scene7.com/is/image/FalabellaPE/770197465_1?wid=800&hei=800&qlt=70",
    "https://m.media-amazon.com/images/I/71g0Vo7zJUL.__AC_SY300_SX300_QL70_FMwebp_.jpg",
    "https://media.istockphoto.com/id/1136230616/es/foto/cuchara-dorada-aislada-sobre-fondo-blanco.jpg?s=612x612&w=0&k=20&c=v9lFtWCJWSflVyvkzx-GOvPTENDIh2uyLLyhEVTqwZY=",
  ]);

  const [categ, setCategs] = useState([
    { id: 1, name: "GOD" },
    { id: 2, name: "NonGOD" },
  ]);

  const [tech, setTech] = useState([
    { type: "Longitud", value: "20cm" },
    { type: "Color", value: "Dorado obviamente xdddd" },
  ]);

  const [comments, setComments] = useState([
    {
      author: "Test",
      content: "Test",
      votes: 10,
      replies: [
        {
          author: "Test",
          content: "Test",
          votes: 10,
          replies: [],
        },
        {
          author: "Test",
          content: "Test",
          votes: 10,
          replies: [],
        },
      ],
    },
    { author: "Test", content: "Test", votes: 10, replies: [] },
  ]);

  const categSelect = () => {
    let temp = "";
    for (let i = 0; i < categ.length; i++) {
      if (prod.categId === categ[i].id) {
        temp = categ[i].name;
      }
    }
    return temp;
  };

  const addCart = (e) => {
    e.preventDefault();
    let out = {
      prodId: prod.id,
      quantity: parseInt(e.target.cartQuantity.value),
      userId: 1,
    };
    axios.post("/api/user/cart", out).then((response) => {
      console.log(response.data);
    });
  };

  const ratingAvg = 5;
  const [rU, setRU] = useState(0);

  useEffect(() => {
    axios.get("/api/produc/[id]").then((response) => {
      setProd(response.data.prod);
      setUrls(response.data.urls);
      setTech(response.data.tech);
      setComments(response.data.comments);
    });
    axios.get("/api/category").then((response) => {
      setCategs(response.data);
    });
  }, []);

  return (
    <div className="flex justify-center text-center">
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
            <Typography variant="h5" mt={2}>
              {prod.name}
            </Typography>
            <Rating name="avgRating" value={ratingAvg} readOnly />
            <Grid2 container spacing={12}>
              <Grid2 xs={12} md={4}>
                <ImageList
                  sx={{ width: 230, height: 300 }}
                  cols={1}
                  rowHeight={164}
                >
                  {urls.map((e, i) => (
                    <ImageListItem key={i}>
                      <img src={urls[i]} />
                    </ImageListItem>
                  ))}
                </ImageList>
              </Grid2>
              <Grid2 xs={12} md={8}>
                <Card className="p-4" elevation={10}>
                  <div className="text-left">
                    <Typography variant="body1" mt={1}>
                      <b>Description:</b> {prod.desc}
                    </Typography>
                    <Typography variant="body1" mt={1}>
                      <b>Brand: </b> {prod.brand}
                    </Typography>
                    <Typography variant="body1" mt={1}>
                      <b>Weight: </b> {prod.weight}
                    </Typography>
                    {tech.map((e, i) => (
                      <Typography key={i} variant="body1" mt={1}>
                        <b>{e.type}: </b> {e.value}
                      </Typography>
                    ))}
                    <Typography variant="body1" mt={1}>
                      <b>Category: </b> {categSelect()}
                    </Typography>
                    <Typography variant="h6" mt={4} className="text-right">
                      Q. {prod.price}.00
                    </Typography>
                    <Typography variant="subtitle2" className="text-right">
                      <b>Quantity: </b> {prod.quantity}
                    </Typography>
                  </div>
                  <form onSubmit={addCart}>
                    <TextField
                      className="m-2"
                      label="Quantity"
                      variant="standard"
                      type={"number"}
                      name="cartQuantity"
                      inputProps={{ min: 0, max: prod.quantity }}
                    />
                    <Button
                      className="mt-6"
                      variant="outlined"
                      color="primary"
                      type="submit"
                    >
                      Add to Cart
                    </Button>
                  </form>
                </Card>
              </Grid2>
            </Grid2>
            <Card className="p-4" elevation={10}>
              <Typography variant="body1">Rate this product</Typography>
              <Rating
                name="userRating"
                value={rU}
                onChange={(e, n) => {
                  setRU(n ?? rU);
                }}
              />
              <br />
              <TextField
                className="mt-1"
                label="Comment"
                variant="standard"
                multiline
                name="user_commment"
              />
            </Card>
            <div className="text-left">
              <CommentTree comments={comments} />
            </div>
          </Card>
        </div>
      </Grid2>
    </div>
  );
}
