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

export default function UserAddress({ products }) {
  const [date1, setDate1] = useState("2014-08-18T21:11:54");
  const [date2, setDate2] = useState("2014-08-18T21:11:54");

  const handleSubmit = (event) => {
    event.preventDefault();
    let dirHolder = {
      direction: event.target.direction.value,
      state: event.target.state.value,
      city: event.target.city.value,
      zip: event.target.zip.value,
      phone: event.target.phone.value,
      since: date1,
      till: date2,
    };
    let prodHolder = products;
    console.log({ dir: dirHolder, products: prodHolder });
    //no se como se va a ser el body pero aca ta adelantado
    axios
      .post("url", { dir: dirHolder, products: prodHolder })
      .then((response) => {
        console.log("si");
      });
  };

  return (
    <div>
      <Card className="p-4" elevation={10}>
        <CardContent>
          <Typography variant="h4" className="text-center">
            Mailing Address
          </Typography>
          <form onSubmit={handleSubmit}>
            <TextField
              sx={{ minWidth: "100%" }}
              label="Address"
              variant="standard"
              name="direction"
            />
            <div>
              <TextField
                label="State"
                variant="standard"
                className="mr-1"
                name="state"
              />
              <TextField label="City" variant="standard" name="city" />
            </div>
            <div>
              <TextField
                label="Zip"
                variant="standard"
                className="mr-1"
                name="zip"
              />
              <TextField label="Phone" variant="standard" name="phone" />
            </div>
            <br />
            <Typography variant="h5" className="text-center">
              Delivery Time Range
            </Typography>
            <br />
            <div className="flex">
              <DateTimePicker
                renderInput={(props) => <TextField {...props} />}
                label="From"
                value={date1}
                onChange={(newValue) => {
                  setDate1(newValue);
                }}
              />{" "}
              &nbsp;
              <DateTimePicker
                renderInput={(props) => <TextField {...props} />}
                label="To"
                value={date2}
                onChange={(newValue) => {
                  setDate2(newValue);
                }}
              />
            </div>
            <div className="text-center mt-3">
              <Button type="submit" variant="outlined">
                Place Order
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
