"use client";

import React, { useEffect, useState, useRouter } from "react";
import {
  Card,
  CardContent,
  CardMedia,
  Button,
  TextField,
  Typography,
  CardHeader,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import KeyboardBackspaceOutlinedIcon from "@mui/icons-material/KeyboardBackspaceOutlined";
import Link from "next/link";

export default function BackPage() {
  const router = useRouter;
  return (
    <div className="justify-left block">
      <Button size="large" component={Link} href="javascript:history.back()">
        <KeyboardBackspaceOutlinedIcon className="m-1" />
        Back
      </Button>
    </div>
  );
}
