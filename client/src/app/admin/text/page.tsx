"use client";

import { useTexts } from "@/hooks/textContext";
import {
  Button,
  Card,
  CardContent,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
} from "@mui/material";
import axios from "axios";
import { useCallback, useEffect, useMemo, useState } from "react";
import { MdAdd } from "react-icons/md";

export default function AdminTexts(props: any) {
  const texts = useTexts();

  const [dbTexts, setTexts] = useState<Record<
    string,
    Record<string, string>
  > | null>(null);

  useEffect(() => {
    axios.get("/api/text").then((res) => {
      setTexts(res.data);
    });
  }, []);

  const flattened = useMemo(
    () =>
      dbTexts === null
        ? []
        : Object.entries(dbTexts).flatMap(([component, compmap]) =>
            Object.entries(compmap).map(([key, value]) => ({
              component,
              key,
              value,
            }))
          ),
    [dbTexts]
  );

  const [unsavedText, setUnsavedText] = useState({
    component: "",
    key: "",
    value: "",
  });

  const saveText = useCallback(async () => {
    if (unsavedText.component.trim() === "" || unsavedText.key.trim() === "")
      return;

    await axios.put("/api/text", unsavedText);
    setUnsavedText({
      component: "",
      key: "",
      value: "",
    });

    const listRes = await axios.get("/api/text");
    setTexts(listRes.data);
  }, [unsavedText]);

  return (
    <Card>
      <CardContent>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>{texts.admintext.component}</TableCell>
              <TableCell>{texts.admintext.key}</TableCell>
              <TableCell>{texts.admintext.value}</TableCell>
              <TableCell></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {flattened.map((v) => (
              <TableRow key={`${v.component}.${v.key}`}>
                <TableCell>{v.component}</TableCell>
                <TableCell>{v.key}</TableCell>
                <TableCell>{v.value}</TableCell>
                <TableCell></TableCell>
              </TableRow>
            ))}
            <TableRow>
              <TableCell>
                <TextField
                  label={texts.admintext.component}
                  variant="standard"
                  value={unsavedText.component}
                  onChange={(e) =>
                    setUnsavedText({
                      ...unsavedText,
                      component: e.currentTarget.value,
                    })
                  }
                />
              </TableCell>
              <TableCell>
                <TextField
                  label={texts.admintext.key}
                  variant="standard"
                  value={unsavedText.key}
                  onChange={(e) =>
                    setUnsavedText({
                      ...unsavedText,
                      key: e.currentTarget.value,
                    })
                  }
                />
              </TableCell>
              <TableCell>
                <TextField
                  label={texts.admintext.value}
                  variant="standard"
                  value={unsavedText.value}
                  onChange={(e) =>
                    setUnsavedText({
                      ...unsavedText,
                      value: e.currentTarget.value,
                    })
                  }
                />
              </TableCell>
              <TableCell>
                <Button onClick={saveText}>
                  <MdAdd />
                </Button>
              </TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}
