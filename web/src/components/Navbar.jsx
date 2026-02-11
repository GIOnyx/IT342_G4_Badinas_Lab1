import { Link } from 'react-router-dom'

export default function Navbar({ user, onLogout }) {
  return (
    <nav className="navbar">
      <div className="nav-left">
        <Link to="/dashboard" className="brand">MiniApp</Link>
      </div>
      <div className="nav-right">
        <Link to="/items">Items</Link>
        {user ? (
          <>
            <span className="nav-user">{user.username || 'User'}</span>
            <button onClick={onLogout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </>
        )}
      </div>
    </nav>
  )
}
