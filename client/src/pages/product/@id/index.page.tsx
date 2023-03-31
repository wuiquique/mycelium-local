import { useTexts } from "@/hooks/textContext";
import { Button, Card, CardMedia, TextField, Typography } from "@mui/material";
import ImageList from "@mui/material/ImageList";
import ImageListItem from "@mui/material/ImageListItem";
import Rating from "@mui/material/Rating";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import { useCallback, useEffect, useState } from "react";
import BackPage from "../../../components/BackPage";
import CommentTree from "../../../components/comments/CommentTree";
import { useUser } from "../../../hooks/userContext";

export function Page({ params: { id } }) {
  const texts = useTexts();

  const [prod, setProd] = useState<{
    id: number;
    name: string;
    desc: string;
    brand: string;
    weight: number;
    categorieId: number;
    price: number;
    quantity: number;
  }>({} as any);

  const [urls, setUrls] = useState<{ url: string }[]>([]);

  const [categ, setCategs] = useState<{ id: number; name: string }[]>([]);

  const [tech, setTech] = useState<{ type: string; value: string }[]>([]);

  const [ratings, setRatings] = useState<
    { id: number; user: { id: number } }[]
  >([]);

  const [ratingAvg, setRAvg] = useState(0);

  const [user] = useUser();

  type Comment = {
    id: number;
    message: string;
    created: number;
    updated: number;
    productId: number;
    userName: string;
    votes: number | undefined;
    children: any[];
  };

  const [comments, setComments] = useState<Comment[]>([]);

  useEffect(() => {
    axios.get(`/api/product/${id}`).then((response) => {
      setProd(response.data);
      console.log(response.data);
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
    axios.get(`/api/product/rating/avg/${id}`).then((response) => {
      setRAvg(response.data);
    });
    axios.get(`/api/comment/${id}`).then((response) => {
      setComments(response.data);
    });
  }, [id]);

  useEffect(() => {
    axios.get(`/api/product/rating/${id}`).then((response) => {
      setRatings(response.data);
      //console.log(response.data);
      //console.log(user);
      for (const rat of response.data) {
        if (rat.user.id === user.id) {
          setRU(rat.rating);
        }
      }
    });
  }, [id, user]);

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
      if (rat.user.id === user.id) {
        let temp = {
          userId: user.id,
          rating: n,
        };
        axios.put(`/api/product/rating/${rat.id}`, temp).then((response) => {});
        axios.get(`/api/product/rating/avg/${id}`).then((response) => {
          setRAvg(response.data);
        });
        return;
      }
    }
    let post = {
      userId: user.id,
      productId: id,
      rating: n,
    };
    axios.post(`/api/product/rating/`, post).then((response) => {});
    axios.get(`/api/product/rating/avg/${id}`).then((response) => {
      setRAvg(response.data);
    });
  };

  const reloadComments = useCallback(() => {
    axios.get(`/api/comment/${id}`).then((response) => {
      setComments(response.data);
    });
  }, [id]);

  const submitComment = (e) => {
    e.preventDefault();
    axios
      .post(`/api/comment/${id}`, {
        commentId: null,
        message: e.target.user_commment.value,
      })
      .then(() => {
        e.target.reset();
        reloadComments();
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
            alt={texts.global.shopname}
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
                  <b>{texts.product.description}:</b> {prod.desc}
                </Typography>
                <Typography variant="body1" mt={1}>
                  <b>{texts.product.brand}: </b> {prod.brand}
                </Typography>
                <Typography variant="body1" mt={1}>
                  <b>{texts.product.weight}: </b> {prod.weight}
                </Typography>
                {tech.map((e, i) => (
                  <Typography key={i} variant="body1" mt={1}>
                    <b>{e.type}: </b> {e.value}
                  </Typography>
                ))}
                <Typography variant="body1" mt={1}>
                  <b>{texts.product.category}: </b> {categSelect()}
                </Typography>
                <Typography variant="h6" mt={4} className="text-right">
                  Q. {prod.price}.00
                </Typography>
                <Typography variant="subtitle2" className="text-right">
                  <b>{texts.product.quantity}: </b> {prod.quantity}
                </Typography>
              </div>
              {user.id !== null ? (
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
                    {texts.productpage.addtocartbutton}
                  </Button>
                </form>
              ) : null}
            </Card>
          </Grid2>
        </Grid2>
        {user.id !== null ? (
          <Card className="p-4 mt-2" elevation={10}>
            <Typography variant="body1">
              {texts.productpage.rateproduct}
            </Typography>
            <Rating name="userRating" value={rU} onChange={changeRating} />
            <br />
            <form onSubmit={submitComment}>
              <TextField
                className="block w-full min-h-[4rem]"
                multiline
                InputProps={{
                  className: "block w-full min-h-[4rem]",
                }}
                variant="standard"
                placeholder="Comment"
                name="user_commment"
              />
              <Button className="block" type="submit">
                {texts.comment.postbutton}
              </Button>
            </form>
          </Card>
        ) : null}
        <Card className="p-4 mt-1" elevation={10}>
          <Typography variant="body1">Comentarios</Typography>
          {comments.length > 0 ? (
            <CommentTree
              comments={comments}
              productId={prod.id}
              onPostComment={reloadComments}
            />
          ) : (
            <Typography variant="body2">No hay comentarios</Typography>
          )}
        </Card>
      </Card>
    </div>
  );
}
