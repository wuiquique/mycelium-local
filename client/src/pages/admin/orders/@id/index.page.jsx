import { Card, Typography } from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import { useEffect, useState } from "react";
import BackPage from "../../../../components/BackPage";
import AdminOrderDet from "../../../../components/AdminOrderDet";

export function Page({ params: { id } }) {
  const [products, setProducts] = useState([]);
  const [orderDetails, setOrderDetails] = useState({
    since: 1,
    till: 1,
  });

  useEffect(() => {
    axios
      .get(`/api/user/order/${id}`)
      .then((response) => {
        console.log(response.data);
        setProducts(response.data.productList);
        setOrderDetails(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  const getTotal = () => {
    let total = 0;
    for (let i of products) {
      total += i.productPrice * i.quantity;
    }
    return total;
  };

  return (
    <div>
      <BackPage />
      <Typography variant="h3" className="text-center">
        Order Details
      </Typography>
      <br />
      <Grid2 container spacing={2}>
        <Grid2 lg={8}>
          <AdminOrderDet products={products} onChange={setProducts} />
        </Grid2>
        <Grid2 lg={4}>
          <Card elevation={10}>
            <Typography variant="h4" className="text-center mt-4">
              Order Summary
            </Typography>
            <div className="text-left p-4">
              <Typography variant="h5">{`Total: Q.${getTotal()}.00`}</Typography>
              <Typography variant="h5">{`Unique Items: ${products.length}`}</Typography>
            </div>
            <br />
            <Typography variant="h5" className="text-center">
              <strong>Delivery Details</strong>
            </Typography>
            <div className="text-left p-4">
              <Typography variant="h6">
                <strong>Address:</strong>&nbsp;{orderDetails.direction}
              </Typography>
              <Typography variant="h6">
                <strong>State:</strong>&nbsp;{orderDetails.state}
              </Typography>
              <Typography variant="h6">
                <strong>City:</strong>&nbsp;{orderDetails.city}
              </Typography>
              <Typography variant="h6">
                <strong>Zip Code:</strong>&nbsp;{orderDetails.zip}
              </Typography>
              <Typography variant="h6">
                <strong>Phone:</strong>&nbsp;{orderDetails.phone}
              </Typography>

              <Typography variant="h6">
                <strong>Available From:</strong>&nbsp;
                {new Date(orderDetails.since * 1000)
                  .toISOString()
                  .slice(0, 19)
                  .replace("T", " ")}
              </Typography>
              <Typography variant="h6">
                <strong>Until:</strong>&nbsp;
                {new Date(orderDetails.till * 1000)
                  .toISOString()
                  .slice(0, 19)
                  .replace("T", " ")}
              </Typography>
            </div>
          </Card>
        </Grid2>
      </Grid2>
    </div>
  );
}
