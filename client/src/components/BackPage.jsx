"use client";

import { useTexts } from "@/hooks/textContext";
import KeyboardBackspaceOutlinedIcon from "@mui/icons-material/KeyboardBackspaceOutlined";
import { Button } from "@mui/material";
import { useRouter } from "next/navigation";

export default function BackPage() {
  const texts = useTexts();
  const router = useRouter();
  return (
    <div className="justify-left block">
      <Button size="large" onClick={() => router.back()}>
        <KeyboardBackspaceOutlinedIcon className="m-1" />
        {texts.global.backbutton}
      </Button>
    </div>
  );
}
