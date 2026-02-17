# Sosemanuk Encryption Tool

A professional encryption/decryption web application using the Sosemanuk algorithm.

## Setup Instructions

1. **Install dependencies:**
   ```bash
   npm install
   ```

2. **Run development server:**
   ```bash
   npm run dev
   ```

3. **Build for production:**
   ```bash
   npm run build
   ```

4. **Preview production build:**
   ```bash
   npm run preview
   ```

## Project Structure

```
/
├── components/          # React components
│   ├── Header.jsx
│   ├── Footer.jsx
│   ├── EncryptionForm.jsx
│   ├── ModeSelector.jsx
│   ├── TextInput.jsx
│   └── TextOutput.jsx
├── styles/
│   └── globals.css     # Tailwind CSS styles
├── App.tsx             # Main application component
├── main.jsx            # Application entry point
├── index.html          # HTML template
├── package.json        # Dependencies
└── vite.config.js      # Vite configuration
```

## Technologies Used

- React 18
- Tailwind CSS v4
- Vite
- Sosemanuk Encryption Algorithm
