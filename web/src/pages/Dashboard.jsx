export default function Dashboard({ user, onLogout }) {
  return (
    <div className="centered-page">
      <div className="centered-content">
        <h2>Dashboard</h2>
        <p className="welcome">Welcome {user ? user.name : 'guest'} — this is a simple dashboard.</p>
        <div style={{height: '16px'}} />
        <button className="logout-btn" onClick={() => {
          fetch(`/api/auth/logout`, { method: 'POST', credentials: 'include' })
            .finally(() => {
              if (onLogout) onLogout()
              else window.location.href = '/login'
            })
        }}>Logout</button>
      </div>
    </div>
  )
}
