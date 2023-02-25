'use client';

import React, { useEffect, useState } from 'react'
import axios from 'axios'
import { Card, CardContent, Container } from '@mui/material'
import Grid2 from '@mui/material/Unstable_Grid2'

export default function login() {

  return (
    <div className='text-center'>
      <Grid2 container spacing={2}>
        <Grid2 lg={8} >
          <div className='border-2 border-red-900'>
            <p>md=8</p>
          </div>
        </Grid2>
        <Grid2 lg={4} >
          <div className='border-2 border-red-900'>
            <p>md=4</p>
          </div>
        </Grid2>
        <Grid2 lg={4} >
          <div className='border-2 border-red-900'>
            <p>md=4</p>
          </div>
        </Grid2>
        <Grid2 lg={8} >
          <div className='border-2 border-red-900'>
            <p>md=8</p>
          </div>
        </Grid2>
      </Grid2>
    </div>
  )
}

