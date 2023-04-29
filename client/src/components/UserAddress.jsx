import { useTexts } from "@/hooks/textContext";
import { useUser } from "@/hooks/userContext";
import {
  Button,
  Card,
  CardContent,
  TextField,
  Typography,
} from "@mui/material";
import { DateTimePicker } from "@mui/x-date-pickers";
import axios from "axios";
import { useState } from "react";
import { navigate } from "vite-plugin-ssr/client/router";

export default function UserAddress({ products }) {
  const texts = useTexts();
  const [date1, setDate1] = useState(new Date().toISOString().slice(0, 19));
  const [date2, setDate2] = useState(new Date().toISOString().slice(0, 19));
  const [user] = useUser();

  const handleSubmit = (event) => {
    if (user.id === null) return;
    event.preventDefault();
    let dirHolder = {
      userId: user.id,
      direction: event.target.direction.value,
      state: event.target.state.value,
      city: event.target.city.value,
      zip: event.target.zip.value,
      phone: event.target.phone.value,
      since: date1,
      till: date2,
    };
    let temp = [];
    for (let p of products) {
      let o = {
        name: p.name,
        categoryId: p.categoryId,
        boughtPrice: p.price,
        porcentage: 30,
        quantity: p.quantity,
        weight: p.weight,
        international: p.integrationId !== null,
      };
      temp.push(o);
    }

    //no se como se va a ser el body pero aca ta adelantado
    axios.post("/api/user/order/", dirHolder).then((response) => {
      axios.post("/api/user/order/impuestos", temp).then((r) => {
        navigate(`/user/orders/${response.data}`);
      });
    });
  };

  return (
    <div>
      <Card className="p-4" elevation={10}>
        <CardContent>
          <Typography variant="h4" className="text-center">
            {texts.checkoutpage.mailing}
          </Typography>
          <form onSubmit={handleSubmit}>
            <TextField
              sx={{ minWidth: "100%" }}
              label={texts.checkoutpage.mailingaddr}
              variant="standard"
              name="direction"
            />
            <div>
              <TextField
                label={texts.checkoutpage.mailingstate}
                variant="standard"
                className="mr-1"
                name="state"
              />
              <TextField
                label={texts.checkoutpage.mailingcity}
                variant="standard"
                name="city"
              />
            </div>
            <div>
              <TextField
                label={texts.checkoutpage.mailingzip}
                variant="standard"
                className="mr-1"
                name="zip"
              />
              <TextField
                label={texts.checkoutpage.mailingphone}
                variant="standard"
                name="phone"
              />
            </div>
            <br />
            <Typography variant="h5" className="text-center">
              {texts.checkoutpage.deliveryrange}
            </Typography>
            <br />
            <div className="flex">
              <DateTimePicker
                renderInput={(props) => <TextField {...props} />}
                label={texts.checkoutpage.deliveryfrom}
                value={date1}
                onChange={(newValue) => {
                  if (newValue === null) return;
                  setDate1(newValue);
                }}
              />{" "}
              &nbsp;
              <DateTimePicker
                renderInput={(props) => <TextField {...props} />}
                label={texts.checkoutpage.deliveryto}
                value={date2}
                onChange={(newValue) => {
                  if (newValue === null) return;
                  setDate2(newValue);
                }}
              />
            </div>
            <div className="text-center mt-3">
              <Button type="submit" variant="outlined">
                {texts.checkoutpage.placeorder}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
