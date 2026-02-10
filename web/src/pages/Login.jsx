import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'

export default function Login({ onLogin }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  function handleSubmit(e) {
    e.preventDefault()
    // call backend login (use dev proxy or same-origin)
    fetch(`/api/auth/login`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    })
    .then(async r => {
      const body = await r.json().catch(() => ({}))
      if (r.ok) {
        const user = { name: body.name, email: body.email }
        // token is stored as HttpOnly cookie now; do not persist in localStorage
        onLogin(user)
        navigate('/dashboard')
      } else {
        const msg = body.message || (body.errors ? JSON.stringify(body.errors) : 'Login failed')
        alert(msg)
      }
    })
    .catch(() => alert('Login error'))
  }

  return (
    <div className="auth-page">
      <div className="auth-content">
        <div className="auth-side">
          <h1 className="brand">MiniApp</h1>
          <p className="lead">Welcome to MiniApp — a tiny demo application. Sign in to access your dashboard and items.</p>
        </div>
        <div className="auth-card">
          <h2>Login</h2>
          <form className="form" onSubmit={handleSubmit}>
            <label>Email
              <input value={email} onChange={e => setEmail(e.target.value)} type="email" required />
            </label>
            <label>Password
              <input value={password} onChange={e => setPassword(e.target.value)} type="password" required />
            </label>
            <button type="submit">Sign in</button>
          </form>
          <div className="auth-switch">Don't have an account? <Link to="/register">Register</Link></div>
        </div>
      </div>
    </div>
  )
}
