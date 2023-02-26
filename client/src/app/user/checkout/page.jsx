"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Typography,
  Button,
  Card,
  CardMedia,
  CardContent,
  Box
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import CreditCard from "../../../components/CreditCard";
import UserAddress from "../../../components/UserAddress";
import UserCart from "../../../components/UserCart";

export default function Checkout() {

    const [products, setProducts] = useState([
        {
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
    const [address, setAddress] = useState({})

    useEffect(() => {
        axios.get('/api/user/cart')
        .then(response => {
            setProducts(response.data)
        })
        .catch(error => {
            console.log(error)
        })
    }, [])


  return (
    <div>
        <Typography variant='h2' className='text-center'>Checkout</Typography>
        <br/>
        <Grid2 container spacing={2}>
            <Grid2 lg={6}>
                <CreditCard />
            </Grid2>
            <Grid2 lg={6}>
                <UserAddress setAddress={setAddress}/>
            </Grid2>
            <Grid2 lg={12}>
                <UserCart products={products} cartOrCheckout='checkout' onChange={setProducts}/>
            </Grid2>
        </Grid2>
    </div>
  )
}
