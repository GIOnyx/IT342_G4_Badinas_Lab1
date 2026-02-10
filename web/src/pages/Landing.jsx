import { Link } from 'react-router-dom'

export default function Landing() {
  return (
    <div className="landing-page">
      {/* Hero Section */}
      <div className="landing-hero">
        <nav className="landing-nav">
          <div className="landing-nav-container">
            <div className="landing-logo">
              <a href="\src\assets\logo.svg"></a>
              <span className="logo-text">InStock</span>
            </div>
            <div className="landing-nav-links">
              <a href="#about" className="nav-link">About</a>
              <Link to="/register" className="nav-link">Register</Link>
              <Link to="/login" className="nav-link nav-link-primary">Login</Link>
            </div>
          </div>
        </nav>

        <div className="hero-content">
          <h1 className="hero-title">Discover Your Next Favorite Recipe</h1>
          <p className="hero-subtitle">
            Select ingredients you have at home and find delicious recipes instantly.
            Save your favorites and build your personal cookbook.
          </p>
          <div className="hero-buttons">
            <Link to="/register" className="btn-hero-primary">Get Started</Link>
            <Link to="/login" className="btn-hero-secondary">Sign In</Link>
          </div>
        </div>
      </div>

      {/* About Section */}
      <div id="about" className="landing-section">
        <div className="section-container">
          <h2 className="section-title">How It Works</h2>
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon"></div>
              <h3>Select Ingredients</h3>
              <p>Browse through categories and pick ingredients you have available</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon"></div>
              <h3>Find Recipes</h3>
              <p>Discover recipes that match your selected ingredients instantly</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon"></div>
              <h3>Save Favorites</h3>
              <p>Build your personal collection of favorite recipes for quick access</p>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="landing-footer">
        <p>&copy; 2026 InStock. All rights reserved.</p>
      </footer>
    </div>
  )
}
