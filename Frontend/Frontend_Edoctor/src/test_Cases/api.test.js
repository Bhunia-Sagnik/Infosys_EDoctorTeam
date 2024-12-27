
import api from '../services/api'; // Correct path to `api.js`

import mockAxios from 'jest-mock-axios';

jest.mock('axios');

describe('API Service', () => {
  afterEach(() => {
    // Clear all mocks after each test
    mockAxios.reset();
  });

  test('should make a GET request to fetch data', async () => {
    const mockResponse = { data: { message: 'Success' } };

    // Mock the GET request
    mockAxios.get.mockResolvedValueOnce(mockResponse);

    // Call the API
    const response = await api.get('/some-endpoint');

    // Assertions
    expect(mockAxios.get).toHaveBeenCalledWith('/some-endpoint');
    expect(response.data).toEqual(mockResponse.data);
  });

  test('should make a POST request to send data', async () => {
    const mockResponse = { data: { message: 'Created' } };
    const requestData = { username: 'test', password: '1234' };

    // Mock the POST request
    mockAxios.post.mockResolvedValueOnce(mockResponse);

    // Call the API
    const response = await api.post('/auth/login', requestData);

    // Assertions
    expect(mockAxios.post).toHaveBeenCalledWith('/auth/login', requestData);
    expect(response.data).toEqual(mockResponse.data);
  });

  test('should handle API errors', async () => {
    const mockError = {
      response: {
        status: 400,
        data: { error: 'Bad Request' },
      },
    };

    // Mock the error response
    mockAxios.get.mockRejectedValueOnce(mockError);

    try {
      // Call the API
      await api.get('/error-endpoint');
    } catch (error) {
      // Assertions
      expect(mockAxios.get).toHaveBeenCalledWith('/error-endpoint');
      expect(error.response.status).toBe(400);
      expect(error.response.data.error).toBe('Bad Request');
    }
  });
});
