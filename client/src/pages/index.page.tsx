import { useTexts } from "@/hooks/textContext";
import LocalShippingOutlinedIcon from "@mui/icons-material/LocalShippingOutlined";
import PaymentIcon from "@mui/icons-material/Payment";
import TaskAltIcon from "@mui/icons-material/TaskAlt";
import { Card, CardMedia, Paper, Typography } from "@mui/material";
import Box from "@mui/material/Box";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import Stack from "@mui/material/Stack";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import { useEffect, useState } from "react";
import Carousel from "react-material-ui-carousel";

export function Page() {
  const texts = useTexts();

  useEffect(() => {
    axios.get(`/api/product/topSales`).then((response) => {
      setProds(response.data);
    });
    axios.get(`/api/product/lastBought`).then((response) => {
      setNewP(response.data);
    });
  }, []);

  const [prods, setProds] = useState<any[]>([]);

  const [newp, setNewP] = useState<any[]>([]);

  return (
    <div className="justify-center text-center">
      <Card className="p-4" elevation={10} sx={{ width: "100%" }}>
        <div className="flex justify-center text-center">
          <CardMedia
            className="text-center"
            component="img"
            height="100%"
            image="/redwhite.png"
            alt={texts.global.shopname}
            sx={{ maxWidth: 345 }}
          />
        </div>
        <Typography variant="h4" mt={2}>
          {texts.global.shopname}
        </Typography>
        <Typography variant="h6" m={1}>
          {texts.homepage.title}
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
                      <b>{texts.homepage.promo1title}</b>
                    </Typography>
                    <Typography variant="body2">
                      {texts.homepage.promo1text}
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
                      <b>{texts.homepage.promo2title}</b>
                    </Typography>
                    <Typography variant="body2">
                      {texts.homepage.promo2text}
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
                      <b>{texts.homepage.promo3title}</b>
                    </Typography>
                    <Typography variant="body2">
                      {texts.homepage.promo3text}
                    </Typography>
                  </div>
                </Grid2>
              </Grid2>
            </Grid2>
          </Grid2>
        </Card>
        <Typography variant="h5" mt={6} className="text-left">
          {texts.homepage.bestsellers}
        </Typography>
        <Carousel sx={{ width: 1 }}>
          {prods.map((e, i) => (
            <div key={i}>
              <Paper className="h-80">
                <ListItemButton component="a" href={`/product/${e.id}`}>
                  <Grid2 container spacing={12}>
                    <Grid2 lg={4}>
                      <CardMedia
                        component="img"
                        height="100%"
                        image={e.pictures[0]}
                      />
                    </Grid2>
                    <Grid2 lg={8}>
                      <div className="text-left">
                        <Typography variant="h5" mt={2}>
                          {e.name}
                        </Typography>
                        <Typography variant="body1" mt={2}>
                          <b>{texts.product.description}:</b> {e.desc} <br />
                          <b>{texts.product.brand}:</b> {e.brand}
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
                          <b>{texts.product.quantity}: </b> {e.quantity}
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
        <br />

        <Box className="overflow-x-auto">
          <List component={Stack} direction="row" height={400} width={1600}>
            {newp.map((e, i) => (
              <ListItem disablePadding key={i} sx={{ maxWidth: "500px" }}>
                <ListItemButton component="a" href={`/product/${e.id}`}>
                  <Card elevation={10} className="p-4" sx={{ width: 1 }}>
                    <CardMedia image={e.pictures[0]} sx={{ height: 240 }} />
                    <Typography variant="h6">{e.name}</Typography>
                    <Typography variant="body1">Q. {e.price}.00</Typography>
                    <Typography variant="subtitle2">
                      {texts.product.quantity}: {e.quantity}
                    </Typography>
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
