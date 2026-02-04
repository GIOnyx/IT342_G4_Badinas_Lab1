export default function Dashboard({ user }) {
  return (
    <div className="page container">
      <h2>Dashboard</h2>
      <p>Welcome {user ? user.name : 'guest'} — this is a simple dashboard.</p>
    </div>
  )
}
