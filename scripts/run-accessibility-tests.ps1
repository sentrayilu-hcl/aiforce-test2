$ErrorActionPreference = "Stop"

npm run test -- tests/accessibility/accessibility.unit.spec.tsx
npm run test -- tests/integration/accessibility/accessibility.integration.spec.tsx
npx playwright test e2e/accessibility/accessibility.spec.ts
