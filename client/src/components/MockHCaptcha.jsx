import { useState } from "react";

export default function MockHCaptcha({ setVerified }) {
  const [responseToken, setResponseToken] = useState("");

  function handleVerify() {
    setResponseToken("4185bfd3-1b5b-4c4c-8f64-b0031181b203");
    setVerified(true);
  }

  return (
    <div>
      <div
        style={{
          backgroundColor: "white",
          border: "1px solid #c1c1c1",
          borderRadius: "4px",
          padding: "20px",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          minHeight: "78px",
        }}
      >
        <div
          style={{
            margin: "0 0 20px 0",
            fontSize: "16px",
            fontWeight: "bold",
            color: "#222",
            textAlign: "center",
          }}
        >
          Are you a robot?
        </div>
        {!responseToken && (
          <button
            style={{
              backgroundColor: "#f9a93c",
              color: "white",
              borderRadius: "4px",
              padding: "10px 20px",
              fontSize: "14px",
              fontWeight: "bold",
              border: "none",
              cursor: "pointer",
            }}
            onClick={handleVerify}
          >
            Verify
          </button>
        )}
        {responseToken && (
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              justifyContent: "center",
            }}
          >
            <div
              style={{
                margin: "0 0 20px 0",
                fontSize: "16px",
                fontWeight: "bold",
                color: "#222",
                textAlign: "center",
              }}
            >
              Verification Complete
            </div>
            <div
              style={{
                margin: "0 0 10px 0",
                fontSize: "14px",
                color: "#666",
                textAlign: "center",
              }}
            >
              Response Token:
            </div>
            <div
              style={{
                margin: "0 0 20px 0",
                fontSize: "14px",
                fontWeight: "bold",
                color: "#222",
                textAlign: "center",
              }}
            >
              {responseToken}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
