import ModeSelector from './ModeSelector';
import KeyInputs from './KeyInputs';
import TextInput from './TextInput';
import TextOutput from './TextOutput';

export default function EncryptionForm({ mode, setMode, inputText, setInputText, outputText, handleProcess, initialVector, setInitialVector, cipherKey, setCipherKey }) {
  return (
    <div className="space-y-6">
      <ModeSelector mode={mode} setMode={setMode} />
      
      <KeyInputs 
        initialVector={initialVector}
        setInitialVector={setInitialVector}
        cipherKey={cipherKey}
        setCipherKey={setCipherKey}
      />
      
      <div className="bg-white rounded-lg border border-gray-200 shadow-sm p-6">
        <div className="space-y-6">
          <TextInput 
            mode={mode}
            inputText={inputText}
            setInputText={setInputText}
          />

          <div className="flex justify-center">
            <button
              onClick={handleProcess}
              disabled={!inputText}
              className="px-8 py-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors font-medium"
            >
              {mode === 'encrypt' ? 'Encrypt' : 'Decrypt'}
            </button>
          </div>

          <TextOutput 
            mode={mode}
            outputText={outputText}
          />
        </div>
      </div>

      <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
        <p className="text-sm text-gray-700">
          <strong>Algorithm:</strong> Sosemanuk is a software-oriented stream cipher 
          designed for high-performance encryption. It was selected as part of the 
          eSTREAM portfolio for Profile 1 (software applications).
        </p>
      </div>
    </div>
  );
}