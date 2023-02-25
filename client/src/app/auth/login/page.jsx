'use client';

import React, { useEffect, useState } from 'react'
import axios from 'axios'
import { Card, CardContent, CardMedia, Container, TextField, Typography } from '@mui/material'
import Grid2 from '@mui/material/Unstable_Grid2'

export default function login() {
  //import redwhite from '../../../assets/icons/redwhite.png;'

  //'../../../public/redwhite.png;'

  return (

    <div className='text-center'>
      <Grid2 container spacing={2}>
        <Grid2 lg={4}>
        </Grid2>
        <Grid2 lg={4} >
          <Card sx={{ maxWidth: 345 }}>
            <CardMedia
              component='img'
              height="100%"
              image='/redwhite.png'
              alt="Mycelium Logo"
            />
            <Typography variant='h4' mt={2}>
              Login
            </Typography>
            <form className='mt-8 mb-11'>
              <TextField label="Email" variant="standard" />
              <TextField label="Password" variant="standard" />
            </form>
          </Card>
        </Grid2>
        <Grid2 lg={4} >
        </Grid2>
      </Grid2>
    </div>
  )
}

