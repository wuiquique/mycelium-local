"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Card,
  CardMedia,
  CardContent,
  Box,
  Typography,
  Button,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";

export default function UserCart({products, cartOrCheckout, onChange}) {

    const handleDeleteItem = (productId) => {
        axios.delete(`/api/user/cart/${productId}`)
        .then(response => {
            onChange(response.data)
        })
    }

  return (
    <div>
        {
            products.length === 0 ?

            <Card elevation={10} sx={{ display: 'flex' }}>
                <Typography variant='h4'>No Cuentas con Objetos en tu Carrito :)</Typography>
            </Card>
            :
            products.map((e, i) => (
                <Card elevation={10} sx={{ display: 'flex' }} key={i}>
                    <CardMedia
                        sx={{ width: 250 }}
                        component='img'
                        image={e.pictures[0]}
                        alt="Imagen de Producto"
                    />
                    <CardContent >
                        <div className='flex justify-start' >
                            <Typography 
                                variant='h4' 
                                className='mr-5'
                            >
                                {e.name}
                                </Typography>
                        </div>
                        <div className="text-left">
                            <Typography variant='h5'>{`Qty. ${e.quantity}`}</Typography>
                            <Typography variant='h6'>{e.description}</Typography>
                            <Typography>{`Brand: ${e.brand}`}</Typography>
                            <Typography>{`Weight: ${e.weight} Lb`}</Typography>
                        </div>
                    </CardContent>
                    <div className="text-right p-5">
                        {
                            cartOrCheckout === 'cart' ?
                                <Button
                                    variant='outlined'
                                    onClick={() => handleDeleteItem(e.id)}
                                >
                                    Eliminar
                                </Button>
                            :
                                <></>
                        }
                        <Typography variant='h4'>{`Q.${e.price}.00`}</Typography>
                    </div>
                    <br/>
                </Card>
            ))
        }
    </div>
  )
}
