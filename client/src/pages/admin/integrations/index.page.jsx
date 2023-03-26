import {
  Button,
  Card,
  CardContent,
  TextField,
  Typography,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import { useEffect, useState } from "react";
import BackPage from "../../../components/BackPage";

export function Page() {
  const [integ, setInteg] = useState([]);
  const [showPass, setShowPass] = useState(false);

  useEffect(() => {
    axios.get("/api/integration/").then((response) => {
      console.log(response.data);
      setInteg(response.data);
    });
  }, []);

  const submitInt = (e) => {
    e.preventDefault();

    let post = {
      name: e.target.int_name.value,
      request: e.target.int_ref.value,
      user: e.target.int_user.value,
      password: e.target.int_pass.value,
    };

    axios.post("/api/integration/", post).then((response) => {
      console.log(response.data);
      setInteg(response.data);
      e.target.reset();
    });
  };

  const deleteInt = (id) => {
    axios.delete(`/api/integration/${id}`).then((response) => {
      setInteg(response.data);
    });
  };

  const editInt = (e, id) => {
    e.preventDefault();

    let post = {
      name: e.target.int_name.value,
      request: e.target.int_ref.value,
      user: e.target.int_user.value,
      password: e.target.int_pass.value,
    };

    axios.put(`/api/integration/${id}`, post).then((response) => {
      setInteg(response.data);
    });
  };

  return (
    <div>
      <BackPage />
      <Typography variant="h3" className="text-center">
        Integrations
      </Typography>
      <br />
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Card elevation={10}>
            <CardContent>
              <form onSubmit={submitInt}>
                <div className="flex justify-between">
                  <TextField
                    sx={{ minWidth: "33%" }}
                    variant="standard"
                    label="Nombre"
                    name="int_name"
                  />
                  <TextField
                    sx={{ minWidth: "33%" }}
                    variant="standard"
                    label="Usuario"
                    name="int_user"
                  />
                  <TextField
                    sx={{ minWidth: "33%" }}
                    variant="standard"
                    label="Contraseña"
                    name="int_pass"
                    type="password"
                  />
                </div>
                <br />
                <TextField
                  sx={{ minWidth: "100%" }}
                  variant="standard"
                  label="Ruta"
                  name="int_ref"
                />
                <div className="flex justify-center">
                  <Button className="mt-3" variant="outlined" type="submit">
                    Guardar
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        </Grid2>
        {integ.map((e, i) => (
          <Grid2 key={i} lg={6}>
            <Card elevation={10}>
              <CardContent>
                <form onSubmit={(ev) => editInt(ev, e.id)}>
                  <div className="p-2 flex justify-between">
                    <TextField
                      variant="standard"
                      defaultValue={e.name}
                      name="int_name"
                    />
                    &nbsp;
                    <TextField
                      defaultValue={e.user}
                      variant="standard"
                      name="int_user"
                    />
                    &nbsp;
                    <TextField
                      defaultValue={e.password}
                      variant="standard"
                      name="int_pass"
                      type="password"
                    />
                  </div>
                  <TextField
                    sx={{ minWidth: "100%" }}
                    defaultValue={e.request}
                    variant="standard"
                    name="int_ref"
                  />
                  <br />
                  <div className="flex justify-center">
                    <Button type="submit">Update</Button>
                    <Button
                      onClick={() => {
                        deleteInt(e.id);
                      }}
                    >
                      Remove
                    </Button>
                  </div>
                </form>
              </CardContent>
            </Card>
          </Grid2>
        ))}
      </Grid2>
    </div>
  );
}
