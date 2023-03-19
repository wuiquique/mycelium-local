"use client";

import CopyrightIcon from "@mui/icons-material/Copyright";
import { Box, Container, Typography } from "@mui/material";
import { useState } from "react";

export default function Footer() {
  const [pMeth, setPMeth] = useState([
    "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/Old_Visa_Logo.svg/2560px-Old_Visa_Logo.svg.png",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Mastercard-logo.svg/1280px-Mastercard-logo.svg.png",
    "https://1000logos.net/wp-content/uploads/2016/10/American-Express-Color.png",
    "https://www.ebenezer.org.gt/wp-content/uploads/2021/06/PayPal-Logo.png",
  ]);

  return (
    <div className="mt-6">
      <Box
        sx={{
          bgcolor: "#edebed",
          color: "grey.700",
        }}
        className="text-center justify-center"
      >
        <Container maxWidth="lg" sx={{ py: 6 }}>
          <CopyrightIcon fontSize="small" />
          <Typography variant="outline">Copyright - Mycelium</Typography>
          <div className="flex justify-center">
            {pMeth.map((e, i) => (
              <div key={i} height="5px" className="m-1">
                <img src={pMeth[i]} height="100%" width="50px" />
              </div>
            ))}
          </div>
          <Typography variant="subtitle2">
            We&apos;re your best option, what makes you, our best option.
          </Typography>
        </Container>
      </Box>
    </div>
  );
}
