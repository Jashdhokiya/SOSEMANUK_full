export default function TextInput({ mode, inputText, setInputText }) {
  return (
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-2">
        {mode === 'encrypt' ? 'Input Text' : 'Encrypted Text'}
      </label>
      <textarea
        value={inputText}
        onChange={(e) => setInputText(e.target.value)}
        placeholder={
          mode === 'encrypt'
            ? 'Enter text to encrypt...'
            : 'Enter encrypted text to decrypt...'
        }
        className="w-full h-40 px-4 py-3 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
        rows={6}
      />
      <div className="mt-2 text-sm text-gray-500">
        {inputText.length} characters
      </div>
    </div>
  );
}
