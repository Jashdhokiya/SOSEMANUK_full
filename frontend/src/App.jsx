import { useState } from 'react';
import Header from './components/Header';
import EncryptionForm from './components/EncryptionForm';
import Footer from './components/Footer';

export default function App() {
  const [mode, setMode] = useState('encrypt');
  const [inputText, setInputText] = useState('');
  const [outputText, setOutputText] = useState('');
  const [initialVector, setInitialVector] = useState('');
  const [cipherKey, setCipherKey] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleProcess = async () => {
    if (!inputText) return;

    setLoading(true);
    setError('');
    setOutputText('');

    try {
      const endpoint =
  mode === 'encrypt'
    ? 'http://localhost:8080/crypto/encrypt'
    : 'http://localhost:8080/crypto/decrypt';
    
      const response = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          msg: inputText,
          key: cipherKey,
          iv: initialVector
        })
      });

      if (!response.ok) {
        throw new Error('Server error');
      }

      const data = await response.text();

      setOutputText(data);
    } catch (err) {
      setError('Failed to connect to backend');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <Header />

      <main className="flex-1 container mx-auto px-4 py-8 max-w-5xl">
        <EncryptionForm
          mode={mode}
          setMode={setMode}
          inputText={inputText}
          setInputText={setInputText}
          outputText={outputText}
          handleProcess={handleProcess}
          initialVector={initialVector}
          setInitialVector={setInitialVector}
          cipherKey={cipherKey}
          setCipherKey={setCipherKey}
          loading={loading}
          error={error}
        />
      </main>

      <Footer />
    </div>
  );
}
