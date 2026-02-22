import { useState, useEffect } from "react";
import { Upload, FileText, File } from "lucide-react";
import KeyInputs from "./KeyInputs";
import TextInput from "./TextInput";
import TextOutput from "./TextOutput";

export default function EncryptionForm({
  mode,
  setMode,
  inputText,
  setInputText,
  outputText,
  setOutputText,   // ✅ ADD THIS
  handleProcess,
  initialVector,
  setInitialVector,
  cipherKey,
  setCipherKey,
  pdfFile,
  setPdfFile,
  loading,
  error,
}) {
  const [inputType, setInputType] = useState("text");

  // Reset when switching mode or input type
  useEffect(() => {
    setInputText("");
    setOutputText("");
    setCipherKey("");
    setInitialVector("");
    setPdfFile(null);
  }, [mode, inputType]);

  return (
    <div className="space-y-8">

      {/* Encrypt / Decrypt Toggle */}
      <div className="flex bg-gray-200 rounded-full p-1 w-64 mx-auto">
        <button
          onClick={() => setMode("encrypt")}
          className={`flex-1 py-2 rounded-full transition ${
            mode === "encrypt"
              ? "bg-blue-600 text-white"
              : "text-gray-600"
          }`}
        >
          Encrypt
        </button>

        <button
          onClick={() => setMode("decrypt")}
          className={`flex-1 py-2 rounded-full transition ${
            mode === "decrypt"
              ? "bg-blue-600 text-white"
              : "text-gray-600"
          }`}
        >
          Decrypt
        </button>
      </div>

      {/* Text / PDF Selector */}
      <div className="grid grid-cols-2 gap-4 max-w-md mx-auto">
        <div
          onClick={() => setInputType("text")}
          className={`p-4 border rounded-xl cursor-pointer transition ${
            inputType === "text"
              ? "border-blue-600 bg-blue-50"
              : "border-gray-300"
          }`}
        >
          <FileText className="mx-auto mb-2" />
          <p className="text-center font-medium">Text Mode</p>
        </div>

        <div
          onClick={() => setInputType("pdf")}
          className={`p-4 border rounded-xl cursor-pointer transition ${
            inputType === "pdf"
              ? "border-blue-600 bg-blue-50"
              : "border-gray-300"
          }`}
        >
          <File className="mx-auto mb-2" />
          <p className="text-center font-medium">PDF Mode</p>
        </div>
      </div>

      <KeyInputs
        initialVector={initialVector}
        setInitialVector={setInitialVector}
        cipherKey={cipherKey}
        setCipherKey={setCipherKey}
      />

      <div className="bg-white rounded-2xl border shadow-sm p-6 space-y-6">

        {/* Input Area */}
        {inputType === "text" ? (
          <TextInput
            mode={mode}
            inputText={inputText}
            setInputText={setInputText}
          />
        ) : (
          <div>
            <label className="block text-sm font-medium mb-2">
              Upload PDF
            </label>

            <label className="flex flex-col items-center justify-center w-full h-40 border-2 border-dashed border-gray-300 rounded-xl cursor-pointer hover:bg-gray-50 transition">
              <Upload className="w-8 h-8 text-gray-500 mb-2" />
              <span className="text-sm text-gray-600">
                Click to upload PDF
              </span>

              <input
                type="file"
                accept="application/pdf"
                onChange={(e) => setPdfFile(e.target.files[0])}
                className="hidden"
              />
            </label>

            {pdfFile && (
              <div className="flex justify-between items-center bg-green-50 border border-green-200 rounded-lg p-3 mt-3">
                <span className="text-sm text-green-700">
                  {pdfFile.name}
                </span>

                <button
                  onClick={() => setPdfFile(null)}
                  className="text-red-500 text-sm"
                >
                  Remove
                </button>
              </div>
            )}
          </div>
        )}

        {/* Button */}
        <div className="flex justify-center">
          <button
            onClick={handleProcess}
            disabled={
              loading ||
              (inputType === "text" ? !inputText : !pdfFile)
            }
            className="px-8 py-3 bg-blue-600 text-white rounded-xl hover:bg-blue-700 disabled:bg-gray-300 transition font-medium"
          >
            {loading
              ? "Processing..."
              : mode === "encrypt"
              ? `Encrypt ${inputType === "pdf" ? "PDF" : "Text"}`
              : `Decrypt ${inputType === "pdf" ? "PDF" : "Text"}`}
          </button>
        </div>

        {/* Output (Text Mode Only) */}
        {inputType === "text" && (
          <TextOutput mode={mode} outputText={outputText} />
        )}

        {error && (
          <div className="text-center text-red-500 text-sm">
            {error}
          </div>
        )}
      </div>
    </div>
  );
}