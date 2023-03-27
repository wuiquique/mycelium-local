import {
  Button,
  Card,
  CardContent,
  CardMedia,
  Chip,
  Rating,
  Typography,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import { Stack } from "@mui/system";
import axios from "axios";
import { useEffect, useState } from "react";
import BackPage from "../../components/BackPage";

export function Page() {
  useEffect(() => {
    axios.get("/api/categories/").then((response) => {
      setAllCategories(response.data);
    });
  }, []);

  const [allCategories, setAllCategories] = useState([]);
  const [currentCat, setCurrentCat] = useState({
    name: "Select a Category",
    id: 0,
  });
  const [products, setProducts] = useState([
    /*{
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
    },*/
  ]);

  const handleChangeCat = (cat) => {
    setCurrentCat(cat);
    axios.get(`/api/product/byCategory/${cat.id}`).then((response) => {
      setProducts(response.data);
    });
  };

  return (
    <div>
      <BackPage />
      <Typography variant="h3" className="text-center">
        Categories
      </Typography>
      <br />
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Typography variant="body1" className="mb-2">
            ***Click to display all category products:
          </Typography>
          <Stack direction="row" spacing={1}>
            {allCategories.map((e, i) => (
              <Chip
                key={i}
                label={e.name}
                onClick={() => handleChangeCat(e)}
              ></Chip>
            ))}
          </Stack>
        </Grid2>
        <Grid2 lg={12}>
          <Typography variant="h5" className="text-center">
            {currentCat.name}
          </Typography>
          {products.map((e, i) => (
            <Grid2 lg={6} key={i}>
              <Button component="a" href={`/product/${e.id}`}>
                <Card elevation={10} sx={{ display: "flex" }}>
                  <CardMedia
                    sx={{ width: "50%" }}
                    component="img"
                    image={e.pictures[0]}
                    alt="Imagen de Producto"
                  />
                  <CardContent className="text-left">
                    <Typography variant="h5">{e.name}</Typography>
                    <Typography variant="h6">{e.description}</Typography>
                    <Typography variant="body1">
                      <strong>{e.brand}</strong>
                    </Typography>
                    <br />
                    <Typography variant="h4">$.{e.price}.00</Typography>
                    <br />
                    <Rating readOnly size="large" />
                  </CardContent>
                </Card>
              </Button>
            </Grid2>
          ))}
        </Grid2>
      </Grid2>
    </div>
  );
}
