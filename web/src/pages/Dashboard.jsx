export default function Dashboard({ user }) {
  return (
    <div className="page">
      <h2>Dashboard</h2>
      <p>Welcome back, <strong>{user ? user.name : 'Guest'}</strong>! Ready to cook something delicious?</p>
      <div className="dashboard-grid">
        <div className="stat-card">
          <div className="stat-value">🥕</div>
          <div className="stat-label">Browse ingredients and build your pantry</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">📖</div>
          <div className="stat-label">Find recipes based on what you have</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">📦</div>
          <div className="stat-label">Manage your saved items</div>
        </div>
      </div>
    </div>
  )
}
