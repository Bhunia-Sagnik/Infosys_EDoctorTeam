import React from "react";
import { Link } from "react-router-dom";
import "../CSS/Home.css";

function Home() {
  return (
    <body className="home-page">
      <div className="home-container">
        <header className="hero-section">
          <div className="hero-content">
            <h1>Welcome to EDoctor</h1>
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
          <h2>Why Choose E-Doctor?</h2>
          <div className="features">
            <div className="feature">
              <h3>Easy Scheduling</h3>
              <p>
                Quickly schedule and manage your appointments without any
                hassle.
              </p>
            </div>
            <div className="feature">
              <h3>Seamless Experience</h3>
              <p>
                A smooth, intuitive interface tailored to your specific needs.
              </p>
            </div>
            <div className="feature">
              <h3>Data Security</h3>
              <p>
                We prioritize your privacy and security at every step.
              </p>
            </div>
          </div>
        </section>
      </div>
    </body>
  );
}

export default Home;
