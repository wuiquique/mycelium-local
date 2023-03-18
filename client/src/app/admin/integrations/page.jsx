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
  Rating,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import { AiOutlineSearch } from "react-icons/ai";
import BackPage from "../../../components/BackPage";

export default function Integrations() {

    const [integ, setInteg] = useState([
        {
            name: 'Amazon', 
            username: 'neim', 
            pass: 'paseworl',
            ref: 'http://lol.com', 
        }
    ])

    useEffect(() => {
        axios.get('ruta')
        .then((response) => {
            setInteg(response.data)
        })
    }, [])

    return (
        <div>
            <BackPage/>
            <Typography variant='h3' className="text-center">
                Integrations
            </Typography>
            <br/>
            <Grid2 container spacing={2}>
                <Grid2 lg={12}>
                    <Card elevation={10}>
                        <CardContent>
                            <div className="flex justify-between">
                                <TextField 
                                    sx={{ minWidth: '33%'}}
                                    variant="standard"
                                    label="Nombre"
                                />
                                <TextField
                                    sx={{ minWidth: '33%'}}
                                    variant="standard"
                                    label="Usuario"
                                />
                                <TextField
                                    sx={{ minWidth: '33%'}}
                                    variant="standard"
                                    label="Contraseña"
                                />
                            </div>
                            <br/>
                            <TextField
                                sx={{ minWidth: '100%' }}
                                variant='standard'
                                label='Ruta'
                            />
                            <div className="flex justify-center">
                                <Button
                                    className="mt-2 "
                                    variant='outlined'
                                >
                                    Guardar
                                </Button>
                            </div>
                        </CardContent>
                    </Card>
                </Grid2>
                {
                integ.map((e, i) => (
                    <Grid2 key={i} lg={6}>
                        <Card elevation={10}> 
                            <CardContent>
                                <div className="p-2">
                                    <Typography variant="h4">
                                        {e.name}
                                    </Typography>
                                    <Typography variant="h5">
                                        Credentials: {e.username}
                                    </Typography>
                                    <Typography variant="h6">
                                        API Route: {e.ref}
                                    </Typography>
                                </div>
                                <Button>
                                    Remove
                                </Button>
                            </CardContent>
                        </Card>
                    </Grid2>
                ))
                }
            </Grid2>
        </div>

    )
}