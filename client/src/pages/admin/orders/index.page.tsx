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
import { useEffect, useState } from "react";
import { BiPaperPlane } from "react-icons/bi";
import BackPage from "../../../components/BackPage";

export function Page() {
  const [orders, setOrders] = useState<
    {
      id: number;
      city: string;
      direction: string;
      state: string;
      phone: number;
      since: number;
      till: number;
      zip: number;
    }[]
  >([]);

  useEffect(() => {
    axios.get(`/api/user/order/`).then((response) => {
      console.log(response.data);
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
                  <TableCell>Id</TableCell>
                  <TableCell>Address</TableCell>
                  <TableCell>Since</TableCell>
                  <TableCell>Till</TableCell>
                  <TableCell>Details</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {orders.map((o, i) => (
                  <TableRow key={i}>
                    <TableCell>{o.id}</TableCell>
                    <TableCell>{`${o.direction}, ${o.city}, ${o.state}, ${o.zip}`}</TableCell>
                    <TableCell>
                      {new Date(o.since)
                        .toISOString()
                        .slice(0, 19)
                        .replace("T", " ")}
                    </TableCell>
                    <TableCell>
                      {new Date(o.till)
                        .toISOString()
                        .slice(0, 19)
                        .replace("T", " ")}
                    </TableCell>
                    <TableCell>
                      <Button
                        variant="text"
                        component="a"
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
