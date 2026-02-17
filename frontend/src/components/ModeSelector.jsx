import { Lock, Unlock } from 'lucide-react';

export default function ModeSelector({ mode, setMode }) {
  return (
    <div className="flex justify-center">
      <div className="inline-flex bg-white border border-gray-300 rounded-lg p-1">
        <button
          onClick={() => setMode('encrypt')}
          className={`flex items-center gap-2 px-6 py-2 rounded-md transition-colors font-medium ${
            mode === 'encrypt'
              ? 'bg-blue-600 text-white'
              : 'text-gray-700 hover:bg-gray-100'
          }`}
        >
          <Lock className="w-4 h-4" />
          Encrypt
        </button>
        <button
          onClick={() => setMode('decrypt')}
          className={`flex items-center gap-2 px-6 py-2 rounded-md transition-colors font-medium ${
            mode === 'decrypt'
              ? 'bg-blue-600 text-white'
              : 'text-gray-700 hover:bg-gray-100'
          }`}
        >
          <Unlock className="w-4 h-4" />
          Decrypt
        </button>
      </div>
    </div>
  );
}
