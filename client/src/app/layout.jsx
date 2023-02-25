'use client';
    
import React from 'react'
import { Container } from '@mui/material'

export default function layout({children}) {
  return (

    <html lang="en">
        {/*
        <head /> will contain the components returned by the nearest parent
        head.tsx. Find out more at https://beta.nextjs.org/docs/api-reference/file-conventions/head
        */}
        <head />
        <body>
            <div>
                <br/>
                <br/>
                <br/>
                <Container maxWidth="lg">
                    <div className='text-center'>
                        {children}
                    </div>
                </Container>
            </div>
        </body>
    </html>

  )
}
