import { useTexts } from "@/hooks/textContext";
import { Card, CardContent, TextField, Typography } from "@mui/material";

export default function CreditCard() {
  const texts = useTexts();

  return (
    <div>
      <Card className="p-4" elevation={10}>
        <CardContent>
          <Typography variant="h4" className="text-center">
            {texts.checkoutpage.creditcardinfo}
          </Typography>
          <TextField
            sx={{ minWidth: "100%" }}
            label={texts.checkoutpage.cardnumber}
            variant="standard"
            inputProps={{ maxLength: 16 }}
          />
          <TextField
            sx={{ minWidth: "100%" }}
            label={texts.checkoutpage.cardname}
            variant="standard"
          />
          <div>
            <TextField
              label={texts.checkoutpage.cardexpiration}
              variant="standard"
              className="mr-1"
            />
            <TextField label={texts.checkoutpage.cardcvv} variant="standard" />
          </div>
          <br />
          <br />
          <div>
            <Typography variant="h4" className="text-center">
              {texts.checkoutpage.billing}
            </Typography>
            <TextField
              sx={{ minWidth: "100%" }}
              label={texts.checkoutpage.billingaddr}
              variant="standard"
            />
            <TextField
              sx={{ minWidth: "100%" }}
              label={texts.checkoutpage.billingspec}
              variant="standard"
            />
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
