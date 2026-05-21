module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/tests/setupTests.ts'],
  testMatch: [
    '<rootDir>/tests/accessibility/**/*.spec.tsx',
    '<rootDir>/tests/integration/**/*.spec.tsx'
  ]
};
