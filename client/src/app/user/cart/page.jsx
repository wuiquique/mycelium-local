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
import Link from 'next/link';
import UserCart from "../../../components/UserCart";

export default function Cart() {

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

/*    useEffect(() => {
        axios.get('/api/user/cart')
        .then(response => {
            setProducts(response.data)
        })
        .catch(error => {
            
        })
    }, [setProducts])
*/
    const getTotal = () => {
        let total = 0
        for (let i of products) {
            total += i.price
        }
        return total
    }

  return (
    <div>
        <Typography variant="h3" className='text-center'>mycelium cart :)</Typography>
        <br/>
        <Grid2 container spacing={2}>
            <Grid2 lg={8}>
                <UserCart products={products} cartOrCheckout='cart' onChange={setProducts}/>
            </Grid2>
            <Grid2 lg={4}>
                <Card elevation={10}>
                    <CardContent className='text-center'>
                        <Typography variant='h4'>{`Total: Q.${getTotal()}.00`}</Typography>
                        <Button
                            className="mt-6"
                            variant="outlined"
                            color="secondary"
                            size='large'
                            component={Link}
                            href='/user/checkout'
                        >
                            Checkout
                        </Button>
                    </CardContent>
                    <CardMedia
                        component="img"
                        width="100%"
                        image="/redwhite.png"
                        alt="Mycelium Logo"
                        className="p-2"
                    />
                </Card>
            </Grid2>
        </Grid2>
    </div>
  )
}
