import { useTexts } from "@/hooks/textContext";
import {
  Box,
  Button,
  Card,
  CardContent,
  CardMedia,
  Chip,
  FormControl,
  InputLabel,
  MenuItem,
  OutlinedInput,
  Rating,
  Select,
  SelectChangeEvent,
  Slider,
  Typography,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import { useEffect, useState } from "react";
import BackPage from "../../components/BackPage";

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
  PaperProps: {
    style: {
      maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
      width: 250,
    },
  },
};

function valuetext(value: number) {
  return `${value}.00`;
}

export function Page({ searchParams: { q } }: { searchParams: { q: string } }) {
  const texts = useTexts();

  const [categories, setCategories] = useState<
    {
      id: number;
      name: string;
    }[]
  >([]);

  const [results, setResults] = useState<
    {
      id: number;
      name: string;
      desc: string;
      categorieId: number;
      brand: string;
      weight: number;
      quantity: number;
      price: number;
      pictures: string[];
      technical: {
        type: string;
        value: string;
      }[];
    }[]
  >([]);

  const [priceRange, setPriceRange] = useState([0, 1000]);
  const [selectedPriceRange, setSelectedPriceRange] = useState([0, 1000]);
  const [selectedCategories, setSelectedCategories] = useState<number[]>([]);

  const handleSliderChange = (event: unknown, newValue: number | number[]) => {
    if (!Array.isArray(newValue)) return;
    setPriceRange(newValue);
  };

  const handleSliderCommit = (event: unknown, newValue: number | number[]) => {
    if (!Array.isArray(newValue)) return;
    setSelectedPriceRange(newValue);
  };

  const handleChange = (
    event: SelectChangeEvent<typeof selectedCategories>
  ) => {
    const {
      target: { value },
    } = event;
    setSelectedCategories(
      // On autofill we get a stringified value.
      typeof value === "string"
        ? value.split(",").map((s) => parseInt(s, 10))
        : value
    );
  };

  useEffect(() => {
    axios
      .get("/api/categories/")
      .then((response) => {
        setCategories(response.data);
        console.log(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  useEffect(() => {
    const url = new URL("/api/product/search", window.location.href);
    url.searchParams.set("q", q);
    url.searchParams.set("pricemin", selectedPriceRange[0].toString());
    url.searchParams.set("pricemax", selectedPriceRange[1].toString());
    url.searchParams.set("categories", selectedCategories.join(","));
    axios.get(url.toString()).then((response) => {
      setResults(response.data);
    });
  }, [q, selectedPriceRange, selectedCategories]);

  return (
    <div>
      <BackPage />
      <Typography variant="h3" className="text-center">
        {texts.searchpage.title}
      </Typography>
      <br />
      <Grid2 container spacing={2}>
        <Grid2 lg={6}>
          <Typography variant="h6" className="text-center">
            {texts.searchpage.categoryfilter}
          </Typography>
          <FormControl sx={{ width: "100%" }}>
            <InputLabel id="demo-multiple-chip-label">Categories</InputLabel>
            <Select
              labelId="demo-multiple-chip-label"
              id="demo-multiple-chip"
              multiple
              value={selectedCategories}
              onChange={handleChange}
              input={
                <OutlinedInput id="select-multiple-chip" label="Categories" />
              }
              renderValue={(selected) => (
                <Box sx={{ display: "flex", flexWrap: "wrap", gap: 0.5 }}>
                  {selected.map((value) => (
                    <Chip
                      key={value}
                      label={categories.find((c) => c.id === value)?.name ?? ""}
                    />
                  ))}
                </Box>
              )}
              MenuProps={MenuProps}
            >
              {categories.map((e, i) => (
                <MenuItem key={i} value={e.id}>
                  {e.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid2>
        <Grid2 lg={6}>
          <Typography variant="h6" className="text-center">
            {texts.searchpage.pricefilter}
          </Typography>
          <Slider
            className="mt-2"
            value={priceRange}
            onChange={handleSliderChange}
            valueLabelDisplay="auto"
            getAriaValueText={valuetext}
            max={1000}
            onChangeCommitted={handleSliderCommit}
          />
        </Grid2>
        {results.map((e, i) => (
          <Grid2 lg={6} key={i}>
            <Button component="a" href={`/product/${e.id}`}>
              <Card elevation={10} sx={{ display: "flex" }}>
                <CardMedia
                  sx={{ width: "50%" }}
                  component="img"
                  image={
                    "https://falabella.scene7.com/is/image/FalabellaPE/770197465_1?wid=800&hei=800&qlt=70"
                  }
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
    </div>
  );
}
