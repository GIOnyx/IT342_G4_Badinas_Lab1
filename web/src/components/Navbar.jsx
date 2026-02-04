import { Link } from 'react-router-dom'

export default function Navbar({ user, onLogout }) {
  return (
    <header className="nav">
      <div className="nav-inner">
        <div className="brand">
          <Link to="/dashboard">MiniApp</Link>
        </div>
        <nav>
          <Link to="/items">Items</Link>
          {user ? (
            <>
              <span className="nav-user">{user.name}</span>
              <button className="link-btn" onClick={onLogout}>Logout</button>
            </>
          ) : (
            <>
              <Link to="/login">Login</Link>
              <Link to="/register">Register</Link>
            </>
          )}
        </nav>
      </div>
    </header>
  )
}
