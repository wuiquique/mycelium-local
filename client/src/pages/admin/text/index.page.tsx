import type { TextMap } from "@/hooks/textContext";
import { defaultKeys, useTexts } from "@/hooks/textContext";
import { Button, Card, CardContent, TextField } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import axios from "axios";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { MdAdd } from "react-icons/md";

export function Page() {
  const texts = useTexts();

  const valueRef = useRef<HTMLInputElement | null>(null);

  const [dbTexts, setTexts] = useState<TextMap | null>(null);

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

  const editValue = useCallback(
    (v: { component: string; key: string; value: string }) => {
      if (valueRef.current === null) return;
      setUnsavedText({ ...v });
      valueRef.current.focus();
    },
    []
  );

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
    const texts: TextMap = { ...defaultKeys, ...listRes.data };
    for (let [component, compmap] of Object.entries(texts)) {
      texts[component] = {
        ...defaultKeys[component as keyof typeof defaultKeys],
        ...compmap,
      };
    }
    setTexts(texts);
  }, [unsavedText]);

  useEffect(() => {
    axios.get("/api/text").then((res) => {
      const texts: TextMap = { ...defaultKeys, ...res.data };
      for (let [component, compmap] of Object.entries(texts)) {
        texts[component] = {
          ...defaultKeys[component as keyof typeof defaultKeys],
          ...compmap,
        };
      }
      setTexts(texts);
    });
  }, []);

  return (
    <Card>
      <CardContent>
        <DataGrid
          columns={[
            {
              field: "component",
              headerName: texts.admintextpage.component,
              flex: 0.5,
            },
            {
              field: "key",
              headerName: texts.admintextpage.key,
              flex: 0.5,
            },
            {
              field: "value",
              headerName: texts.admintextpage.value,
              flex: 1,
            },
          ]}
          autoHeight
          rows={flattened}
          initialState={{
            pagination: {
              paginationModel: {
                pageSize: 5,
              },
            },
          }}
          pageSizeOptions={[5, 10, 15]}
          getRowId={(t) => `${t.component}.${t.key}`}
          rowSelection={false}
          onRowClick={(v) => editValue(v.row)}
        />
        <form
          onSubmit={(e) => {
            e.preventDefault();
            e.stopPropagation();
            if (!e.currentTarget.checkValidity()) {
              e.currentTarget.reportValidity();
              return;
            }
            saveText();
          }}
        >
          <TextField
            label={texts.admintextpage.component}
            variant="standard"
            required
            value={unsavedText.component}
            onChange={(e) =>
              setUnsavedText({
                ...unsavedText,
                component: e.currentTarget.value,
              })
            }
          />
          <TextField
            label={texts.admintextpage.key}
            variant="standard"
            required
            value={unsavedText.key}
            onChange={(e) =>
              setUnsavedText({
                ...unsavedText,
                key: e.currentTarget.value,
              })
            }
          />
          <TextField
            inputRef={valueRef}
            label={texts.admintextpage.value}
            variant="standard"
            value={unsavedText.value}
            onChange={(e) =>
              setUnsavedText({
                ...unsavedText,
                value: e.currentTarget.value,
              })
            }
          />
          <Button type="submit">
            <MdAdd />
          </Button>
        </form>
      </CardContent>
    </Card>
  );
}
