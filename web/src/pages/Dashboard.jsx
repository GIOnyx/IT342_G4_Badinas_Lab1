import { Link } from 'react-router-dom'

export default function Dashboard({ user, onLogout }) {
  return (
    <div className="centered-page">
      <div className="centered-content">
        <h2>Dashboard</h2>
        <p className="welcome">Welcome {user ? user.name : 'guest'} — this is a simple dashboard.</p>
        <div style={{height: '16px'}} />
        <div style={{display: 'flex', gap: '0.5rem', justifyContent: 'center'}}>
          <Link to="/profile"><button className="primary-btn">Profile</button></Link>
          <button className="logout-btn" onClick={() => {
            if (onLogout) onLogout()
            else {
              fetch(`/api/auth/logout`, { method: 'POST', credentials: 'include' })
                .finally(() => window.location.href = '/login')
            }
          }}>Logout</button>
        </div>
      </div>
    </div>
  )
}
