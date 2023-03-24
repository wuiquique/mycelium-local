"use client";

import {
  Button,
  Card,
  CardContent,
  FormControl,
  TextField,
  Typography,
} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";
import axios from "axios";
import { useEffect, useState } from "react";
import BackPage from "../../../components/BackPage";

export default function page() {
    
    const [products, setProducts] = useState([])

    const changeInput = () => {
        return 0
    }

    const blurSave = () => {
        return 0
    }

  return (
    <div>
      <Grid2 container spacing={2}>
        <Grid2 lg={12}>
          <Typography variant="h3" className="text-center">
            Product Administration
          </Typography>
          <br />
          <Card elevation={10} sx={{ minHeight: 300 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Name</TableCell>
                  <TableCell>Description</TableCell>
                  <TableCell>Quantity</TableCell>
                  <TableCell>Price</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {products.length === 0 ? (
                  <></>
                ) : (
                  products.map((e, i) => (
                    <TableRow key={i}>
                      <TableCell>
                        <TextField
                          defaultValue={e.name}
                          variant="standard"
                          onChange={(ev) =>
                            changeInput(i, "name", ev.target.value)
                          }
                          onBlur={() => blurSave(e.id, i)}
                        />
                      </TableCell>
                      <TableCell>
                        <TextField
                          defaultValue={e.lastname}
                          variant="standard"
                          onChange={(ev) =>
                            changeInput(i, "lastname", ev.target.value)
                          }
                          onBlur={() => blurSave(e.id, i)}
                        />
                      </TableCell>
                      <TableCell>
                        <TextField
                          defaultValue={e.email}
                          variant="standard"
                          onChange={(ev) =>
                            changeInput(i, "email", ev.target.value)
                          }
                          onBlur={() => blurSave(e.id, i)}
                        />
                      </TableCell>
                      <TableCell>
                        <TextField
                            defaultValue={e.email}
                            variant="standard"
                            onChange={(ev) =>
                                changeInput(i, "email", ev.target.value)
                            }
                            onBlur={() => blurSave(e.id, i)}
                            />
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </Card>
        </Grid2>
      </Grid2>
    </div>
  )
}
