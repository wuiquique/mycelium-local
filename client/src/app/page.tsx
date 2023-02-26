"use client";

import Image from 'next/image'
import { Inter } from '@next/font/google'
import styles from './page.module.css'
import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Card,
  CardContent,
  CardMedia,
  Button,
  TextField,
  Typography,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import CardHeader from "@mui/material/CardHeader";
import Avatar from "@mui/material/Avatar";
import { red } from "@mui/material/colors";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";


const inter = Inter({ subsets: ['latin'] })

export default function Home() {
  return (
    <div className="flex justify-center text-center">
      <Grid2 container spacing={2}>
        <div>
          <Card className="p-4" elevation={10} sx={{width: "100%"}}>
          <CardMedia
                component="img"
                height="100%"
                image="/redwhite.png"
                alt="Mycelium Logo"
                sx={{ maxWidth: 345 }}
              />
          </Card>
        </div>
      </Grid2>
    </div>
  )
}
