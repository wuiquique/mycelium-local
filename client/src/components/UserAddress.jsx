"use client";

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

export default function UserAddress() {
  return (
    <div>
        <Card className="p-4" elevation={10}>
            <CardContent>
                <div>
                    <Typography variant='h4'>Mailing Address</Typography>
                    <TextField
                        sx={{ minWidth:'100%' }}
                        label='Full Address'
                        variant='standard'
                    />
                    <TextField
                        sx={{ minWidth:'100%' }}
                        label='Dept No. / House No. / Access Code'
                        variant='standard'
                    />
                    
                </div>
            </CardContent>
        </Card>
    </div>
  )
}
