module.exports = {
    testEnvironment: "jsdom", // Required for React components
    transform: {
      "^.+\\.[t|j]sx?$": "babel-jest", // Transforms modern JavaScript
    },
    moduleNameMapper: {
      "\\.(css|less|sass|scss)$": "identity-obj-proxy", // Mock CSS imports
    },
    transformIgnorePatterns: [
      "node_modules/(?!(axios)/)", // Ensure Jest processes 'axios'
    ],
  };
  