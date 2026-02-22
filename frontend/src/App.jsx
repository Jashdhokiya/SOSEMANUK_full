import { useState } from "react";
import Header from "./components/Header";
import EncryptionForm from "./components/EncryptionForm";
import Footer from "./components/Footer";

export default function App() {
  const [mode, setMode] = useState("encrypt");
  const [inputText, setInputText] = useState("");
  const [outputText, setOutputText] = useState("");
  const [initialVector, setInitialVector] = useState("");
  const [cipherKey, setCipherKey] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [pdfFile, setPdfFile] = useState(null);

  const handleProcess = async () => {
    setLoading(true);
    setError("");
    setOutputText("");

    try {
      // ================= PDF FLOW =================
      if (pdfFile) {
        const endpoint =
          mode === "encrypt"
            ? "http://localhost:8080/crypto/encrypt-pdf"
            : "http://localhost:8080/crypto/decrypt-pdf";

        const formData = new FormData();
        formData.append("file", pdfFile);
        formData.append("key", cipherKey);
        formData.append("iv", initialVector);

        const response = await fetch(endpoint, {
          method: "POST",
          body: formData,
        });

        if (!response.ok) throw new Error();

        const blob = await response.blob();

        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download =
          mode === "encrypt" ? "encrypted.pdf" : "decrypted.pdf";
        a.click();
      }
      // ================= TEXT FLOW =================
      else {
        const endpoint =
          mode === "encrypt"
            ? "http://localhost:8080/crypto/encrypt"
            : "http://localhost:8080/crypto/decrypt";

        const response = await fetch(endpoint, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            msg: inputText,
            key: cipherKey,
            iv: initialVector,
          }),
        });

        if (!response.ok) throw new Error();

        const data = await response.text();
        setOutputText(data);
      }
    } catch (err) {
      setError("Failed to process request");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-blue-50 flex flex-col">
      <Header />

      <main className="flex-1 flex items-center justify-center px-4 py-10">
      <div className="w-full max-w-2xl">
        <EncryptionForm
          mode={mode}
          setMode={setMode}
          inputText={inputText}
          setInputText={setInputText}
          outputText={outputText}
          handleProcess={handleProcess}
          setOutputText={setOutputText}
          initialVector={initialVector}
          setInitialVector={setInitialVector}
          cipherKey={cipherKey}
          setCipherKey={setCipherKey}
          pdfFile={pdfFile}
          setPdfFile={setPdfFile}
          loading={loading}
          error={error}
        />
        </div>
      </main>

      <Footer />
    </div>
  );
}