import { useTexts } from "@/hooks/textContext";
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
  const texts = useTexts();

  useEffect(() => {
    axios.get("/api/categories/").then((response) => {
      setAllCategories(response.data);
    });
  }, []);

  const [allCategories, setAllCategories] = useState<
    {
      name: string;
      id: number;
    }[]
  >([]);
  const [currentCat, setCurrentCat] = useState<{
    name: string;
    id: number;
  } | null>(null);
  const [products, setProducts] = useState<
    {
      id: number;
      name: string;
      desc: string;
      brand: string;
      weight: number;
      quantity: number;
      price: number;
      pictures: string[];
      technical: { type: string; value: string }[];
    }[]
  >([]);

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
        {texts.categorypage.title}
      </Typography>
      <br />
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Typography variant="body1" className="mb-2">
            {texts.categorypage.instruction}
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
            {currentCat === null
              ? texts.categorypage.notselected
              : currentCat.name}
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
                    <Typography variant="h6">{e.desc}</Typography>
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
