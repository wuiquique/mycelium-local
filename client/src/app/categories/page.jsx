"use client";

import React, { useEffect, useState, useTheme } from "react";
import axios from "axios";
import {
  Card,
  CardMedia,
  CardContent,
  Typography,
  Button,
  TextField,
  FormControl,
  InputLabel,
  Select, 
  OutlinedInput,
  MenuItem,
  Box,
  Chip,
  Slider,
  Rating
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import { AiOutlineSearch } from 'react-icons/ai'
import { Stack } from "@mui/system";

export default function Categories() {
/*
  useEffect(() => {
    axios.get('/api/categories/')
    .then(response => {
      setAllCategories(response.data)
    })
  }, [])
*/

  const [allCategories, setAllCategories] = useState([
    {
      'id': 1, 
      'name': 'DIOROOO' 
    },
    {
      'id': 2, 
      'name': 'WOOOOW', 
    },
    {
      'id': 3, 
      'name': 'SOPAS'
    }
  ])
  const [currentCat, setCurrentCat] = useState({name: 'Select a Category', id: 0})
  const [products, setProducts] = useState([
    {
      type: 'local',
      id: '1', 
      name: 'Cuchara de Oro', 
      description: 'Muy Fina',
      quantity: '3', 
      category: 'Oro',  //-> nombre
      brand: 'Dioro', 
      weight: '23', 
      price: 75, 
      pictures: ['https://falabella.scene7.com/is/image/FalabellaPE/770197465_1?wid=800&hei=800&qlt=70'] //-> solo 1
    }
  ])

  const handleChangeCat = (cat) => {
    setCurrentCat(cat)
    axios.post(`/api/categories/${cat.id}`)
    .then(response => {
      setProducts(response.data)
    })
  }

  return (
    <div>
        <Typography variant='h3' className="text-center">Categories</Typography>
        <br/>
        <Grid2 container spacing={2}>
            <Grid2 lg={12}>
              <Typography variant='body1' className="mb-2">***Click to display all category products:</Typography>
              <Stack direction='row' spacing={1}>
                {
                  allCategories.map((e, i) => (
                    <Chip key={i} label={e.name} onClick={() => handleChangeCat(e)}></Chip>
                  ))
                }
              </Stack>
            </Grid2>
            <Grid2 lg={12}>
              <Typography variant='h5' className="text-center">{currentCat.name}</Typography>
              {
                products.map((e, i) => (
                    <Grid2 lg={6} key={i}>
                        <Button>
                            <Card elevation={10} sx={{ display: 'flex' }}>
                                <CardMedia
                                    sx={{ width: '50%' }}
                                    component='img'
                                    image={e.pictures[0]}
                                    alt="Imagen de Producto"
                                />
                                <CardContent className="text-left">
                                    <Typography variant='h5'>{e.name}</Typography>
                                    <Typography variant='h6'>{e.description}</Typography>
                                    <Typography variant='body1'><strong>{e.brand}</strong></Typography>
                                    <br/>
                                    <Typography variant='h4'>$.{e.price}.00</Typography>
                                    <br/>
                                    <Rating
                                        readOnly
                                        size='large'
                                    />
                                </CardContent>
                            </Card>
                        </Button>
                    </Grid2>
                ))
            }
            </Grid2>
        </Grid2>
    </div> 
  )
}
