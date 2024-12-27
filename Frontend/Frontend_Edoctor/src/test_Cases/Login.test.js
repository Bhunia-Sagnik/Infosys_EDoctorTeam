import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import '@testing-library/jest-dom';
import Login from '../components/Login';
import api from '../services/api'; // Mock the Axios instance

jest.mock('../services/api'); // Mock the API module

describe('Login Component', () => {
  it('renders the login form correctly', () => {
    render(
      <Router>
        <Login />
      </Router>
    );

    // Check for form inputs and button
    expect(screen.getByLabelText(/Username:/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Password:/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /login/i })).toBeInTheDocument();

    // Check for footer links
    expect(screen.getByText(/Forgot Password\?/i)).toBeInTheDocument();
    expect(screen.getByText(/Don't have an account\?/i)).toBeInTheDocument();
  });

  it('handles input changes', () => {
    render(
      <Router>
        <Login />
      </Router>
    );

    const usernameInput = screen.getByPlaceholderText(/Enter your username/i);
    const passwordInput = screen.getByPlaceholderText(/Enter your password/i);

    fireEvent.change(usernameInput, { target: { value: 'testuser' } });
    fireEvent.change(passwordInput, { target: { value: 'password123' } });

    expect(usernameInput.value).toBe('testuser');
    expect(passwordInput.value).toBe('password123');
  });

  it('displays a success message and redirects on successful login', async () => {
    const mockResponse = {
      data: { name: 'Test User', role: 'ADMIN' },
    };

    api.post.mockResolvedValueOnce(mockResponse);

    render(
      <Router>
        <Login />
      </Router>
    );

    const usernameInput = screen.getByPlaceholderText(/Enter your username/i);
    const passwordInput = screen.getByPlaceholderText(/Enter your password/i);
    const loginButton = screen.getByRole('button', { name: /login/i });

    fireEvent.change(usernameInput, { target: { value: 'testuser' } });
    fireEvent.change(passwordInput, { target: { value: 'password123' } });
    fireEvent.click(loginButton);

    // Wait for message to appear
    const successMessage = await screen.findByText(/Welcome Test User! Redirecting to your dashboard.../i);
    expect(successMessage).toBeInTheDocument();
  });

  it('displays an error message on failed login', async () => {
    const mockError = {
      response: {
        data: { error: 'Invalid credentials' },
      },
    };

    api.post.mockRejectedValueOnce(mockError);

    render(
      <Router>
        <Login />
      </Router>
    );

    const usernameInput = screen.getByPlaceholderText(/Enter your username/i);
    const passwordInput = screen.getByPlaceholderText(/Enter your password/i);
    const loginButton = screen.getByRole('button', { name: /login/i });

    fireEvent.change(usernameInput, { target: { value: 'wronguser' } });
    fireEvent.change(passwordInput, { target: { value: 'wrongpassword' } });
    fireEvent.click(loginButton);

    // Wait for error message to appear
    const errorMessage = await screen.findByText(/Invalid credentials/i);
    expect(errorMessage).toBeInTheDocument();
  });
});
