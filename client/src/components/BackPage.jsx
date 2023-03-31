import { useTexts } from "@/hooks/textContext";
import KeyboardBackspaceOutlinedIcon from "@mui/icons-material/KeyboardBackspaceOutlined";
import { Button } from "@mui/material";

export default function BackPage() {
  const texts = useTexts();
  return (
    <div className="justify-left block">
      <Button size="large" onClick={() => window.history.back()}>
        <KeyboardBackspaceOutlinedIcon className="m-1" />
        {texts.global.backbutton}
      </Button>
    </div>
  );
}
