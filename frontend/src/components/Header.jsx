import { Lock } from 'lucide-react';

export default function Header() {
  return (
    <header className="bg-white border-b border-gray-200">
      <div className="container mx-auto px-4 py-6 max-w-5xl">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-blue-600 rounded-lg flex items-center justify-center">
            <Lock className="w-6 h-6 text-white" />
          </div>
          <div>
            <h1 className="text-2xl font-semibold text-gray-900">
              Sosemanuk Cipher Tool
            </h1>
            <p className="text-sm text-gray-600">
              Secure text encryption and decryption
            </p>
          </div>
        </div>
      </div>
    </header>
  );
}
