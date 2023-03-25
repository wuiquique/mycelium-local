"use client";

import {
  Button,
  Card,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import Link from "next/link";
import { useEffect, useState } from "react";
import { BiPaperPlane } from "react-icons/bi";
import BackPage from "../../../components/BackPage";

export default function Orders() {
  const [orders, setOrders] = useState<
    {
      id: number;
      first_name: string;
      last_name: string;
      product_count: number;
      since: number;
      till: number;
    }[]
  >([]);

  useEffect(() => {
    axios.get(`/api/admin/orders/`).then((response) => {
      setOrders(response.data);
    });
  }, []);

  return (
    <div>
      <BackPage />
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Typography variant="h3" className="text-center">
            All Orders
          </Typography>
          <br />
          <Card elevation={10} sx={{ minHeight: 300 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Name</TableCell>
                  <TableCell>Product_Count</TableCell>
                  <TableCell>Since</TableCell>
                  <TableCell>Till</TableCell>
                  <TableCell>Details</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {orders.map((o, i) => (
                  <TableRow key={i}>
                    <TableCell>{`${o.first_name} ${o.last_name}`}</TableCell>
                    <TableCell>{o.product_count}</TableCell>
                    <TableCell>{o.since}</TableCell>
                    <TableCell>{o.till}</TableCell>
                    <TableCell>
                      <Button
                        variant="text"
                        component={Link}
                        href={`/admin/orders/${o.id}`}
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
