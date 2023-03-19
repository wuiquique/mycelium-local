"use client";

import KeyboardBackspaceOutlinedIcon from "@mui/icons-material/KeyboardBackspaceOutlined";
import { Button } from "@mui/material";
import { useRouter } from "next/navigation";

export default function BackPage() {
  const router = useRouter();
  return (
    <div className="justify-left block">
      <Button size="large" onClick={() => router.back()}>
        <KeyboardBackspaceOutlinedIcon className="m-1" />
        Back
      </Button>
    </div>
  );
}
