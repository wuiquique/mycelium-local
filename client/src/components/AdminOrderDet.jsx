import {
  Button,
  Card,
  CardContent,
  CardMedia,
  TextField,
  Typography,
} from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";

export default function AdminOrderDet({ products, onChange }) {
  const [prods, setProds] = useState(products);

  useEffect(() => {
    setProds(products);
  }, [products]);

  const handleCommentChange = (i, value) => {
    let temp = [...prods];
    temp[i]["comment"] = value;
    setProds(temp);
  };

  const handleBlur = (status) => 
    axios
      .post(`/api/user/order/${orderId}/message/${status}`)
      .then((response) => {
        onChange(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <div>
      {prods.map((e, i) => (
        <Card
          elevation={10}
          sx={{ display: "flex", marginBottom: "10px" }}
          key={i}
        >
          <CardMedia
            sx={{ width: 250 }}
            component="img"
            image={e.pictures[0]}
            alt="Imagen de Producto"
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
            </div>
          </CardContent>
          <div className="text-right p-5">
            <Typography variant="h4">{`Q.${e.productPrice}.00`}</Typography>
            <Typography variant="subtitle1">
              <strong>{`Qty. ${e.quantity}`}</strong>
            </Typography>
            <div className="mt-8">
              <Button
                sx={{ minWidth: "100% " }}
                variant="outlined"
                color="secondary"
              >
                <Typography>
                  <strong>{e.statusId.name}</strong>
                </Typography>
              </Button>
              <TextField
                className="mt-2"
                label="Status Comment"
                value={e.comment}
                variant="standard"
                size="small"
                multiline
                rows={2}
                onChange={(ev) => handleCommentChange(i, ev.target.value)}
                onBlur={() => handleBlur()}
              />
            </div>
          </div>
        </Card>
      ))}
    </div>
  );
}
