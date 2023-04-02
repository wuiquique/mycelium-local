import { useTexts } from "@/hooks/textContext";
import CopyrightIcon from "@mui/icons-material/Copyright";
import { Box, Container, Typography } from "@mui/material";
import { useState } from "react";

export default function Footer() {
  const texts = useTexts();

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
          color: "grey.700",
        }}
        className="text-center justify-center"
      >
        <Container maxWidth="lg" sx={{ py: 6 }}>
          <Typography>
            <CopyrightIcon fontSize="small" /> {texts.footer.copyright}
          </Typography>
          <div>
            {pMeth.map((e, i) => (
              <img
                key={i}
                src={pMeth[i]}
                width="50"
                className="inline-block align-middle mx-1"
              />
            ))}
          </div>
          <Typography variant="subtitle2">{texts.footer.slogan}</Typography>
        </Container>
      </Box>
    </div>
  );
}
