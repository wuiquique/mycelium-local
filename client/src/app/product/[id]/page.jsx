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
import Image from "next/image";
import BackPage from "../../../components/BackPage";
import { usePathname } from "next/navigation";
import { useUser } from "../../../hooks/userContext";

export default function Product({ params: { id } }) {
  const [prod, setProd] = useState({});

  const [urls, setUrls] = useState([]);

  const [categ, setCategs] = useState([]);

  const [tech, setTech] = useState([]);

  const [ratings, setRatings] = useState([]);

  const [ratingAvg, setRAvg] = useState([]);

  const [user, setUser] = useUser();

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

  useEffect(() => {
    axios.get(`/api/product/${id}`).then((response) => {
      setProd(response.data);
      //    setUrls(response.data.urls);
      //  setTech(response.data.tech);
      //setComments(response.data.comments);
    });
    axios.get("/api/categories/").then((response) => {
      setCategs(response.data);
    });
    axios.get(`/api/pictures/product/${id}`).then((response) => {
      setUrls(response.data);
    });
    axios.get(`/api/technical/product/${id}`).then((response) => {
      setTech(response.data);
    });
    axios.get(`/api/product/rating/${id}`).then((response) => {
      setRatings(response.data);
      for (const rat of response.data) {
        if (rat.userId === user.id) {
          setRU(rat.rating);
        }
      }
    });
    axios.get(`/api/product/rating/avg/${id}`).then((response) => {
      setRAvg(response.data);
    });
  }, [id]);

  const categSelect = () => {
    let temp = "";
    for (let i = 0; i < categ.length; i++) {
      if (prod.categorieId === categ[i].id) {
        temp = categ[i].name;
      }
    }
    return temp;
  };

  const addCart = (e) => {
    e.preventDefault();
    let out = {
      international: false,
      productId: prod.id,
      quantity: parseInt(e.target.cartQuantity.value),
    };
    axios.put("/api/user/cart", out).then((response) => {
      console.log(response.data);
    });
  };

  const [rU, setRU] = useState(0);

  const changeRating = (e, n) => {
    setRU(n ?? rU);
    for (const rat of ratings) {
      if (rat.userId === user.id) {
        let temp = {
          userId: user.id,
          rating: n,
        };
        axios.put(`/api/product/rating/${id}`, temp).then((response) => {});
        axios.get(`/api/product/rating/avg/${id}`).then((response) => {
          setRAvg(response.data);
        });
        return;
      }
    }
    let post = {
      userId: user.id,
      rating: n,
    };
    axios.post(`/api/product/rating/${id}`, post).then((response) => {});
    axios.get(`/api/product/rating/avg/${id}`).then((response) => {
      setRAvg(response.data);
    });
  };

  const submitComment = (e) => {
    e.preventDefault();
    let post = {
      userId: "",
      productId: "",
      commentId: "",
      message: e.target.user_commment.value,
    };
    axios
      .post("ruta_comentario", post)
      .then((response) => {
        console.log(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <div className="justify-center">
      <BackPage />
      <Card className="p-4 mt-2" elevation={10} sx={{ width: "100%" }}>
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
        <Grid2 container spacing={2}>
          <Grid2 xs={12} md={4} width={1}>
            <ImageList sx={{ height: 300 }} cols={1} rowHeight={164}>
              {urls.map((e, i) => (
                <ImageListItem key={i}>
                  <img src={urls[i].url} alt="" />
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
        <Card className="p-4 mt-2" elevation={10}>
          <Typography variant="body1">Rate this product</Typography>
          <Rating name="userRating" value={rU} onChange={changeRating} />
          <br />
          <form onSubmit={submitComment}>
            <TextField
              className="mt-1"
              label="Comment"
              variant="standard"
              multiline
              name="user_commment"
            />
            <Button variant="outlined" type="submit">
              Post Comment
            </Button>
          </form>
        </Card>
        <Card className="p-4 mt-1" elevation={10}>
          <CommentTree comments={comments} />
        </Card>
      </Card>
    </div>
  );
}
