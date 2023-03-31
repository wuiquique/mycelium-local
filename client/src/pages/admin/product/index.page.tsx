import { useTexts } from "@/hooks/textContext";
import {
  Button,
  Card,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import { useEffect, useState } from "react";
import { BiPaperPlane } from "react-icons/bi";

export function Page() {
  const texts = useTexts();

  const [products, setProducts] = useState([]);

  useEffect(() => {
    axios.get("/api/product").then((response) => {
      setProducts(response.data);
      console.log(response.data);
    });
  }, []);

  const changeInput = (index, camp, value) => {
    let temp = [...products];
    temp[index][camp] = value;
    setProducts(temp);
  };

  const blurSave = (id, index) => {
    let post = {
      name: products[index].name,
      desc: products[index].desc,
      categorieId: products[index].categorieId,
      brand: products[index].brand,
      weight: products[index].weight,
      quantity: products[index].quantity,
      price: products[index].price,
    };
    axios.put(`/api/product/${id}`, post).then((response) => {
      setProducts(response.data);
      console.log(response.data);
    });
  };

  return (
    <div>
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Typography variant="h3" className="text-center">
            {texts.adminproductpage.title}
          </Typography>
          <br />
          <Card elevation={10} sx={{ minHeight: 300 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>{texts.product.name}</TableCell>
                  <TableCell>{texts.product.description}</TableCell>
                  <TableCell>{texts.product.quantity}</TableCell>
                  <TableCell>{texts.product.price}</TableCell>
                  <TableCell>{texts.adminproductpage.detailscol}</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {products.map((e, i) => (
                  <TableRow key={e.id}>
                    <TableCell>
                      <TextField
                        defaultValue={e.name}
                        variant="standard"
                        onChange={(ev) =>
                          changeInput(i, "name", ev.target.value)
                        }
                        onBlur={() => blurSave(e.id, i)}
                      />
                    </TableCell>
                    <TableCell>
                      <TextField
                        defaultValue={e.desc}
                        variant="standard"
                        onChange={(ev) =>
                          changeInput(i, "desc", ev.target.value)
                        }
                        onBlur={() => blurSave(e.id, i)}
                      />
                    </TableCell>
                    <TableCell>
                      <TextField
                        defaultValue={e.quantity}
                        variant="standard"
                        onChange={(ev) =>
                          changeInput(i, "quantity", ev.target.value)
                        }
                        onBlur={() => blurSave(e.id, i)}
                      />
                    </TableCell>
                    <TableCell>
                      <TextField
                        defaultValue={e.price}
                        variant="standard"
                        onChange={(ev) =>
                          changeInput(i, "price", ev.target.value)
                        }
                        onBlur={() => blurSave(e.id, i)}
                      />
                    </TableCell>
                    <TableCell>
                      <Button
                        variant="text"
                        component="a"
                        href={`/admin/product/${e.id}`}
                      >
                        <BiPaperPlane size="1.6rem" />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </Card>
        </Grid2>
      </Grid2>
    </div>
  );
}
