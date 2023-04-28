import React, { useEffect, useState } from "react";
import axios from "axios";
import Grid2 from "@mui/material/Unstable_Grid2";
import {
  Button,
  Card,
  Table,
  Typography,
  CardMedia,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  ListItemButton,
} from "@mui/material";

export function Page() {
  const [orders, setOrders] = useState([{}]);

  useEffect(() => {
    axios.get(`/api/user/order/user`).then((response) => {
      setOrders(response.data);
      console.log(response.data);
    });
  }, []);
  return (
    <div className="justify-center text-center">
      <Card className="p-4" elevation={10} sx={{ width: "100%" }}>
        <div className="flex justify-center text-center">
          <CardMedia
            className="text-center"
            component="img"
            image="/redwhite.png"
            sx={{ maxWidth: 190 }}
          />
        </div>
        <Typography variant="h4" mt={2}>
          Orders
        </Typography>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Order number</TableCell>
              <TableCell>Address</TableCell>
              <TableCell>State</TableCell>
              <TableCell>City</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {orders.map((item, index) => (
              <TableRow
                component="a"
                href={`/user/orders/${item.id}`}
                key={index}
              >
                <TableCell>{item.id}</TableCell>
                <TableCell>{item.direction}</TableCell>
                <TableCell>{item.state}</TableCell>
                <TableCell>{item.city}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Card>
    </div>
  );
}
