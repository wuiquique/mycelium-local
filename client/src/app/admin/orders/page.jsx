"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Card,
  Typography,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Button
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import { BiPaperPlane } from 'react-icons/bi'
import Link from 'next/link';

export default function Orders() {

    const [orders, setOrders] = useState([
      {
        id: 1,
        first_name: 'Diego', 
        last_name: 'Vallejo', 
        product_count: 18,
        since: '2023-02-23',
        till: '2023-02-25'
      }
    ])

    axios.get(`admin/orders/`)
    .then(response => {
      setOrders(response.data)
    })
    .catch(error => {
      console.log(error)
    })

    
  return (
    <div>
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Typography variant='h3' className='text-center'>All Orders</Typography>
          <br/>
          <Card elevation={10} sx={{ minHeight: 300 }}>
            <Table>
              <TableHead>
                <TableCell>Name</TableCell>
                <TableCell>Product_Count</TableCell>
                <TableCell>Since</TableCell>
                <TableCell>Till</TableCell>
                <TableCell>Details</TableCell>
              </TableHead>
              <TableBody>
                {
                  orders.map((e, i) => (
                    <TableRow key={i}>
                      <TableCell>{`${e.first_name} ${e.last_name}`}</TableCell>
                      <TableCell>{e.product_count}</TableCell>
                      <TableCell>{e.since}</TableCell>
                      <TableCell>{e.till}</TableCell>
                      <TableCell>
                        <Button
                          variant='text'
                          component={Link}
                          href={`/admin/orders/${e.id}`}
                        >
                          <BiPaperPlane size='1.6rem'/>
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))
                }
              </TableBody>
            </Table>
          </Card>
        </Grid2>
      </Grid2>
    </div>
  )
}
