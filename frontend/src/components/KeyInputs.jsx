export default function KeyInputs({ initialVector, setInitialVector, cipherKey, setCipherKey }) {
  return (
    <div className="bg-white rounded-lg border border-gray-200 shadow-sm p-6">
      <h2 className="text-lg font-semibold text-gray-900 mb-4">Encryption Parameters</h2>
      
      <div className="space-y-4">
        {/* Initial Vector Input */}
        <div>
          <label htmlFor="iv" className="block text-sm font-medium text-gray-700 mb-2">
            Initial Vector (IV) <span className="text-gray-500">- 128 bit</span>
          </label>
          <input
            id="iv"
            type="text"
            value={initialVector}
            onChange={(e) => setInitialVector(e.target.value)}
            placeholder="Enter 32 hexadecimal characters (128 bits)"
            className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent font-mono text-sm"
            maxLength="32"
          />
          <p className="mt-1 text-xs text-gray-500">
            {initialVector.length}/32 characters
            {initialVector.length === 32 && <span className="text-green-600 ml-2">✓ Valid length</span>}
          </p>
        </div>

        {/* Cipher Key Input */}
        <div>
          <label htmlFor="key" className="block text-sm font-medium text-gray-700 mb-2">
            Cipher Key <span className="text-gray-500">- 256 bit</span>
          </label>
          <input
            id="key"
            type="text"
            value={cipherKey}
            onChange={(e) => setCipherKey(e.target.value)}
            placeholder="Enter 64 hexadecimal characters (256 bits)"
            className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent font-mono text-sm"
            maxLength="64"
          />
          <p className="mt-1 text-xs text-gray-500">
            {cipherKey.length}/64 characters
            {cipherKey.length === 64 && <span className="text-green-600 ml-2">✓ Valid length</span>}
          </p>
        </div>
      </div>
    </div>
  );
}
