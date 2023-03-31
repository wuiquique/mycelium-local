import { useTexts } from "@/hooks/textContext";
import { Button, Card, CardMedia, TextField, Typography } from "@mui/material";
import axios from "axios";
import { navigate } from "vite-plugin-ssr/client/router";
import BackPage from "../../components/BackPage";
import { useUser } from "../../hooks/userContext";

export function Page() {
  const texts = useTexts();
  const [, setUser] = useUser();

  const handleSubmit = async (e) => {
    e.preventDefault();
    let post = {
      username: e.target.email_login.value,
      password: e.target.password_login.value,
    };
    try {
      await axios.post("/api/login", post);
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
            alt={texts.global.shopname}
          />
          <Typography variant="h4" mt={2}>
            {texts.global.shopname}
          </Typography>
          <form className="mt-8 mb-11" onSubmit={handleSubmit}>
            <TextField
              label={texts.user.email}
              variant="standard"
              name="email_login"
            />
            <TextField
              label={texts.user.password}
              variant="standard"
              name="password_login"
              type="password"
            />
            <br />
            <Button
              className="mt-6"
              variant="outlined"
              disableElevation
              color="secondary"
              type="submit"
            >
              {texts.loginpage.loginbutton}
            </Button>
          </form>
        </Card>
      </div>
    </div>
  );
}
