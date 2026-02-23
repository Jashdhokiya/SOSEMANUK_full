# SOSEMANUK Encryptor - Full Stack Web Application

## Installation Guide

### 1. Download the Project

Either:

- Download the project as a ZIP file and extract it

OR

- Clone the repository:

```bash
git clone https://github.com/Jashdhokiya/SOSEMANUK_full
cd <project-folder>
```

------------------------------------------------------------

Frontend Setup (ReactJS)

1. Check Node.js version

Make sure Node.js is installed and version 22 or higher:

```bash
node -v
```

2. Navigate to frontend folder

```bash
cd frontend
```

3. Install dependencies

```bash
npm install
```

4. Run the frontend

```bash
npm run dev
```

Open your browser and go to:

http://localhost:5173

------------------------------------------------------------

Backend Setup (Spring Boot)

1. Check Java version

Make sure Java 21 or higher is installed:

```bash
java -version
```

2. Navigate to backend folder

```bash
cd backend
```

3. Run the backend

For Linux / MacOS:

```bash
./mvnw spring-boot:run
```

For Windows:

```bash
mvnw.cmd spring-boot:run
```

Backend will start at:

http://localhost:8080

------------------------------------------------------------

Working

- Upload your PDF or text file.
- Enter the Initialization Vector (IV) of your choice.
- Enter the Secret Key of your choice.
- Choose encryption or decryption.
- Download the processed file.

Important: You must remember your IV and Key.
Without the correct IV and Key, you will not be able to decrypt your file and recover the original content.

------------------------------------------------------------

Important Notes

- Run both frontend and backend at the same time.
- Make sure the backend runs on port 8080 only.
- If the backend port is changed, update the API URL in the frontend accordingly.
