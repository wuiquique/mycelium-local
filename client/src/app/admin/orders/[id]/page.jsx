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
import UserCart from "../../../../components/UserCart";

export default function OrderDetails() {

  let id = 1

  const [products, setProducts] = useState([
    {
      status: 'In Transit',
      comment: 'SE ATRAZÓ SALU3',
      type: 'local',
      id: '1', 
      name: 'Pez', 
      description: 'Pero mira como bebe por ver a dios nacido, beben y beben y vuelven a beber',
      quantity: '3', 
      category: 'God',  //-> nombre
      brand: 'Jesus', 
      weight: '23', 
      price: 75, 
      pictures: ['https://pbs.twimg.com/profile_images/949787136030539782/LnRrYf6e_400x400.jpg'] //-> solo 1
  },
  {
    status: 'In Transit', 
    comment: "",
    type: 'local',
    id: '1', 
    name: 'Pez', 
    description: 'Pero mira como bebe por ver a dios nacido, beben y beben y vuelven a beber',
    quantity: '3', 
    category: 'God',  //-> nombre
    brand: 'Jesus', 
    weight: '23', 
    price: 75, 
    pictures: ['https://pbs.twimg.com/profile_images/949787136030539782/LnRrYf6e_400x400.jpg'] //-> solo 1
  }
  ])
  const [orderDetails, setOrderDetails] = useState({
    id: 1,
    direction: 'Guatemala zona 10', 
    state: 'Guatemala',
    city: 'Guatemala',
    zip: '01062',
    phone: '09091212', 
    since: '2023-02-23',
    till: '2023-02-25'
  })

  useEffect(() => {
    axios.get(`/api/admin/orders/${id}`)
    .then(response => {
      setProducts(response.data.products)
      setOrderDetails(response.data)
    })
    .catch(error => {
      console.log(error)
    })
  }, [])

  const getTotal = () => {
    let total = 0
    for (let i of products) {
        total += i.price
    }
    return total
  } 

  return (
    <div>
      <Typography variant='h3' className='text-center'>Order Details</Typography>
      <br/>
      <Grid2 container spacing={2}>
        <Grid2 lg={8}>
          <UserCart products={products} cartOrCheckout='details' onChange={setProducts} orderId={orderDetails.id}/>
        </Grid2>
        <Grid2 lg={4}>
          <Card elevation={10}>
            <Typography variant='h4' className="text-center mt-4">Order Summary</Typography>
            <div className="text-left p-4">
              <Typography variant="h5">{`Total: Q.${getTotal()}.00`}</Typography>
              <Typography variant="h5">{`Unique Items: ${products.length}`}</Typography>
            </div>
            <br/>
            <Typography variant="h5" className='text-center'><strong>Delivery Details</strong></Typography>
            <div className="text-left p-4">
              <Typography variant='h6'><strong>Address:</strong>&nbsp;{orderDetails.direction}</Typography>
              <Typography variant='h6'><strong>State:</strong>&nbsp;{orderDetails.state}</Typography>
              <Typography variant='h6'><strong>City:</strong>&nbsp;{orderDetails.city}</Typography>
              <Typography variant='h6'><strong>Zip Code:</strong>&nbsp;{orderDetails.zip}</Typography>
              <Typography variant='h6'><strong>Phone:</strong>&nbsp;{orderDetails.phone}</Typography>
              <Typography variant='h6'><strong>Available From:</strong>&nbsp;{orderDetails.since}</Typography>
              <Typography variant='h6'><strong>Until:</strong>&nbsp;{orderDetails.till}</Typography>
            </div>
          </Card>
        </Grid2>
      </Grid2>
    </div>
  )
}
