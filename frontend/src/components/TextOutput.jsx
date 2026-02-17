import { useState } from 'react';
import { Copy, Check } from 'lucide-react';

export default function TextOutput({ mode, outputText }) {
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    const textArea = document.createElement('textarea');
    textArea.value = outputText;
    textArea.style.position = 'fixed';
    textArea.style.left = '-999999px';
    document.body.appendChild(textArea);
    textArea.select();
    document.execCommand('copy');
    document.body.removeChild(textArea);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div>
      <div className="flex items-center justify-between mb-2">
        <label className="block text-sm font-medium text-gray-700">
          {mode === 'encrypt' ? 'Encrypted Output' : 'Decrypted Output'}
        </label>
        {outputText && (
          <button
            onClick={handleCopy}
            className="flex items-center gap-1 px-3 py-1 text-sm text-blue-600 hover:bg-blue-50 rounded transition-colors"
          >
            {copied ? (
              <>
                <Check className="w-4 h-4" />
                Copied
              </>
            ) : (
              <>
                <Copy className="w-4 h-4" />
                Copy
              </>
            )}
          </button>
        )}
      </div>
      <textarea
        value={outputText}
        readOnly
        placeholder={
          mode === 'encrypt'
            ? 'Encrypted output will appear here...'
            : 'Decrypted output will appear here...'
        }
        className="w-full h-40 px-4 py-3 bg-gray-50 border border-gray-300 rounded-md resize-none"
        rows={6}
      />
    </div>
  );
}
