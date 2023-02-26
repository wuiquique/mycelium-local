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
            pictures: ['data:image/jpeg;base64,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'] //-> solo 1
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
