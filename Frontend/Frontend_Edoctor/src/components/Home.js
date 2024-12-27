import React from "react";
import { Link } from "react-router-dom";
import "../CSS/Home.css"; // Updated CSS for the landing page

function Home() {
  return (
    <div className="body1">
      <div className="home-container">
        <header className="hero-section">
          <div className="hero-content">
            <h1>Welcome to E-Doctor</h1>
            <p>
              Your one-stop solution for managing outpatient appointments
              efficiently.
            </p>
            <div className="hero-buttons">
              <Link to="/register" className="btn">
                Get Started
              </Link>
              <Link to="/login" className="btn">
                Login
              </Link>
            </div>
          </div>
        </header>
        <section className="features-section">
          <h2>Why Choose Us?</h2>
          <div className="features">
            <div className="feature">
              <h3>Easy Scheduling</h3>
              <p>
                Schedule appointments with ease, whether you're a doctor or a
                patient.
              </p>
            </div>
            <div className="feature">
              <h3>Seamless Experience</h3>
              <p>
                Enjoy a smooth and user-friendly interface tailored to your
                needs.
              </p>
            </div>
            <div className="feature">
              <h3>Secure Platform</h3>
              <p>
                Your data is safe with us. Privacy and security are our
                priorities.
              </p>
            </div>
          </div>
        </section>
      </div>
    </div>
  );
}

export default Home;
