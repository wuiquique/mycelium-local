import { Card, CardContent, TextField, Typography } from "@mui/material";

export default function CreditCard() {
  return (
    <div>
      <Card className="p-4" elevation={10}>
        <CardContent>
          <Typography variant="h4" className="text-center">
            Credit Card Information
          </Typography>
          <TextField
            sx={{ minWidth: "100%" }}
            label="Card Number"
            variant="standard"
            inputProps={{ maxLength: 16 }}
          />
          <TextField
            sx={{ minWidth: "100%" }}
            label="Name on Credit Card"
            variant="standard"
          />
          <div>
            <TextField
              label="Expiration Date"
              variant="standard"
              className="mr-1"
            />
            <TextField label="CVV Code" variant="standard" />
          </div>
          <br />
          <br />
          <div>
            <Typography variant="h4" className="text-center">
              Billing Address
            </Typography>
            <TextField
              sx={{ minWidth: "100%" }}
              label="Full Address"
              variant="standard"
            />
            <TextField
              sx={{ minWidth: "100%" }}
              label="Dept No. / House No. / Access Code"
              variant="standard"
            />
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
