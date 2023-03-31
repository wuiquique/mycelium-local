import { useTexts } from "@/hooks/textContext";
import {
  Button,
  Card,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";
import { FaTrashAlt } from "react-icons/fa";
import BackPage from "../../../components/BackPage";

export function Page() {
  const texts = useTexts();

  useEffect(() => {
    axios.get("/api/categories/").then((response) => {
      setCategs(response.data);
    });
  }, []);

  const [categ, setCategs] = useState<
    {
      id: number;
      name: string;
    }[]
  >([]);

  const changeInput = (index, camp, value) => {
    let temp = [...categ];
    temp[index][camp] = value;
    // console.log(temp);
    setCategs(temp);
  };

  const blurSave = (id, index) => {
    let post = {
      name: categ[index].name,
    };
    axios.put(`/api/categories/${id}`, post).then((response) => {
      // setCategs(response.data);
    });
  };

  const deleteRow = (id, i) => {
    let temp = [...categ];
    temp.splice(i, 1);
    setCategs(temp);
    axios.delete(`/api/categories/${id}`).then((response) => {
      console.log("Delete Succesfull");
    });
  };

  const addRow = (e) => {
    /*let temp = [...categ];
    temp.push({ id: categ.length + 1, name: "" });
    setCategs(temp);*/
    let post = { name: "New Category" };
    axios.post(`/api/categories/`, post).then((response) => {
      let temp = [...categ];
      temp.push({ id: response.data, name: "New Category" });
      setCategs(temp);
    });
  };

  return (
    <div className="justify-center">
      <BackPage />
      <Card className="p-4 mt-2" elevation={10} sx={{ width: "100%" }}>
        <Typography variant="h3" className="text-center">
          {texts.admincategorypage.title}
        </Typography>{" "}
        <br />
        <Card elevation={10} sx={{ minHeight: 300 }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>{texts.category.name}</TableCell>
                <TableCell>{texts.admincategorypage.deletecol}</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {categ.map((e, i) => (
                <TableRow key={i}>
                  <TableCell>
                    <TextField
                      value={e.name}
                      variant="standard"
                      onChange={(ev) => changeInput(i, "name", ev.target.value)}
                      onBlur={() => blurSave(e.id, i)}
                    />
                  </TableCell>
                  <TableCell>
                    <Button
                      className="mt-6"
                      variant="outlined"
                      color="primary"
                      onClick={() => deleteRow(e.id, i)}
                    >
                      <FaTrashAlt />
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          <div className="flex justify-center text-center">
            <Button
              className="m-6"
              variant="outlined"
              color="secondary"
              onClick={addRow}
            >
              +
            </Button>
          </div>
        </Card>
      </Card>
    </div>
  );
}
