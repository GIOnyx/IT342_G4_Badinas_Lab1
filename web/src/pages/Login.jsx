import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Login({ onLogin }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  function handleSubmit(e) {
    e.preventDefault()
    const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
    // call backend login
    fetch(`${API_URL}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    })
    .then(async r => {
      const body = await r.json().catch(() => ({}))
      if (r.ok && body.token) {
        const user = { name: body.name, email: body.email, token: body.token }
        localStorage.setItem('token', body.token)
        onLogin(user)
        navigate('/ingredients')
      } else {
        const msg = body.message || (body.errors ? JSON.stringify(body.errors) : 'Login failed')
        alert(msg)
      }
    })
    .catch(() => alert('Login error'))
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h2>Welcome Back</h2>
        <form className="form" onSubmit={handleSubmit}>
          <label>Email
            <input value={email} onChange={e => setEmail(e.target.value)} type="email" placeholder="you@example.com" required />
          </label>
          <label>Password
            <input value={password} onChange={e => setPassword(e.target.value)} type="password" placeholder="••••••••" required />
          </label>
          <button type="submit">Sign in</button>
        </form>
        <div className="auth-switch">Don't have an account? <a href="/register">Register</a></div>
      </div>
    </div>
  )
}
