# Product Catalog Monorepo

This repository contains:

- `backend/`: Java 21 Spring Boot API with H2 in-memory database.
- `frontend/`: React + TypeScript web app for searching and viewing products.

## Backend quick start

```bash
cd backend
mvn spring-boot:run
```

API: `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui.html`  
H2 console: `http://localhost:8080/h2-console`

## Frontend quick start

```bash
cd frontend
npm install
npm run dev
```

App: `http://localhost:5173`

The frontend calls the backend at `http://localhost:8080` by default (see `frontend/.env`). Start the backend first, then the frontend.

### Tests

```bash
cd frontend
npm test
```

### Build for production

```bash
cd frontend
npm run build
npm run preview
```

## Development workflow

Run both services in separate terminals:

```bash
# Terminal 1 — API
cd backend && mvn spring-boot:run

# Terminal 2 — web UI
cd frontend && npm run dev
```
