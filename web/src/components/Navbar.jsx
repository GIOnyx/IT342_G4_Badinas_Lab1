import { Link } from 'react-router-dom'

export default function Navbar({ user, onLogout }) {
  return (
    <header className="nav">
      <div className="nav-inner">
        <div className="brand"><Link to="/dashboard">MiniApp</Link></div>
        <div className="nav-right">
          <Link to="/items">Items</Link>
          {user ? (
            <>
              <span className="nav-user">{user.name || 'User'}</span>
              <Link to="/profile" className="link-btn" style={{marginRight: '0.5rem'}}>Profile</Link>
              <button className="link-btn" onClick={onLogout}>Logout</button>
            </>
          ) : (
            <>
              <Link to="/login">Login</Link>
              <Link to="/register">Register</Link>
            </>
          )}
        </div>
      </div>
    </header>
  )
}
