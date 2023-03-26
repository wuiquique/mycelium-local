import { Button, Card, CardMedia, TextField, Typography } from "@mui/material";
import axios from "axios";
import { navigate } from "vite-plugin-ssr/client/router";
import BackPage from "../../components/BackPage";
import { useUser } from "../../hooks/userContext";

export function Page() {
  const [, setUser] = useUser();

  const handleSubmit = async (e) => {
    e.preventDefault();
    let post = {
      username: e.target.email.value,
      password: e.target.password.value,
      name: e.target.first_name.value,
      lastname: e.target.last_name.value,
    };
    try {
      const registerResponse = await axios.post("/api/register", post);

      if (registerResponse.data.code === "fail") {
        // TODO: tell user
        return;
      }

      const sessionResponse = await axios.get("/api/session");

      if (!sessionResponse.data.id) {
        // TODO: tell user;
        return;
      }

      setUser(sessionResponse.data);
      navigate("/");
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div>
      <BackPage />
      <div className="flex justify-center text-center">
        <Card className="p-4" elevation={10} sx={{ maxWidth: 345 }}>
          <CardMedia
            component="img"
            height="100%"
            image="/redwhite.png"
            alt="Mycelium Logo"
          />
          <Typography variant="h4" mt={2}>
            mycelium :)
          </Typography>
          <form className="mt-8 mb-11" onSubmit={handleSubmit}>
            <TextField
              label="First Name"
              variant="standard"
              name="first_name"
            />
            <TextField label="Last Name" variant="standard" name="last_name" />
            <TextField label="Email" variant="standard" name="email" />
            <TextField label="Password" variant="standard" name="password" />
            <br />
            <Button
              className="mt-6"
              variant="outlined"
              disableElevation
              color="secondary"
              type="submit"
            >
              Register
            </Button>
          </form>
        </Card>
      </div>
    </div>
  );
}
